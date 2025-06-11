import java.util.*;

// Simulation.java - Main controller
public class Simulation {
    private static int gridWidth;
    private static int gridHeight;
    private static Point startPoint;
    private static Point endPoint;
    private static List<CostZone> costZones;
    private static List<Point> obstacles;
    private static int maxCost;
    private static int maxTime;
    private static int populationSize;
    private static int maxPopulation;
    private static double deathRate;
    private static double reproductionRate;
    private static double mutationRate;
    private static double moveRate;
    private static double comfortThreshold;

    private List<Individual> population;
    private PriorityQueue<Event> pendingEvents;
    private double currentTime;
    private Individual bestIndividual;

    public Simulation(Params params) {
        // Initialize static parameters
        gridWidth = params.n;
        gridHeight = params.m;
        startPoint = params.start;
        endPoint = params.end;
        costZones = params.zones;
        obstacles = params.obstacles;
        maxCost = params.cmax;
        maxTime = params.tmax; // Reduced maximum time
        populationSize = params.popsize; // Reduced population size
        maxPopulation = params.maxpop; // Reduced maximum population
        deathRate = params.deathrate;
        reproductionRate = params.reprate;
        mutationRate = params.mutrate;
        moveRate = params.moverate;
        comfortThreshold = params.comfort;

        //System.out.println("n"+ gridWidth + " m" + gridHeight);
        //System.out.println("Start: " + startPoint + ", End: " + endPoint);
        //System.out.println("Cost Zones: " + costZones);
        //System.out.println("Obstacles: " + obstacles);
        //System.out.println("Max Cost: " + maxCost);
        //System.out.println("Max Time: " + maxTime);
        //System.out.println("Population Size: " + populationSize);
        //System.out.println("Max Population: " + maxPopulation);
        //System.out.println("Death Rate: " + deathRate);
        //System.out.println("Reproduction Rate: " + reproductionRate);
        //System.out.println("Mutation Rate: " + mutationRate);
        //System.out.println("Move Rate: " + moveRate);
        //System.out.println("Comfort Threshold: " + comfortThreshold);


        // Initialize simulation state
        population = new ArrayList<>();
        pendingEvents = new PriorityQueue<>(Comparator.comparingDouble(Event::getTime));
        currentTime = 0;
        bestIndividual = null;

        // Create initial population
        for (int i = 0; i < populationSize; i++) {
            Individual ind = new Individual(startPoint);
            population.add(ind);
            scheduleEvents(ind);
        }
    }

    public void run() {
        while (!pendingEvents.isEmpty() && currentTime < maxTime) {
            Event event = pendingEvents.poll();
            currentTime = event.getTime();
            event.execute(this);
        }

        // Print final results
        System.out.println("Simulation ended at time " + currentTime);
        if (bestIndividual != null) {
            System.out.println("Best individual found:");
            System.out.println("Path: " + bestIndividual.getPath());
            System.out.println("Comfort: " + bestIndividual.getComfort());
        } else {
            System.out.println("No solution found");
        }
    }

    private void scheduleEvents(Individual ind) {
        // Schedule death event
        double deathTime = currentTime + calculateDeathTime(ind);
        pendingEvents.add(new DeathEvent(deathTime, ind));
        ind.setDeathTime(deathTime);

        // Schedule move event
        double moveTime = currentTime + calculateMoveTime();
        pendingEvents.add(new MoveEvent(moveTime, ind));

        // Schedule reproduction event
        double reproductionTime = currentTime + calculateReproductionTime(ind);
        pendingEvents.add(new ReproductionEvent(reproductionTime, ind));
    }

    private double calculateDeathTime(Individual ind) {
        return -Math.log(Math.random()) / (deathRate * (1 - ind.getComfort()));
    }

    private double calculateMoveTime() {
        return -Math.log(Math.random()) / moveRate;
    }

    private double calculateReproductionTime(Individual ind) {
        return -Math.log(Math.random()) / (reproductionRate * ind.getComfort());
    }

    public void handleDeath(Individual individual) {
        Point currentPos = individual.getCurrentPosition();
        if (currentPos.x >= 1 && currentPos.x <= gridWidth && currentPos.y >= 1 && currentPos.y <= gridHeight) {
            System.out.println("Death at time " + currentTime + " for individual at position " + currentPos);
            System.out.println("Path taken: " + individual.getPath());
            System.out.println("Comfort level: " + individual.getComfort());
        }
        population.remove(individual);
        if (population.size() < maxPopulation) {
            // Create new individual to maintain population size
            Individual newInd = new Individual(startPoint);
            population.add(newInd);
            scheduleEvents(newInd);
        }
    }

    public void handleMove(Individual individual) {
        Point current = individual.getCurrentPosition();
        if (current.equals(endPoint)) {
            bestIndividual = individual;
            System.out.println("🎉 Solution found! Path: " + individual.getPath());
            System.out.println("Comfort: " + individual.getComfort());
            return;
        }
        List<Point> possibleMoves = getPossibleMoves(current);
        if (!possibleMoves.isEmpty()) {
            Point nextMove = possibleMoves.get((int) (Math.random() * possibleMoves.size()));
            individual.move(nextMove);
            scheduleEvents(individual);
        }
    }

    public void handleReproduction(Individual individual) {
        if (population.size() < maxPopulation && individual.getComfort() > comfortThreshold) {
            Individual child = individual.reproduce();
            population.add(child);
            scheduleEvents(child);
        }
    }

    private List<Point> getPossibleMoves(Point current) {
        List<Point> moves = new ArrayList<>();
        // Directions: North, East, South, West (1-based coordinates)
        int[] dx = {0, 1, 0, -1};  // Changed from {-1, 0, 1, 0}
        int[] dy = {1, 0, -1, 0};  // Changed from {0, 1, 0, -1}

        for (int i = 0; i < 4; i++) {
            int newX = current.x + dx[i];
            int newY = current.y + dy[i];
            Point next = new Point(newX, newY);

            if (isValidMove(next)) {
                moves.add(next);
            }
        }

        return moves;
    }

    private boolean isValidMove(Point p) {
        // Check grid boundaries
        if (p.x < 1 || p.x > gridWidth || p.y < 1 || p.y > gridHeight) {
            return false;
        }

        // Check obstacles
        for (Point obstacle : obstacles) {
            if (p.x == obstacle.x && p.y == obstacle.y) {
                return false;
            }
        }

        return true;
    }

    public static double getEdgeCost(Point p1, Point p2) {
        // Check if either point is in a cost zone
        for (CostZone zone : costZones) {
            if (isPointInZone(p1, zone) || isPointInZone(p2, zone)) {
                return zone.getCost();
            }
        }
        return 1.0; // Default cost
    }

    private static boolean isPointInZone(Point p, CostZone zone) {
        return p.x >= zone.getTopLeft().x && p.x <= zone.getBottomRight().x &&
               p.y >= zone.getTopLeft().y && p.y <= zone.getBottomRight().y;
    }

    public static int getGridWidth() {
        return gridWidth;
    }

    public static int getGridHeight() {
        return gridHeight;
    }

    public static Point getStartPoint() {
        return startPoint;
    }

    public static Point getEndPoint() {
        return endPoint;
    }

    public static int getMaxCost() {
        return maxCost;
    }
}

