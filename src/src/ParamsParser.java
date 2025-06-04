import java.io.IOException;
import java.util.*;

public class ParamsParser {
    public static Params fromFile(String filename) throws IOException {
        // You'd normally read from a file. Here's a placeholder for now.
        // Replace this with actual file parsing logic.
        Point start = new Point(1, 1);
        Point end = new Point(5, 5);
        List<CostZone> costZones = new ArrayList<>();
        costZones.add(new CostZone(new Point(2, 2), new Point(3, 3), 5));
        Set<Point> obstacles = new HashSet<>();
        obstacles.add(new Point(4, 4));

        return new Params(10, 10, start, end, costZones, obstacles,
                          100.0, 10, 50, 2.0, 1.0, 1.0, 1.0);
    }

    public static Params randomParams(String[] args) {
        int n = 10, m = 10;
        Point start = new Point(1, 1);
        Point end = new Point(n, m);
        List<CostZone> costZones = new ArrayList<>();
        costZones.add(new CostZone(new Point(2, 2), new Point(3, 3), 3));
        Set<Point> obstacles = new HashSet<>();
        obstacles.add(new Point(4, 4));

        return new Params(n, m, start, end, costZones, obstacles,
                          100.0, 10, 50, 2.0, 1.0, 1.0, 1.0);
    }
}
