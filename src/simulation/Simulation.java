package simulation;

import java.util.*;
import model.Point;
import model.Individual;
import model.CostZone;
import model.Params;
import events.Event;
import events.DeathEvent;
import events.MoveEvent;
import events.ReproductionEvent;

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

    private int realizedEvents = 0;
    private List<Individual> allIndividuals = new ArrayList<>();
    private double[] observationTimes;
    private int nextObservationIndex = 0;
    private Individual bestFitIndividual = null;
    private boolean finalPointHit = false;

    public Simulation(Params params) {
        // Initialize static parameters
        gridWidth = params.getN();
        gridHeight = params.getM();
        startPoint = params.getStart();
        endPoint = params.getEnd();
        costZones = params.getZones();
        obstacles = params.getObstacles();
        maxCost = params.getCmax();
        maxTime = params.getTmax(); // Reduced maximum time
        populationSize = params.getPopsize(); // Reduced population size
        maxPopulation = params.getMaxpop(); // Reduced maximum population
        deathRate = params.getDeathrate();
        reproductionRate = params.getReprate();
        mutationRate = params.getMutrate();
        moveRate = params.getMoverate();
        comfortThreshold = params.getComfort();

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
            allIndividuals.add(ind);
            scheduleEvents(ind);
        }
        // Prepare observation times (21 evenly spaced)
        observationTimes = new double[21];
        for (int i = 0; i <= 20; i++) {
            observationTimes[i] = i * (maxTime / 20.0);
        }
    }

    public void run() {
        while (!pendingEvents.isEmpty() && currentTime < maxTime) {
            Event event = pendingEvents.poll();
            currentTime = event.getTime();
            event.execute(this);
            realizedEvents++;
            // Print observation if time reached
            while (nextObservationIndex < observationTimes.length && currentTime >= observationTimes[nextObservationIndex]) {
                printObservation();
                nextObservationIndex++;
            }
        }
        // Print any remaining observations
        while (nextObservationIndex < observationTimes.length) {
            printObservation();
            nextObservationIndex++;
        }
        printBestFitIndividual();
    }

    private void printObservation() {
        Individual best = getBestIndividual();
        boolean hit = best != null && best.getCurrentPosition().equals(endPoint);
        System.out.println("Observation number: " + nextObservationIndex);
        System.out.println("Present time: " + String.format("%.2f", Math.min(currentTime, maxTime)));
        System.out.println("Number of realized events: " + realizedEvents);
        System.out.println("Population size: " + population.size());
        System.out.println("Final point has been hit: " + (hit ? "yes" : "no"));
        System.out.print("Path of the best fit individual: ");
        System.out.println(best != null ? best.getPath() : "[]");
        System.out.print("Cost/Comfort: ");
        if (hit) {
            System.out.println(String.format("%.2f", best.getPathCost()));
        } else {
            System.out.println(String.format("%.5f", best != null ? best.getComfort() : 0.0));
        }
        System.out.println();
    }

    private Individual getBestIndividual() {
        // Among all individuals ever created, find the best fit
        Individual best = null;
        double bestCost = Double.MAX_VALUE;
        double bestComfort = -1;
        for (Individual ind : allIndividuals) {
            boolean atEnd = ind.getCurrentPosition().equals(endPoint);
            if (atEnd) {
                double cost = ind.getPathCost();
                if (cost < bestCost) {
                    bestCost = cost;
                    best = ind;
                }
            } else if (best == null && ind.getComfort() > bestComfort) {
                bestComfort = ind.getComfort();
                best = ind;
            }
        }
        return best;
    }

    private void printBestFitIndividual() {
        Individual best = getBestIndividual();
        System.out.println("Best fit individual:");
        if (best != null && best.getCurrentPosition().equals(endPoint)) {
            System.out.println(best.getPath() + " with cost " + String.format("%.2f", best.getPathCost()));
        } else if (best != null) {
            System.out.println(best.getPath() + " with comfort " + String.format("%.5f", best.getComfort()));
        } else {
            System.out.println("[]");
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
        if (currentPos.getX() >= 1 && currentPos.getX() <= gridWidth && currentPos.getY() >= 1 && currentPos.getY() <= gridHeight) {
            //System.out.println("Death at time " + currentTime + " for individual at position " + currentPos);
            //System.out.println("Path taken: " + individual.getPath());
            //System.out.println("Comfort level: " + individual.getComfort());
        }
        population.remove(individual);
        if (population.size() < maxPopulation) {
            // Create new individual to maintain population size
            Individual newInd = new Individual(startPoint);
            population.add(newInd);
            allIndividuals.add(newInd);
            scheduleEvents(newInd);
        }
    }

    public void handleMove(Individual individual) {
        Point current = individual.getCurrentPosition();
        if (current.equals(endPoint)) {
            bestIndividual = individual;
            return;
        }
        List<Point> possibleMoves = getPossibleMoves(current);
        int n = possibleMoves.size();
        if (n > 0) {
            double rand = Math.random();
            int chosenIdx = -1;
            for (int i = 0; i < n; i++) {
                if (rand <= (double)(i + 1) / n) {
                    chosenIdx = i;
                    break;
                }
            }
            if (chosenIdx == -1) chosenIdx = n - 1; // fallback, should not happen
            Point nextMove = possibleMoves.get(chosenIdx);
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
        int[] dx = {0, 1, 0, -1};
        int[] dy = {1, 0, -1, 0};

        for (int i = 0; i < 4; i++) {
            int newX = current.getX() + dx[i];
            int newY = current.getY() + dy[i];
            Point next = new Point(newX, newY);

            if (isValidMove(next)) {
                moves.add(next);
            }
        }

        return moves;
    }

    private boolean isValidMove(Point p) {
        // Check grid boundaries
        if (p.getX() < 1 || p.getX() > gridWidth || p.getY() < 1 || p.getY() > gridHeight) {
            return false;
        }

        // Check obstacles
        for (Point obstacle : obstacles) {
            if (p.getX() == obstacle.getX() && p.getY() == obstacle.getY()) {
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
        return p.getX() >= zone.getTopLeft().getX() && p.getX() <= zone.getBottomRight().getX() &&
               p.getY() >= zone.getTopLeft().getY() && p.getY() <= zone.getBottomRight().getY();
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