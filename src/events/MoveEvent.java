package events;

import simulation.Simulation;
import model.Individual;

public class MoveEvent extends Event {
    public MoveEvent(double time, Individual individual) {
        super(EventType.MOVE, time, individual);
    }

    @Override
    public void execute(Simulation simulation) {
        simulation.handleMove(individual);
    }
}