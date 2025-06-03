// CostZone.java - Represents a special cost area
public class CostZone {
    public final Point bottomLeft;
    public final Point topRight;
    public final int cost;
    
    public CostZone(Point bottomLeft, Point topRight, int cost) {
        this.bottomLeft = bottomLeft;
        this.topRight = topRight;
        this.cost = cost;
    }
    
    public boolean containsEdge(Point p1, Point p2) { 
        if (!isWithinZone(p1) || !isWithinZone(p2)) {
            return false;
        }
        
        // Then check if they form a border edge
        return (p1.x == bottomLeft.x || p1.x == topRight.x || 
                p1.y == bottomLeft.y || p1.y == topRight.y) &&
               (p2.x == bottomLeft.x || p2.x == topRight.x || 
                p2.y == bottomLeft.y || p2.y == topRight.y) &&
               // And they are adjacent
               (Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y) == 1);
    }

    private boolean isWithinZone(Point p) {
        return p.x >= bottomLeft.x && p.x <= topRight.x &&
               p.y >= bottomLeft.y && p.y <= topRight.y;
    }

    public int getCost() {
        return cost;
    }
}