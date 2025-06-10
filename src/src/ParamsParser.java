import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class ParamsParser {

    public static Params fromFile(String filename) throws IOException {
        try (Scanner scanner = new Scanner(new File(filename))) {
            if (!scanner.hasNextLine()) {
                throw new IllegalArgumentException("Ficheiro vazio");
            }
            String linha = scanner.nextLine();
            String[] partes = linha.trim().split("\s+");
            if (partes.length != 15) {
                throw new IllegalArgumentException("Esperava 15 inteiros " + partes.length);
            }
            int idx = 0;
            int n = Integer.parseInt(partes[idx++]);
            int m = Integer.parseInt(partes[idx++]);
            Point start = new Point(Integer.parseInt(partes[idx++]), Integer.parseInt(partes[idx++]));
            Point end   = new Point(Integer.parseInt(partes[idx++]), Integer.parseInt(partes[idx++]));

            int p1 = Integer.parseInt(partes[idx++]);
            int p2 = Integer.parseInt(partes[idx++]);
            int p3 = Integer.parseInt(partes[idx++]);
            int p4 = Integer.parseInt(partes[idx++]);
            int p5 = Integer.parseInt(partes[idx++]);
            int p6 = Integer.parseInt(partes[idx++]);
            int p7 = Integer.parseInt(partes[idx]);

            List<CostZone> zones = new ArrayList<>();
            Set<Point> obstacles = new HashSet<>();

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;

                if (line.toLowerCase().startsWith("special cost zones")) {
                    while (scanner.hasNextLine()) {
                        String zoneLine = scanner.nextLine().trim();
                        if (zoneLine.isEmpty()) continue;
                        if (zoneLine.toLowerCase().startsWith("obstacles")) {
                            line = zoneLine;
                            break;
                        }
                        String[] z = zoneLine.split("\s+");
                        if (z.length != 5) continue;
                        Point tl = new Point(Integer.parseInt(z[0]), Integer.parseInt(z[1]));
                        Point br = new Point(Integer.parseInt(z[2]), Integer.parseInt(z[3]));
                        int cost = Integer.parseInt(z[4]);
                        zones.add(new CostZone(tl, br, cost));
                    }
                }

                if (line.toLowerCase().startsWith("obstacles")) {
                    while (scanner.hasNextLine()) {
                        String obsLine = scanner.nextLine().trim();
                        if (obsLine.isEmpty()) continue;
                        String[] o = obsLine.split("\s+");
                        if (o.length != 2) continue;
                        obstacles.add(new Point(Integer.parseInt(o[0]), Integer.parseInt(o[1])));
                    }
                }
            }

            return new Params(n, m, start, end, zones, obstacles,
                               p1, p2, p3, p4, p5, p6, p7,
                               100, 1000, 1.0, 0.1, 0.1, 0.1, 1000.0);
        }
    }

    public static Params randomParams(String[] args) {
        if (args.length < 23) {
            throw new IllegalArgumentException("São necessários pelo menos 23 argumentos para randomParams");
        }
        int idx = 0;
        int n = Integer.parseInt(args[idx++]);
        int m = Integer.parseInt(args[idx++]);
        Point start = new Point(Integer.parseInt(args[idx++]), Integer.parseInt(args[idx++]));
        Point end   = new Point(Integer.parseInt(args[idx++]), Integer.parseInt(args[idx++]));
        int numZones = Integer.parseInt(args[idx++]);
        int numObs   = Integer.parseInt(args[idx++]);

        validarDimensoes(n, m);
        validarPonto("start", start, n, m);
        validarPonto("end",   end,   n, m);
        if (start.equals(end)) {
            throw new IllegalArgumentException("start e end não podem coincidir");
        }
        if (numZones < 0 || numObs < 0) {
            throw new IllegalArgumentException("Número de zonas/obstáculos não pode ser negativo");
        }

        List<CostZone> zones = new ArrayList<>();
        for (int i = 0; i < numZones; i++) {
            int x1 = gerarEntre(0, n - 1);
            int y1 = gerarEntre(0, m - 1);
            int x2 = gerarEntre(x1, n - 1);
            int y2 = gerarEntre(y1, m - 1);
            if (x1 == x2 && y1 == y2) { i--; continue; }
            int cost = gerarEntre(1, 10);
            zones.add(new CostZone(new Point(x1, y1), new Point(x2, y2), cost));
        }

        Set<Point> obstacles = new HashSet<>();
        while (obstacles.size() < numObs) {
            Point p = new Point(gerarEntre(0, n - 1), gerarEntre(0, m - 1));
            if (!p.equals(start) && !p.equals(end)) obstacles.add(p);
        }

        int p1 = Integer.parseInt(args[idx++]);
        int p2 = Integer.parseInt(args[idx++]);
        int p3 = Integer.parseInt(args[idx++]);
        int p4 = Integer.parseInt(args[idx++]);
        int p5 = Integer.parseInt(args[idx++]);
        int p6 = Integer.parseInt(args[idx++]);
        int p7 = Integer.parseInt(args[idx++]);
        int initialPop = Integer.parseInt(args[idx++]);
        int maxPop = Integer.parseInt(args[idx++]);
        double k = Double.parseDouble(args[idx++]);
        double mu = Double.parseDouble(args[idx++]);
        double delta = Double.parseDouble(args[idx++]);
        double rho = Double.parseDouble(args[idx++]);
        double finalTime = Double.parseDouble(args[idx]);

        return new Params(n, m, start, end, zones, obstacles,
                           p1, p2, p3, p4, p5, p6, p7,
                           initialPop, maxPop, k, mu, delta, rho, finalTime);
    }

    private static void validarDimensoes(int n, int m) {
        if (n <= 0 || m <= 0) {
            throw new IllegalArgumentException("Dimensões da grelha devem ser inteiros positivos");
        }
    }

    private static void validarPonto(String nome, Point p, int n, int m) {
        if (p.x < 0 || p.x >= n || p.y < 0 || p.y >= m) {
            throw new IllegalArgumentException(nome + " fora dos limites da grelha");
        }
    }

    public static int gerarEntre(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public static class Point {
        public final int x, y;
        public Point(int x, int y) { this.x = x; this.y = y; }
        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Point p)) return false;
            return x == p.x && y == p.y;
        }
        @Override public int hashCode() { return Objects.hash(x, y); }
        @Override public String toString() { return "(" + x + "," + y + ")"; }
    }

    public static class CostZone {
        public final Point topLeft, bottomRight;
        public final int cost;
        public CostZone(Point tl, Point br, int cost) {
            this.topLeft = tl; this.bottomRight = br; this.cost = cost;
        }
        @Override public String toString() {
            return String.format("[%s->%s : %d]", topLeft, bottomRight, cost);
        }
    }
}
