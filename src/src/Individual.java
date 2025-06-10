import java.util.List;
import java.util.ArrayList;
// Individual.java - Represents a potential solution
public class Individual implements Comparable<Individual> {
    private List<Point> path;
    private double comfort;
    private double deathTime;
    private static final double K = 1.0; // comfort sensitivity parameter
    
    public Individual(Point start) {
        this.path = new ArrayList<>();
        this.path.add(start);
        this.comfort = calculateComfort();
    }

    public Individual(List<Point> path) {
        this.path = new ArrayList<>(path);
        this.comfort = calculateComfort();
    }
    
    public Point getCurrentPosition() {
        return path.get(path.size() - 1);
    }

    public double getComfort() {
        return comfort;
    }

    public List<Point> getPath() {
        return path;
    }

    public double getDeathTime() {
        return deathTime;
    }

    @Override
    public int compareTo(Individual other) {
        return Double.compare(this.comfort, other.comfort);
    }
    
    public void move(Point newPosition) {
        // Check if the new position is already in the path
        int index = path.indexOf(newPosition);
        if (index != -1) {
            // Remove the cycle
            while (path.size() > index + 1) {
                path.remove(path.size() - 1);
            }
        }
        path.add(newPosition);
        comfort = calculateComfort();
    }

    public void setDeathTime(double time) {
        this.deathTime = time;
    }

    public Individual reproduce() {
        // Calculate the length of the prefix to keep (90% + fraction of remaining 10%)
        int totalLength = path.size();
        int prefixLength = (int) Math.ceil(totalLength * 0.9 + totalLength * 0.1 * comfort);
        prefixLength = Math.min(prefixLength, totalLength);

        // Create new individual with the prefix
        List<Point> childPath = new ArrayList<>(path.subList(0, prefixLength));
        return new Individual(childPath);
    }

    private double calculateComfort() {
        Point current = getCurrentPosition();
        Point end = Simulation.getEndPoint();
        int n = Simulation.getGridWidth();
        int m = Simulation.getGridHeight();
        int cmax = Simulation.getMaxCost();

        // Calculate path cost and length
        double cost = calculatePathCost();
        int length = path.size() - 1; // number of edges

        // Calculate distance to end point
        int dist = calculateDistanceToEnd(current, end);

        // Calculate comfort using the formula
        double term1 = (1 - cost - length + 2) / ((cmax - 1) * length + 3);
        double term2 = 1 - (double) dist / (n + m + 1);

        return Math.pow(term1, K) * Math.pow(term2, K);
    }

    private double calculatePathCost() {
        double cost = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            Point p1 = path.get(i);
            Point p2 = path.get(i + 1);
            cost += Simulation.getEdgeCost(p1, p2);
        }
        return cost;
    }

    private int calculateDistanceToEnd(Point current, Point end) {
        return Math.abs(current.x - end.x) + Math.abs(current.y - end.y);
    }

    @Override
    public String toString() {
        return "Individual{" +
                "path=" + path +
                ", comfort=" + comfort +
                '}';
    }
}