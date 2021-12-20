package exp501;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.function.Consumer;

public class Flooding {
    public int n;
    public int m;
    public ArrayList<Router> routers;
    public Graph graph;
    static int time = 0;
    static int now = 0;

    public int floodingK(int k, Consumer<Router> start, int timeUp, Consumer<Router> timeout, Consumer<Router> feedback) {
        graph.floyd();
        int cnt = 0, tries = 3;
        if (start != null) start.accept(routers.get(0));
        boolean feedback_executed = false;
        while (now != 0) {
            for (Router r : routers) {
                if (!r.available(time)) continue;
                Iterator<Packet> iterator = r.packets.iterator();
                while (iterator.hasNext()) {
                    Packet p = iterator.next();
                    if (p.ttl <= 0) {
                        iterator.remove();
                        now--;
                        continue;
                    }
                    if (p.time > time) break;
                    if (r.id == p.to.id) {
                        now--;
                        iterator.remove();
                        if (feedback != null && !feedback_executed) feedback.accept(routers.get(n - 1));
                        feedback_executed = true;
                        break;
                    }
                    cnt++;
                    now--;
                    int counter = k;
                    for (Edge edge : graph.getPointDistance(r.id, p.to.id)) {
                        if (p.last.equals(edge.to)) continue;
                        edge.to.packets.add(new Packet(p, time + graph.getDistance(r.id, edge.to.id), r));
                        now++;
                        if (--counter == 0) break;
                    }
                    iterator.remove();
                    break;
                }
            }
            time++;
            if ((time > timeUp || now == 0) && !feedback_executed && timeout != null && tries-- > 0) {
                timeUp += time;
                timeout.accept(routers.get(0));
            }
        }

        return cnt;
    }

    public int flooding1(Consumer<Router> start, int timeUp, Consumer<Router> timeout, Consumer<Router> feedback) {
        return floodingK(1, start, timeUp, timeout, feedback);
    }

    public int floodingAll(Consumer<Router> start, int timeUp, Consumer<Router> timeout, Consumer<Router> feedback) {
        return floodingK(-1, start, timeUp, timeout, feedback);
    }

    public void sendPacket(int from, int to, int ttl) {
        now++;
        Router a = routers.get(from), b = routers.get(to);
        a.packets.add(new Packet(a, b, a, ttl, time));
    }

    public static void main(String[] args) {
        new Flooding().run();
    }

    void run() {
        Scanner scan = new Scanner(System.in);
        n = scan.nextInt();
        m = scan.nextInt();
        routers = new ArrayList<>();
        graph = new Graph(n, routers);
        int k = scan.nextInt();
        for (int i = 0; i < n; i++) {
            routers.add(new Router(i));
        }
        for (int i = 0; i < m; i++) {
            int a = scan.nextInt(), b = scan.nextInt(), delay = scan.nextInt();
            Edge edge = new Edge(routers.get(a), routers.get(b), delay);
            routers.get(a).edges.add(edge);
            graph.addEdge(edge);
        }
        Consumer<Router> start = (x) -> sendPacket(0, n - 1, 100);
        int cnt = floodingK(k, start, 100, null, null);
        System.out.println(graph.getDistance(0, n - 1));
        System.out.println(cnt);
        System.out.println((double) cnt / graph.getDistance(0, n - 1));
    }
}
