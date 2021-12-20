package exp501;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class Flooding {
    static Scanner scan;
    static int n, m;
    static ArrayList<Router> routers;
    static Graph graph;

    static int floodingK(int k) {
        graph.floyd();
        int time = 0, cnt = 0, now = 1;
        routers.get(0).packets.add(new Packet(routers.get(0), routers.get(n - 1), routers.get(0), 10, 0));
        while (now != 0) {
            for (Router r : routers) {
                if (!r.available(time)) continue;
                Iterator<Packet> iterator = r.packets.iterator();
                while (iterator.hasNext()) {
                    Packet p = iterator.next();
                    if (p.ttl < 0) {
                        iterator.remove();
                        now--;
                        continue;
                    }
                    if (p.time > time) break;
                    if (r.id != n - 1) cnt++;
                    now--;
                    int counter = k;
                    for (Edge edge : graph.getPointDistance(r.id)) {
                        if (p.last.equals(edge.to)) continue;
                        edge.to.packets.add(new Packet(p, time + edge.delay, r));
                        now++;
                        if (--counter == 0) break;
                    }
                    iterator.remove();
                }
            }
            time++;
        }
        return cnt;
    }

    static int flooding1() {
        return floodingK(1);
    }

    static int floodingAll() {
        return floodingK(-1);
    }

    public static void main(String[] args) {
        scan = new Scanner(System.in);
        n = scan.nextInt();
        m = scan.nextInt();
        routers = new ArrayList<>();
        graph = new Graph(n, routers);
        int k = scan.nextInt();
        int cnt = floodingK(k);
        for (int i = 0; i < n; i++) {
            routers.add(new Router(i));
        }
        for (int i = 0; i < m; i++) {
            int a = scan.nextInt(), b = scan.nextInt(), delay = scan.nextInt();
            Edge edge = new Edge(routers.get(a), routers.get(b), delay);
            routers.get(a).edges.add(edge);
            graph.addEdge(edge);
        }

        System.out.println(graph.getDistance(0, n - 1));
        System.out.println(cnt);
        System.out.println((double) cnt / graph.getDistance(0, n - 1));
    }
}
