public class DeathEvent extends Event {

    public DeathEvent(double time, Individual individual) {
        super(EventType.DEATH, time, individual);
    }

    @Override
    public void execute(Simulation simulation) {
        //System.out.println("💀 Death at time " + time);
        simulation.handleDeath(individual);
    }

}

