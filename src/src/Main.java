// Main.java - Entry point
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

public class Main {
    public static void main(String[] args) {
        Params params;

        try {
            //Params params;
            if (args[0].equals("-f")) {
                params = ParamsParser.fromFile(args[1]);
            } else if (args[0].equals("-r")) {
                params = ParamsParser.randomParams(args);
            } else {
                throw new IllegalArgumentException("Invalid mode. Use -f for file or -r for random.");
            }

            
            Simulation simulation = new Simulation(params);
            simulation.run();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}

/*public class Main {
    public static void main(String[] args) {
        int width = 5;
        int height = 5;

        Set<Point> obstacles = new HashSet<>(); // No obstacles so we can see all edges

        List<CostZone> costZones = new ArrayList<>();
        costZones.add(new CostZone(new Point(2, 2), new Point(3, 3), 5)); // Cost zone with cost 5

        Grid grid = new Grid(width, height, obstacles, costZones);

        Point current = new Point(2, 2);
        List<Point> moves = grid.getPossibleMoves(current);

        System.out.println("Valid moves from " + current + ":");
        for (Point p : moves) {
            int cost = grid.getEdgeCost(current, p);
            System.out.println(" → " + p + " (cost: " + cost + ")");
        }
    }
}*/

