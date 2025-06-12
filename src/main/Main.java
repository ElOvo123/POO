package main;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import model.*;
import simulation.Simulation;
import util.ParamsParser;

public class Main {
    public static void main(String[] args) {
        try {
            Params params;
            if (args.length >= 2 && "-f".equalsIgnoreCase(args[0])) {
                // Print input file contents as required
                List<String> lines = Files.readAllLines(Paths.get(args[1]));
                for (String line : lines) {
                    System.out.println(line);
                }
                System.out.println();
                System.out.println();
                params = ParamsParser.fromFile(args[1]);
            } else if (args.length == 16 && "-r".equalsIgnoreCase(args[0])) {
                // Skip the -r flag and pass the rest of the arguments
                String[] randomArgs = new String[args.length - 1];
                System.arraycopy(args, 1, randomArgs, 0, args.length - 1);
                params = ParamsParser.randomParams(randomArgs);
                // Print parameters in input.txt format
                printParamsAsInputFormat(params);
                System.out.println();
                System.out.println();
            } else {
                throw new IllegalArgumentException("Invalid mode. Use -f for file or -r for random.");
            }

            Simulation simulation = new Simulation(params);
            simulation.run();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void printParamsAsInputFormat(Params params) {
        // Print first line
        System.out.printf("%d %d %d %d %d %d %d %d %d %d %d %d %d %d %d %d\n",
            params.getN(), params.getM(),
            params.getStart().getX(), params.getStart().getY(),
            params.getEnd().getX(), params.getEnd().getY(),
            params.getZones().size(), params.getObstacles().size(),
            params.getTmax(), params.getPopsize(), params.getMaxpop(),
            (int)params.getComfort(), // k
            (int)params.getDeathrate(), // mu
            (int)params.getMoverate(), // delta
            (int)params.getReprate() // rho
        );
        if (!params.getZones().isEmpty()) {
            System.out.println("special cost zones:");
            for (CostZone z : params.getZones()) {
                System.out.printf("%d %d %d %d\n", z.getTopLeft().getX(), z.getTopLeft().getY(), z.getBottomRight().getX(), z.getBottomRight().getY());
            }
            for (CostZone z : params.getZones()) {
                System.out.printf("%.2f\n", z.getCost());
            }
        }
        if (!params.getObstacles().isEmpty()) {
            System.out.println("obstacles:");
            for (Point p : params.getObstacles()) {
                System.out.printf("%d %d\n", p.getX(), p.getY());
            }
        }
    }
}