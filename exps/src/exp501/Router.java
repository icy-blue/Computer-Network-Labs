package exp501;

import java.util.ArrayList;

public class Router {
    public ArrayList<Edge> edges = new ArrayList<>();
    public ArrayList<Packet> packets = new ArrayList<>();
    int id;
    public static int BUFF_SIZE = 11;

    public Router(int id) {
        this.id = id;
    }

    boolean available(int time) {
        if (packets.size() == 0) return false;
        packets.sort(Packet::compareTo);
        return packets.get(0).time <= time;
    }

    boolean isFull(int time) {
        int cnt = 0;
        for (Packet packet : packets) {
            if (packet.time == time) cnt++;
            if (cnt > BUFF_SIZE) return true;
        }
        return false;
    }
}
