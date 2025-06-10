import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ParamsParser {
    public static Params fromFile(String filename) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filename));
        List<Integer> numbers = new ArrayList<>();
        
        while (scanner.hasNextInt()) {
            numbers.add(scanner.nextInt());
        }
        
        if (numbers.size() < 6) {
            throw new IllegalArgumentException("File must contain at least 6 integers");
        }
        
        int n = numbers.get(0);
        int m = numbers.get(1);
        Point start = new Point(numbers.get(2), numbers.get(3));
        Point end = new Point(numbers.get(4), numbers.get(5));
        
        List<CostZone> zones = new ArrayList<>();
        List<Point> obstacles = new ArrayList<>();
        
        int i = 6;
        while (i < numbers.size()) {
            if (numbers.get(i) == 0) {
                // Cost zone
                if (i + 4 >= numbers.size()) {
                    throw new IllegalArgumentException("Invalid cost zone format");
                }
                Point topLeft = new Point(numbers.get(i + 1), numbers.get(i + 2));
                Point bottomRight = new Point(numbers.get(i + 3), numbers.get(i + 4));
                double cost = numbers.get(i + 5);
                zones.add(new CostZone(topLeft, bottomRight, cost));
                i += 6;
            } else if (numbers.get(i) == 1) {
                // Obstacle
                if (i + 2 >= numbers.size()) {
                    throw new IllegalArgumentException("Invalid obstacle format");
                }
                Point obstacle = new Point(numbers.get(i + 1), numbers.get(i + 2));
                obstacles.add(obstacle);
                i += 3;
            } else {
                throw new IllegalArgumentException("Invalid type: " + numbers.get(i));
            }
        }
        
        return new Params(n, m, start, end, zones, obstacles,
                         100, 1000, 100, 200, 0.1, 0.2, 0.1, 0.3, 0.5);
    }
    
    public static Params randomParams(String[] args) {
        if (args.length != 15) {
            throw new IllegalArgumentException("Random mode requires 15 arguments");
        }
        
        int n = Integer.parseInt(args[0]);
        int m = Integer.parseInt(args[1]);
        Point start = new Point(Integer.parseInt(args[2]), Integer.parseInt(args[3]));
        Point end = new Point(Integer.parseInt(args[4]), Integer.parseInt(args[5]));
        int numZones = Integer.parseInt(args[6]);
        int numObstacles = Integer.parseInt(args[7]);
        int cmax = Integer.parseInt(args[8]);
        int tmax = Integer.parseInt(args[9]);
        int popsize = Integer.parseInt(args[10]);
        int maxpop = Integer.parseInt(args[11]);
        double deathrate = Double.parseDouble(args[12]);
        double reprate = Double.parseDouble(args[13]);
        double mutrate = Double.parseDouble(args[14]);
        double moverate = 0.3; // Default value
        double comfort = 0.5;  // Default value
        
        List<CostZone> zones = new ArrayList<>();
        for (int i = 0; i < numZones; i++) {
            int x1 = 1 + (int) (Math.random() * n);
            int y1 = 1 + (int) (Math.random() * m);
            int x2 = x1 + (int) (Math.random() * (n - x1 + 1));
            int y2 = y1 + (int) (Math.random() * (m - y1 + 1));
            x2 = Math.min(x2, n);
            y2 = Math.min(y2, m);
            Point topLeft = new Point(x1, y1);
            Point bottomRight = new Point(x2, y2);
            double cost = 1 + Math.random() * (cmax - 1);
            zones.add(new CostZone(topLeft, bottomRight, cost));
        }
        
        List<Point> obstacles = new ArrayList<>();
        for (int i = 0; i < numObstacles; i++) {
            Point obstacle = new Point(1 + (int) (Math.random() * n), 1 + (int) (Math.random() * m));
            if (!obstacle.equals(start) && !obstacle.equals(end)) {
                obstacles.add(obstacle);
            }
        }
        
        return new Params(n, m, start, end, zones, obstacles,
                         cmax, tmax, popsize, maxpop, deathrate, reprate,
                         mutrate, moverate, comfort);
    }
}
