package exp502;

import exp501.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
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
        AtomicInteger received = new AtomicInteger();
        AtomicInteger first = new AtomicInteger(-1);
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
            check.add(new SendingData(i * 3 + 1, i, 5));
            check.add(new SendingData(i * 3 + 2, i, 5));
            check.add(new SendingData(i * 3 + 3, i, 5));
        }
        sum *= 3;
        Consumer<Router> start = (x) -> {
            for (SendingData data : check) {
                flooding.sendPacket(0, flooding.n - 1, data.time, 6, data.data);
            }
        };
        Consumer<Router> timeout = (x) -> {
            int num = 0;
            check.sort(Comparator.comparingInt(a -> a.time));
            for (SendingData data : check) {
                if (data.time + timeup <= flooding.time) {
                    if (data.tries <= 0) continue;
                    data.time = flooding.time;
                    flooding.sendPacket(0, flooding.n - 1, 6, data.data);
                    num++;
//                    System.out.println("timeout" + data.data);
                    data.tries--;
                } else break;
            }
            System.out.println(flooding.time + " " + num);
        };
        BiConsumer<Router, Packet> feedback = (x, p) -> {
            received.getAndIncrement();
            if (p.data < 0) {
                check.removeIf(data -> data.data == -p.data);
                if (first.get() == -1) first.set(flooding.time);
            } else {
                flooding.sendPacket(flooding.n - 1, 0, 6, -p.data);
//                System.out.println("feedback " + p.data);
            }
        };
        ArrayList<Integer> cnt = flooding.floodingK(2, start, timeout, feedback);
        System.out.println(cnt);
        System.out.println(cnt.size());
        System.out.println((double) received.get() / first.get() / sum);
        System.out.printf("%.2f", (double) check.size() / sum * 100);
        System.out.println("%");
    }
}
