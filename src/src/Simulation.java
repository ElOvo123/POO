import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
// Simulation.java - Main controller
public class Simulation {
    private final Params params;
    private final Grid grid;
    private final List<Individual> population;
    private final PEC pec;
    private double currentTime;
    private int eventCount;
    private int observationCount = 0;
    
    public Simulation(Params params) {
        this.params = params;
        this.grid = new Grid(params.n, params.m, params.obstacles, params.costZones);
        this.population = new ArrayList<>();
        this.pec = new PEC();
        initialize();
    }
    
    private void initialize() {
        // Create initial population
        for (int i = 0; i < params.initialPop; i++) {
            double deathTime = -Math.log(1 - Math.random()) * params.mu;
            Individual ind = new Individual(params.start, deathTime);
            population.add(ind);
            
            // Schedule initial events
            pec.addEvent(new DeathEvent(deathTime, ind));
            pec.addEvent(new MoveEvent(calculateNextMoveTime(ind), ind));
            pec.addEvent(new ReproductionEvent(calculateNextReproductionTime(ind), ind));
        }
    }
    
    public void run() {
        printParameters();
        observe(0); // Initial observation
        
        double observationInterval = params.finalTime / 20;
        double nextObservationTime = observationInterval;
        
        while (currentTime < params.finalTime && !pec.isEmpty()) {
            Event event = pec.nextEvent();
            currentTime = event.time;
            
            if (currentTime > params.finalTime) break;
            
            event.execute(this);
            eventCount++;
            
            // Check for epidemic
            if (population.size() > params.maxPop) {
                handleEpidemic();
            }
            
            // Check for observation time
            if (currentTime >= nextObservationTime) {
                observe(nextObservationTime);
                nextObservationTime += observationInterval;
            }
        }
        
        printFinalResult();
    }
    
    private void handleEpidemic() {
        // Sort population by comfort (descending)
        population.sort(Collections.reverseOrder());
        
        // Always keep top 5
        List<Individual> survivors = new ArrayList<>(population.subList(0, Math.min(5, population.size())));
        
        // For the rest, survival based on comfort
        for (int i = 5; i < population.size(); i++) {
            Individual ind = population.get(i);
            if (Math.random() <= ind.getComfort()) {
                survivors.add(ind);
            }
        }
        
        population.clear();
        population.addAll(survivors);
    }
    
    private void observe(double time) { System.out.println("| Observation " + observationCount + ": | Present time: | " + time + " |"); }
    private void printParameters() {System.out.println(n + " " + m + " " + start.x + " " + start.y + " " + 
                      end.x + " " + end.y + " " + costZones.size() + " " + 
                      obstacles.size() + " " + finalTime + " " + initialPop + 
                      " " + maxPop + " " + k + " " + mu + " " + delta + " " + rho);}
    private void printFinalResult() { Individual best = findBestIndividual();
    System.out.println("Best fit individual: " + best.getPath() + 
                       " with cost " + best.getCost()); }

    // Add these methods to Simulation class
private double calculateNextMoveTime(Individual ind) {
    return -Math.log(1 - Math.random()) * (1 - Math.log(ind.getComfort())) * params.delta;
}

private double calculateNextReproductionTime(Individual ind) {
    return -Math.log(1 - Math.random()) * (1 - Math.log(ind.getComfort())) * params.rho;
}

private Individual findBestIndividual() {
    Individual best = null;
    for (Individual ind : population) {
        if (best == null || 
            (ind.getCurrentPosition().equals(params.end) && 
             (best.getCurrentPosition().equals(params.end) ? 
              ind.getCost(grid) < best.getCost(grid) : true)) ||
            (!ind.getCurrentPosition().equals(params.end) && 
             !best.getCurrentPosition().equals(params.end) && 
             ind.getComfort() > best.getComfort())) {
            best = ind;
        }
    }
    return best;
}

public Params getParams() {
    return params;
}

public Grid getGrid() {
    return grid;
}

public PEC getPEC() {
    return pec;
}

public List<Individual> getPopulation() {
    return population;
}

private double getMaxEdgeCost(List<CostZone> costZones) {
    int maxCost = 1;
    for (CostZone zone : costZones) {
        maxCost = Math.max(maxCost, zone.getCost());
    }
    return maxCost;
}
}