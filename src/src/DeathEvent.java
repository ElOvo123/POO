public class DeathEvent extends Event {

    public DeathEvent(double time, Individual individual) {
        super(time, individual, EventType.DEATH);  // Correct constructor
    }

    @Override
    public void execute(Simulation simulation) {
        simulation.getIndividuals().remove(getIndividual());
    }
}

