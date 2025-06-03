public class ReproductionEvent extends Event {
    public ReproductionEvent(double time, Individual individual) {
        super(time, individual, EventType.REPRODUCTION);
    }

    @Override
    public void execute(Simulation simulation) {
        if (time > individual.getDeathTime() || time > simulation.getParams().finalTime) {
            return;
        }
        
        // Create child
        Individual child = individual.reproduce();
        simulation.getPopulation().add(child);
        
        // Schedule events for child
        double deathTime = -Math.log(1 - Math.random()) * simulation.getParams().mu;
        simulation.getPEC().addEvent(new DeathEvent(deathTime, child));
        simulation.getPEC().addEvent(new MoveEvent(
            time + calculateNextMoveTime(child), child));
        simulation.getPEC().addEvent(new ReproductionEvent(
            time + calculateNextReproductionTime(child), child));
        
        // Schedule next reproduction for parent
        double nextTime = time + calculateNextReproductionTime();
        simulation.getPEC().addEvent(new ReproductionEvent(nextTime, individual));
    }
    
    private double calculateNextMoveTime(Individual ind) {
        return -Math.log(1 - Math.random()) * (1 - Math.log(ind.getComfort())) * 
               simulation.getParams().delta;
    }
    
    private double calculateNextReproductionTime(Individual ind) {
        return -Math.log(1 - Math.random()) * (1 - Math.log(ind.getComfort())) * 
               simulation.getParams().rho;
    }
    
    private double calculateNextReproductionTime() {
        return -Math.log(1 - Math.random()) * (1 - Math.log(individual.getComfort())) * 
               simulation.getParams().rho;
    }
}