package exp501;

import java.util.ArrayList;
import java.util.Comparator;

public class Graph {
    private int n;
    private int[][] map, next;
    private ArrayList<Edge> edges = new ArrayList<>();
    private ArrayList<Router> routers;
    static final int inf = (int) 1e6;

    public Graph(int n, ArrayList<Router> routers) {
        this.n = n;
        this.routers = routers;
        this.map = new int[n][n];
        this.next = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                map[i][j] = i == j ? 0 : inf;
                next[i][j] = i == j ? i : -1;
            }
        }
    }

    public void addEdge(Edge edge) {
        if (edge == null) return;
        int from = edge.from.id, to = edge.to.id;
        if (from < 0 || from >= n || to < 0 || to >= n) return;
        this.edges.add(edge);
        map[from][to] = edge.delay;
        next[from][to] = to;
    }

    void floyd() {
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (j == i) continue;
                    if (map[i][k] + map[k][j] < map[i][j]) {
                        map[i][j] = map[i][k] + map[k][j];
                        next[i][j] = next[i][k];
                    }
                }
            }
        }
    }

    int getDistance(Router a, Router b) {
        if (a == null || b == null) return -1;
        return map[a.id][b.id];
    }

    public int getDistance(int a, int b) {
        return map[a][b];
    }

    ArrayList<Edge> getPointDistance(int p, int q) {
        ArrayList<Edge> edges = new ArrayList<>();
        for (Edge e : routers.get(p).edges) {
            if (map[e.to.id][q] >= inf) continue;
            edges.add(new Edge(routers.get(p), e.to, e.delay + map[e.to.id][q]));
        }
        edges.sort(Comparator.comparingInt((Edge a) -> a.delay));
        return edges;
    }
}
