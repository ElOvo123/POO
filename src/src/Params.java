import java.util.List;
import java.util.Set;

public class Params {
    public final int n, m; // Grid dimensions
    public final Point start, end;
    public final List<CostZone> costZones;
    public final Set<Point> obstacles;
    public final double finalTime;
    public final int initialPop, maxPop;
    public final double k, mu, delta, rho;

    // Constructor
    public Params(
        int n, int m,
        Point start, Point end,
        List<CostZone> costZones,
        Set<Point> obstacles,
        double finalTime,
        int initialPop, int maxPop,
        double k, double mu, double delta, double rho
    ) {
        this.n = n;
        this.m = m;
        this.start = start;
        this.end = end;
        this.costZones = costZones;
        this.obstacles = obstacles;
        this.finalTime = finalTime;
        this.initialPop = initialPop;
        this.maxPop = maxPop;
        this.k = k;
        this.mu = mu;
        this.delta = delta;
        this.rho = rho;
    }
}
