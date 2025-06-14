package model;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import simulation.Simulation;

// Individual.java - Represents a potential solution
public class Individual implements Comparable<Individual> {
    private final List<Point> path;
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
        return Collections.unmodifiableList(path);
    }

    public double getDeathTime() {
        return deathTime;
    }

    @Override
    public int compareTo(Individual other) {
        return Double.compare(this.comfort, other.comfort);
    }
    
    public void move(Point newPosition) {
        // If this is the first move and we're at the start position
        if (path.size() == 1 && path.get(0).equals(newPosition)) {
            return; // Don't add duplicate start position
        }
        
        // Don't add if it's the same as the last position
        if (path.get(path.size() - 1).equals(newPosition)) {
            return;
        }
        
        // Don't add if the position is already in the path
        if (path.contains(newPosition)) {
            return;
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

    public double getPathCost() {
        return calculatePathCost();
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
        return Math.abs(current.getX() - end.getX()) + Math.abs(current.getY() - end.getY());
    }

    @Override
    public String toString() {
        return "Individual{" +
                "path=" + path +
                ", comfort=" + comfort +
                '}';
    }
}