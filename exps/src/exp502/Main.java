package exp502;

import exp501.Edge;
import exp501.Flooding;
import exp501.Graph;
import exp501.Router;

import java.util.ArrayList;
import java.util.Scanner;
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
        Consumer<Router> start = (x) -> flooding.sendPacket(0, flooding.n - 1, 2);
        Consumer<Router> timeout = (x) -> flooding.sendPacket(0, flooding.n - 1, 5);
        Consumer<Router> feedback = (x) -> flooding.sendPacket(flooding.n - 1, 0, 5);
        int cnt = flooding.flooding1(start, 6, timeout, feedback);
        System.out.println(flooding.graph.getDistance(0, flooding.n - 1));
        System.out.println(cnt);
        System.out.println((double) cnt / flooding.graph.getDistance(0, flooding.n - 1));
    }
}
