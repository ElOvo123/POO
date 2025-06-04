import java.util.List;

public class MoveEvent extends Event {
    public MoveEvent(double time, Individual individual) {
        super(time, individual, EventType.MOVE);
    }

    @Override
    public void execute(Simulation simulation) {
        if (time > individual.getDeathTime() || time > simulation.getParams().finalTime) {
            return;
        }

        // Get possible moves
        List<Point> moves = simulation.getGrid().getPossibleMoves(individual.getCurrentPosition());
        if (!moves.isEmpty()) {
            // Choose random move
            Point newPos = moves.get((int)(Math.random() * moves.size()));
            individual.move(newPos);
        }

        // Schedule next move
        double nextTime = time + calculateNextMoveTime(simulation);
        simulation.getPEC().addEvent(new MoveEvent(nextTime, individual));
    }

    private double calculateNextMoveTime(Simulation simulation) {
        return -Math.log(1 - Math.random()) * (1 - Math.log(individual.getComfort())) *
               simulation.getParams().delta;
    }
}
