


public class Main {
    public static void main(String[] args) {
        try {
            Params params;
            if (args.length == 2 && "-f".equalsIgnoreCase(args[0])) {
                params = ParamsParser.fromFile(args[1]);
            } else if (args[0].equals("-r")) {
                params = ParamsParser.randomParams(args);
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
}



