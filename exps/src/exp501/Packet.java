package exp501;

public class Packet {
    int ttl, time;
    Router from, to, last;
    public int data;

    Packet(Packet rt, int time, Router last) {
        this.ttl = rt.ttl - 1;
        this.from = rt.from;
        this.to = rt.to;
        this.time = time;
        this.last = last;
        this.data = rt.data;
    }

    public Packet(Router from, Router to, int data, Router last, int ttl, int time) {
        this.ttl = ttl;
        this.from = from;
        this.to = to;
        this.time = time;
        this.last = last;
        this.data = data;
    }

    int compareTo(Packet rt) {
        return this.time - rt.time;
    }
}
