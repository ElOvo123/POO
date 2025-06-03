import java.util.List;
import java.util.Set;
// Params.java - Holds all simulation parameters
public class Params {
    public final int n, m; // Grid dimensions
    public final Point start, end;
    public final List<CostZone> costZones;
    public final Set<Point> obstacles;
    public final double finalTime;
    public final int initialPop, maxPop;
    public final double k, mu, delta, rho;
    
    // Constructors for both file and random initialization
}