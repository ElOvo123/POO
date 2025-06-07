import java.util.List;
import java.util.Set;

public class Params {
    public final int n, m;
    public final ParamsParser.Point start, end;
    public final List<ParamsParser.CostZone> costZones;
    public final Set<ParamsParser.Point> obstacles;
    public final int p1, p2, p3, p4, p5, p6, p7;

    public Params(int n, int m, ParamsParser.Point start, ParamsParser.Point end,
                  List<ParamsParser.CostZone> costZones, Set<ParamsParser.Point> obstacles,
                  int p1, int p2, int p3, int p4, int p5, int p6, int p7) {
        this.n = n;
        this.m = m;
        this.start = start;
        this.end = end;
        this.costZones = costZones;
        this.obstacles = obstacles;
        this.p1 = p1; this.p2 = p2; this.p3 = p3;
        this.p4 = p4; this.p5 = p5; this.p6 = p6;
        this.p7 = p7;
    }
}