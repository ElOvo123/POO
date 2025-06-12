package model;

// CostZone.java - Represents a special cost area
public class CostZone {
    private Point topLeft;
    private Point bottomRight;
    private double cost;
    
    public CostZone(Point topLeft, Point bottomRight, double cost) {
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
        this.cost = cost;
    }
    
    public boolean containsEdge(Point p1, Point p2) { 
        if (!isWithinZone(p1) || !isWithinZone(p2)) 
        {
            return false;
        }
        
        // Then check if they form a border edge
        return (p1.x == topLeft.x || p1.x == bottomRight.x || p1.y == topLeft.y || p1.y == bottomRight.y) &&
               (p2.x == topLeft.x || p2.x == bottomRight.x || p2.y == topLeft.y || p2.y == bottomRight.y) &&
               // And they are adjacent
               (Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y) == 1);
    }

    private boolean isWithinZone(Point p) {
        return p.x >= topLeft.x && p.x <= bottomRight.x && p.y >= topLeft.y && p.y <= bottomRight.y;
    }

    public Point getTopLeft() {
        return topLeft;
    }

    public Point getBottomRight() {
        return bottomRight;
    }

    public double getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return "CostZone{" +
                "topLeft=" + topLeft +
                ", bottomRight=" + bottomRight +
                ", cost=" + cost +
                '}';
    }
}