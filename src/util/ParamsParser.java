package util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import model.*;

public class ParamsParser {
    public static Params fromFile(String filename) throws IOException {
        try (Scanner scanner = new Scanner(new File(filename))) {

            if (!scanner.hasNextLine())
                throw new IllegalArgumentException("Ficheiro vazio");

            String[] partes = scanner.nextLine().trim().split("\\s+");
            if (partes.length != 15)
                throw new IllegalArgumentException("Esperava 15 inteiros – recebeu " + partes.length);

            int n         = Integer.parseInt(partes[0]);
            int m         = Integer.parseInt(partes[1]);
            Point start   = new Point(Integer.parseInt(partes[2]), Integer.parseInt(partes[3]));
            Point end     = new Point(Integer.parseInt(partes[4]), Integer.parseInt(partes[5]));
            int numZones  = Integer.parseInt(partes[6]);
            int numObs    = Integer.parseInt(partes[7]);
            int tmax      = Integer.parseInt(partes[8]);
            int popsize   = Integer.parseInt(partes[9]);
            int maxpop    = Integer.parseInt(partes[10]);
            int comfort   = Integer.parseInt(partes[11]);
            int deathrate = Integer.parseInt(partes[12]);
            int reprate   = Integer.parseInt(partes[13]);
            int mutrate   = Integer.parseInt(partes[13]);
            int moverate  = Integer.parseInt(partes[14]);
  

            List<CostZone> zones  = new ArrayList<>();
            List<Point> obstacles = new ArrayList<>();

            int max_cost = 0;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;

                if (line.toLowerCase().startsWith("special cost zones")) {
                    while (scanner.hasNextLine()) {
                        String zLine = scanner.nextLine().trim();
                        if (zLine.isEmpty()) continue;
                        if (zLine.toLowerCase().startsWith("obstacles")) { line = zLine; break; }

                        String[] z = zLine.split("\\s+");
                        if (z.length != 5) continue;
                        Point tl  = new Point(Integer.parseInt(z[0]), Integer.parseInt(z[1]));
                        Point br  = new Point(Integer.parseInt(z[2]), Integer.parseInt(z[3]));
                        int cost  = Integer.parseInt(z[4]);
                        zones.add(new CostZone(tl, br, cost));
                        max_cost = Math.max(max_cost, cost);
                    }
                }

                if (line.toLowerCase().startsWith("obstacles")) {
                    while (scanner.hasNextLine()) {
                        String oLine = scanner.nextLine().trim();
                        if (oLine.isEmpty()) continue;
                        String[] o = oLine.split("\\s+");
                        if (o.length != 2) continue;
                        obstacles.add(new Point(Integer.parseInt(o[0]), Integer.parseInt(o[1])));
                    }
                }
            }

             
            return new Params(n, m, start, end,
                              zones, obstacles,
                              max_cost, tmax, popsize, maxpop, deathrate, reprate, mutrate, moverate, comfort);
        }
    }

    
    public static Params randomParams(String[] args) {
        if (args.length != 15) {
            throw new IllegalArgumentException("Random mode requires 15 arguments");
        }
        
        int n = Integer.parseInt(args[0]);
        int m = Integer.parseInt(args[1]);
        Point start = new Point(Integer.parseInt(args[2]), Integer.parseInt(args[3]));
        Point end = new Point(Integer.parseInt(args[4]), Integer.parseInt(args[5]));
        int numZones = Integer.parseInt(args[6]);
        int numObstacles = Integer.parseInt(args[7]);
        int cmax = Integer.parseInt(args[7]);
        int tmax = Integer.parseInt(args[8]);
        int popsize = Integer.parseInt(args[9]);
        int maxpop = Integer.parseInt(args[10]);
        double comfort = Double.parseDouble(args[11]);
        double deathrate = Double.parseDouble(args[12]);
        double reprate = Double.parseDouble(args[13]);
        double mutrate = Double.parseDouble(args[13]);
        double moverate = Double.parseDouble(args[14]);
        

        List<CostZone> zones = new ArrayList<>();
        for (int i = 0; i < numZones; i++) {
            int x1 = 1 + (int) (Math.random() * n);
            int y1 = 1 + (int) (Math.random() * m);
            int x2 = x1 + (int) (Math.random() * (n - x1 + 1));
            int y2 = y1 + (int) (Math.random() * (m - y1 + 1));
            x2 = Math.min(x2, n);
            y2 = Math.min(y2, m);
            Point topLeft = new Point(x1, y1);
            Point bottomRight = new Point(x2, y2);
            double cost = 1 + Math.random() * (cmax - 1);
            zones.add(new CostZone(topLeft, bottomRight, cost));
        }
        
        List<Point> obstacles = new ArrayList<>();
        for (int i = 0; i < numObstacles; i++) {
            Point obstacle = new Point(1 + (int) (Math.random() * n), 1 + (int) (Math.random() * m));
            if (!obstacle.equals(start) && !obstacle.equals(end)) {
                obstacles.add(obstacle);
            }
        }
        
        return new Params(n, m, start, end, zones, obstacles,
                         cmax, tmax, popsize, maxpop, deathrate, reprate,
                         mutrate, moverate, comfort);
    }
}