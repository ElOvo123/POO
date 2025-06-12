package model;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Collections;
import java.util.Arrays;

public class Grid {
    private final int width, height;
    private final Set<Point> obstacles;
    private final List<CostZone> costZones;
    
    public Grid(int width, int height, Set<Point> obstacles, List<CostZone> costZones) 
    { 
        this.width = width;
        this.height = height;
        this.obstacles = obstacles;
        this.costZones = costZones; 
    }
    
    public int getEdgeCost(Point p1, Point p2) {
        int maxCost = 1;
        
        for (CostZone zone : costZones) {
            if (zone.containsEdge(p1, p2)) {
                maxCost = Math.max(maxCost, (int)Math.ceil(zone.getCost()));
            }
        }
        
        return maxCost;
    }
    
    public boolean isValidMove(Point from, Point to) {
        // Check if within grid bounds
        if (to.getX() < 1 || to.getX() > width || to.getY() < 1 || to.getY() > height) {
            return false;
        }
        
        // Check if target is an obstacle
        if (obstacles.contains(to)) {
            return false;
        }
        
        // Check if move is adjacent
        int dx = Math.abs(from.getX() - to.getX());
        int dy = Math.abs(from.getY() - to.getY());
        return (dx == 1 && dy == 0) || (dx == 0 && dy == 1);
    }
    
    public List<Point> getPossibleMoves(Point current) {
   
        List<Point> possibleMoves = new ArrayList<>();
    
        // Check all four directions
        List<Point> directions = new ArrayList<>(Arrays.asList(
            new Point(current.getX(), current.getY() + 1),    // North
            new Point(current.getX() + 1, current.getY()),    // East
            new Point(current.getX(), current.getY() - 1),    // South
            new Point(current.getX() - 1, current.getY())     // West
        ));
        Collections.shuffle(directions);  // Randomize the order

        for (Point neighbor : directions) {
            if (isValidMove(current, neighbor)) {
                possibleMoves.add(neighbor);
            }
        }

        return possibleMoves;
    }
    
    public int calculateDistance(Point p1, Point p2) {
        return Math.abs(p1.getX() - p2.getX()) + Math.abs(p1.getY() - p2.getY());
    }
}