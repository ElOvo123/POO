public class DeathEvent extends Event {

    public DeathEvent(double time, Individual individual) {
        super(time, individual, EventType.DEATH);  // Correct constructor
    }

    @Override
    public void execute(Simulation simulation) {
        System.out.println("💀 Death at time " + time);
        simulation.getPopulation().remove(individual);
    }

}

