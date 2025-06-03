public class DeathEvent extends Event {
    public DeathEvent(double time, Individual individual) {
        super(time, individual, EventType.DEATH);
    }

    @Override
    public void execute(Simulation simulation) {
        simulation.getPopulation().remove(individual);
    }
    public class DeathEvent extends Event {
    public DeathEvent(double time, Individual individual) {
        super(time, individual, EventType.DEATH);
    }

    @Override
    public void execute(Simulation simulation) {
        simulation.getPopulation().remove(individual);
    }
}
}