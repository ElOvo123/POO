package model;

// CostZone.java - Represents a special cost area
public final class CostZone {
    private final Point topLeft;
    private final Point bottomRight;
    private final double cost;
    
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
        return (p1.getX() == topLeft.getX() || p1.getX() == bottomRight.getX() || p1.getY() == topLeft.getY() || p1.getY() == bottomRight.getY()) &&
               (p2.getX() == topLeft.getX() || p2.getX() == bottomRight.getX() || p2.getY() == topLeft.getY() || p2.getY() == bottomRight.getY()) &&
               // And they are adjacent
               (Math.abs(p1.getX() - p2.getX()) + Math.abs(p1.getY() - p2.getY()) == 1);
    }

    private boolean isWithinZone(Point p) {
        return p.getX() >= topLeft.getX() && p.getX() <= bottomRight.getX() && p.getY() >= topLeft.getY() && p.getY() <= bottomRight.getY();
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