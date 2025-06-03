import java.util.List;
import java.util.ArrayList;
import java.util.Set;
// Grid.java - Manages the game board
public class Grid {
    private final int width, height;
    private final Set<Point> obstacles;
    private final List<CostZone> costZones;
    
    public Grid(int width, int height, Set<Point> obstacles, List<CostZone> costZones) { this.width = width;
    this.height = height;
    this.obstacles = obstacles;
    this.costZones = costZones; }
    
    public int getEdgeCost(Point p1, Point p2) {
        // Default cost is 1
        // Check if edge is in any cost zone and return max cost if overlapping
    int maxCost = 1;
    
    // Check all cost zones to see if this edge is in any of them
    for (CostZone zone : costZones) {
        if (zone.containsEdge(p1, p2)) {
            // If edge is in a cost zone, track the maximum cost
            maxCost = Math.max(maxCost, zone.getCost());
        }
    }
    
    return maxCost;
    }
    
    public boolean isValidMove(Point from, Point to) {
        // Check if move is within grid bounds
    if (to.x < 1 || to.x > width || to.y < 1 || to.y > height) {
        return false;
    }
    
    // Check if target is an obstacle
    if (obstacles.contains(to)) {
        return false;
    }
    
    // Check if move is adjacent (N,S,E,W - no diagonals)
    int dx = Math.abs(from.x - to.x);
    int dy = Math.abs(from.y - to.y);
    return (dx == 1 && dy == 0) || (dx == 0 && dy == 1);
    }
    
    public List<Point> getPossibleMoves(Point current) {
        // Returns valid adjacent points (N, E, S, W)
        List<Point> possibleMoves = new ArrayList<>();
    
    // Check all four directions
    Point[] directions = {
        new Point(current.x, current.y + 1),    // North
        new Point(current.x + 1, current.y),    // East
        new Point(current.x, current.y - 1),    // South
        new Point(current.x - 1, current.y)     // West
    };
    
    for (Point neighbor : directions) {
        if (isValidMove(current, neighbor)) {
            possibleMoves.add(neighbor);
        }
    }
    
    return possibleMoves;
    }
    
    public int calculateDistance(Point p1, Point p2) {
        // Manhattan distance ignoring obstacles
        return Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y);
    }
}