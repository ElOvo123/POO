package model;

import java.util.Collections;
import java.util.List;

public final class Params {
    private final int n, m;
    private final Point start, end;
    private final List<CostZone> zones;
    private final List<Point> obstacles;
    private final int cmax, tmax, popsize, maxpop;
    private final double deathrate, reprate, mutrate, moverate, comfort;

    public Params(int n, int m, Point start, Point end, List<CostZone> zones, List<Point> obstacles,
                  int cmax, int tmax, int popsize, int maxpop, double deathrate, double reprate,
                  double mutrate, double moverate, double comfort) {
        this.n = n;
        this.m = m;
        this.start = start;
        this.end = end;
        this.zones = Collections.unmodifiableList(zones);
        this.obstacles = Collections.unmodifiableList(obstacles);
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

    public int getN() { return n; }
    public int getM() { return m; }
    public Point getStart() { return start; }
    public Point getEnd() { return end; }
    public List<CostZone> getZones() { return zones; }
    public List<Point> getObstacles() { return obstacles; }
    public int getCmax() { return cmax; }
    public int getTmax() { return tmax; }
    public int getPopsize() { return popsize; }
    public int getMaxpop() { return maxpop; }
    public double getDeathrate() { return deathrate; }
    public double getReprate() { return reprate; }
    public double getMutrate() { return mutrate; }
    public double getMoverate() { return moverate; }
    public double getComfort() { return comfort; }
}