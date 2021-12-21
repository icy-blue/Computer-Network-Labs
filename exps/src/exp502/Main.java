package exp502;

import exp501.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Scanner;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        Flooding flooding = new Flooding();
        flooding.n = scan.nextInt();
        flooding.m = scan.nextInt();
        flooding.routers = new ArrayList<>();
        flooding.graph = new Graph(flooding.n, flooding.routers);
        for (int i = 0; i < flooding.n; i++) {
            flooding.routers.add(new Router(i));
        }
        for (int i = 0; i < flooding.m; i++) {
            int a = scan.nextInt(), b = scan.nextInt(), delay = scan.nextInt();
            Edge edge = new Edge(flooding.routers.get(a), flooding.routers.get(b), delay);
            flooding.routers.get(a).edges.add(edge);
            flooding.graph.addEdge(edge);
        }
        int sum = 100, timeup = 10;
        ArrayList<SendingData> check = new ArrayList<>();
        for (int i = 0; i < sum; i++) {
            check.add(new SendingData(i, i, 3));
        }
        Consumer<Router> start = (x) -> {
            for (SendingData data : check) {
                flooding.sendPacket(0, flooding.n - 1, 6, data.data);
            }
        };
        Consumer<Router> timeout = (x) -> {
            check.sort(Comparator.comparingInt(a -> a.time));
            Iterator<SendingData> it = check.iterator();
            while (it.hasNext()) {
                SendingData data = it.next();
                if (data.time + timeup < flooding.time) {
                    if (data.tries <= 0) {
                        it.remove();
                        continue;
                    }
                    flooding.sendPacket(0, flooding.n - 1, 6, data.data);
                    data.tries--;
                } else break;
            }
        };
        BiConsumer<Router, Packet> feedback = (x, p) -> {
            if (p.data < 0) {
                check.removeIf(data -> data.data == -p.data);
            } else {
                flooding.sendPacket(flooding.n - 1, 0, 6, -p.data);
            }
        };
        ArrayList<Integer> cnt = flooding.floodingK(3, start, timeout, feedback);
        
    }
}
