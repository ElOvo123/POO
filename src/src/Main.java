import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

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
            params.n, params.m,
            params.start.x, params.start.y,
            params.end.x, params.end.y,
            params.zones.size(), params.obstacles.size(),
            params.tmax, params.popsize, params.maxpop,
            (int)params.comfort, // k
            (int)params.deathrate, // mu
            (int)params.moverate, // delta
            (int)params.reprate // rho
        );
        if (!params.zones.isEmpty()) {
            System.out.println("special cost zones:");
            for (CostZone z : params.zones) {
                System.out.printf("%d %d %d %d\n", z.getTopLeft().x, z.getTopLeft().y, z.getBottomRight().x, z.getBottomRight().y);
            }
            for (CostZone z : params.zones) {
                System.out.printf("%.2f\n", z.getCost());
            }
        }
        if (!params.obstacles.isEmpty()) {
            System.out.println("obstacles:");
            for (Point p : params.obstacles) {
                System.out.printf("%d %d\n", p.x, p.y);
            }
        }
    }
}



