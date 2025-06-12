import java.util.List;
import java.util.Set;

public class Params {
    public final int n;  // grid width
    public final int m;  // grid height
    public final Point start;  // start point
    public final Point end;  // end point
    public final List<CostZone> zones;  // cost zones
    public final List<Point> obstacles;  // obstacles
    public final int cmax;  // maximum cost
    public final int tmax;  // maximum time
    public final int popsize;  // initial population size
    public final int maxpop;  // maximum population size
    public final double deathrate;  // death rate
    public final double reprate;  // reproduction rate
    public final double mutrate;  // mutation rate
    public final double moverate;  // move rate
    public final double comfort;  // comfort threshold

    public Params(int n, int m, Point start, Point end, List<CostZone> zones, List<Point> obstacles,
                 int cmax, int tmax, int popsize, int maxpop, double deathrate, double reprate,
                 double mutrate, double moverate, double comfort) {
        this.n = n;
        this.m = m;
        this.start = start;
        this.end = end;
        this.zones = zones;
        this.obstacles = obstacles;
        this.cmax = cmax;
        this.tmax = tmax;
        this.popsize = popsize;
        this.maxpop = maxpop;
        this.deathrate = deathrate;
        this.reprate = reprate;
        this.mutrate = mutrate;
        this.moverate = moverate;
        this.comfort = comfort;
    }
}
