public class Main {
    public static void main(String[] args) {
        System.out.println("Args length: " + args.length);
        for (int i = 0; i < args.length; i++) {
            System.out.println("Arg[" + i + "]: " + args[i]);
        }
        try {
            Params params;
            if (args.length >= 2 && "-f".equalsIgnoreCase(args[0])) {
                params = ParamsParser.fromFile(args[1]);
            } else if (args.length == 16 && "-r".equalsIgnoreCase(args[0])) {
                // Skip the -r flag and pass the rest of the arguments
                String[] randomArgs = new String[args.length - 1];
                System.arraycopy(args, 1, randomArgs, 0, args.length - 1);
                params = ParamsParser.randomParams(randomArgs);
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



