import java.util.List;
import java.util.ArrayList;
// Individual.java - Represents a potential solution
public class Individual implements Comparable<Individual> {
    private final List<Point> path;
    private double comfort;
    private final double deathTime;
    
    public Individual(Point start, double deathTime) {
        this.path = new ArrayList<>();
        this.path.add(start);
        this.deathTime = deathTime;
    }
    
    public void calculateComfort(Params params, Grid grid) {
        double cost = calculatePathCost(grid);
        double length = path.size() - 1; // Number of edges
        double dist = grid.calculateDistance(getCurrentPosition(), params.end);
        
        double cmax = getMaxEdgeCost(params.costZones);
        
        double term1 = 1 - (cost - length + 2) / ((cmax - 1) * length + 3);
        double term2 = 1 - dist / (params.n + params.m + 1);
        
        this.comfort = Math.pow(term1, params.k) * Math.pow(term2, params.k);
    }
    
    private double calculatePathCost(Grid grid) {
        double totalCost = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            totalCost += grid.getEdgeCost(path.get(i), path.get(i + 1));
        }
        return totalCost;
    }
    // Add these methods to Individual class
public Point getCurrentPosition() {
    return path.get(path.size() - 1);
}

public double getComfort() {
    return comfort;
}

public List<Point> getPath() {
    return new ArrayList<>(path); // Return a copy
}

public double getCost(Grid grid) {
    double total = 0;
    for (int i = 0; i < path.size() - 1; i++) {
        total += grid.getEdgeCost(path.get(i), path.get(i + 1));
    }
    return total;
}

public double getDeathTime() {
    return deathTime;
}

@Override
public int compareTo(Individual other) {
    return Double.compare(this.comfort, other.comfort);
}
    public void move(Point newPos) {
    path.add(newPos);
    // Recalculate comfort after moving
    // Note: You'll need to pass params and grid to this method
}

public Individual reproduce() {
    Individual child = new Individual(this.getCurrentPosition(), -Math.log(1 - Math.random()) * /* need mu from params */);
    // Copy some characteristics from parent if needed
    return child;
}
}