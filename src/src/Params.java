import java.util.List;
import java.util.Set;

public class Params {
    public final int n, m;
    public final Point start, end;
    public final List<CostZone> costZones;
    public final Set<Point> obstacles;
    public final int p1, p2, p3, p4, p5, p6, p7;
    public final int initialPop, maxPop;
    public final double k, mu, delta, rho, finalTime;

    public Params(int n, int m, ParamsParser.Point start, ParamsParser.Point end,
                  List<ParamsParser.CostZone> costZones, Set<ParamsParser.Point> obstacles,
                  int p1, int p2, int p3, int p4, int p5, int p6, int p7,
                  int initialPop, int maxPop, double k, double mu, double delta, 
                  double rho, double finalTime) {
        this.n = n;
        this.m = m;
        this.start = new Point(start.x, start.y);
        this.end = new Point(end.x, end.y);
        this.costZones = costZones.stream()
            .map(cz -> new CostZone(
                new Point(cz.topLeft.x, cz.topLeft.y),
                new Point(cz.bottomRight.x, cz.bottomRight.y),
                cz.cost))
            .toList();
        this.obstacles = obstacles.stream()
            .map(p -> new Point(p.x, p.y))
            .collect(java.util.stream.Collectors.toSet());
        this.p1 = p1; this.p2 = p2; this.p3 = p3;
        this.p4 = p4; this.p5 = p5; this.p6 = p6;
        this.p7 = p7;
        this.initialPop = initialPop;
        this.maxPop = maxPop;
        this.k = k;
        this.mu = mu;
        this.delta = delta;
        this.rho = rho;
        this.finalTime = finalTime;
    }
}