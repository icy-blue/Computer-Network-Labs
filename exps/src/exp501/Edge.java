package exp501;

public class Edge {
    Router from, to;
    int delay;

    Edge(Router from, Router to, int delay) {
        this.from = from;
        this.to = to;
        this.delay = delay;
    }
}
