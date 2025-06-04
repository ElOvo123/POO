import java.util.List;
import java.util.Comparator;


public class MoveEvent extends Event {
    public MoveEvent(double time, Individual individual) {
        super(time, individual, EventType.MOVE);
    }

    @Override
    public void execute(Simulation simulation) {
        Point current = individual.getCurrentPosition();
        List<Point> moves = simulation.getGrid().getPossibleMoves(current);

        if (!moves.isEmpty()) {
            // Sort possible moves by distance to goal
            moves.sort(Comparator.comparingInt((Point p) -> simulation.getGrid().calculateDistance(p, simulation.getParams().end)));

            // Move toward the closest
            Point newPos = moves.get(0);
            individual.move(newPos, simulation.getGrid(), simulation.getParams());
            individual.calculateComfort(simulation.getParams(), simulation.getGrid());

            System.out.println("🚶‍♂️ Moved to " + newPos + " | Comfort: " + individual.getComfort());

            // 🎯 Check if reached goal
            if (newPos.equals(simulation.getParams().end)) {
                System.out.println("🎯 Reached the goal at time " + time + "!");
            }
        }

        // Schedule next move
        double nextTime = time + calculateNextMoveTime(individual, simulation);
        simulation.getPEC().addEvent(new MoveEvent(nextTime, individual));
    }

    private double calculateNextMoveTime(Individual ind, Simulation simulation) {
        double safeComfort = Math.max(ind.getComfort(), 1e-3);
        return -Math.log(1 - Math.random()) * (1 - Math.log(safeComfort)) * 
            simulation.getParams().delta;
    }
}
