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
        System.out.println("👶 Reproduction at time " + time);

        simulation.getPopulation().add(child);

        // Schedule events for child
        simulation.getPEC().addEvent(new MoveEvent(
            time + calculateNextMoveTime(simulation, child), child));

        simulation.getPEC().addEvent(new ReproductionEvent(
            time + calculateNextReproductionTime(simulation, child), child));

        double nextTime = time + calculateNextReproductionTime(simulation, individual);
        simulation.getPEC().addEvent(new ReproductionEvent(nextTime, individual));

    }

    private double calculateNextMoveTime(Simulation simulation, Individual ind) {
        double safeComfort = Math.max(ind.getComfort(), 1e-3);
        return -Math.log(1 - Math.random()) * (1 - Math.log(safeComfort)) *
            simulation.getParams().delta;
    }

    private double calculateNextReproductionTime(Simulation simulation, Individual ind) {
        double safeComfort = Math.max(ind.getComfort(), 1e-3);
        return -Math.log(1 - Math.random()) * (1 - Math.log(safeComfort)) *
            simulation.getParams().rho;
    }
}
