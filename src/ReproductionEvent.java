public class ReproductionEvent extends Event {
    public ReproductionEvent(double time, Individual individual) {
        super(EventType.REPRODUCTION, time, individual);
    }

    @Override
    public void execute(Simulation simulation) {
        simulation.handleReproduction(individual);
    }
}
