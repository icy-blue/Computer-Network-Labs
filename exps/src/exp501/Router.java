package exp501;

import java.util.ArrayList;

public class Router {
    ArrayList<Edge> edges = new ArrayList<>();
    ArrayList<Packet> packets = new ArrayList<>();
    int id;

    Router(int id) {
        this.id = id;
    }

    boolean available(int time) {
        if (packets.size() == 0) return false;
        packets.sort(Packet::compareTo);
        return packets.get(0).time <= time;
    }
}
