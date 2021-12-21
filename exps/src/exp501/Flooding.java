package exp501;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Flooding {
    public int n;
    public int m;
    public ArrayList<Router> routers;
    public Graph graph;
    public int time = 0;
    int now = 0;

    public ArrayList<Integer> floodingK(int k, Consumer<Router> start, Consumer<Router> timeout,
                                        BiConsumer<Router, Packet> feedback) {
        graph.floyd();
        ArrayList<Integer> cntArray = new ArrayList<>();
        if (start != null) start.accept(routers.get(0));
        while (now != 0) {
            int cnt = 0;
            for (Router r : routers) {
                if (!r.available(time)) continue;
                Iterator<Packet> iterator = r.packets.iterator();
                while (iterator.hasNext()) {
                    Packet p = iterator.next();
                    iterator.remove();
                    if (p.ttl <= 0) {
                        now--;
                        continue;
                    }
                    if (p.time > time) break;
                    if (r.id == p.to.id) {
                        now--;
                        if (feedback != null) feedback.accept(routers.get(n - 1), p);
                        break;
                    }
                    cnt++;
                    now--;
                    int counter = k;
                    for (Edge edge : graph.getPointDistance(r.id, p.to.id)) {
                        if (p.last.equals(edge.to)) continue;
                        if (edge.to.packets.size() < Router.BUFF_SIZE) {
                            edge.to.packets.add(new Packet(p, time + graph.getDistance(r.id, edge.to.id), r));
                            now++;
                        }
                        if (--counter == 0) break;
                    }
                    break;
                }
            }
            time++;
            cntArray.add(cnt);
            if (timeout != null) {
                timeout.accept(routers.get(0));
            }
        }

        return cntArray;
    }

    public ArrayList<Integer> flooding1(Consumer<Router> start, Consumer<Router> timeout,
                                        BiConsumer<Router, Packet> feedback) {
        return floodingK(1, start, timeout, feedback);
    }

    public ArrayList<Integer> floodingAll(Consumer<Router> start, Consumer<Router> timeout,
                                          BiConsumer<Router, Packet> feedback) {
        return floodingK(-1, start, timeout, feedback);
    }

    public void sendPacket(int from, int to, int ttl, int data) {
        now++;
        Router a = routers.get(from), b = routers.get(to);
        a.packets.add(new Packet(a, b, data, a, ttl, time));
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
        Consumer<Router> start = (x) -> sendPacket(0, n - 1, 10, 0);
        ArrayList<Integer> cnt = floodingK(k, start, null, null);
        int sum = 0;
        for (int x : cnt) sum += x;
        System.out.println(graph.getDistance(0, n - 1));
        System.out.println(cnt);
        System.out.println((double) sum / graph.getDistance(0, n - 1));
    }
}
