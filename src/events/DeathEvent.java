package events;

import simulation.Simulation;
import model.Individual;
public class DeathEvent extends Event {

    public DeathEvent(double time, Individual individual) {
        super(EventType.DEATH, time, individual);
    }

    @Override
    public void execute(Simulation simulation) {
        simulation.handleDeath(individual);
    }

}