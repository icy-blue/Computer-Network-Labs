package exp401;

public class PC implements Comparable<PC> {
    boolean sendable, sent;
    int waiting; // calculated from start
    int id;

    @Override
    public int compareTo(PC o) {
        if (this.sent == o.sent) return this.waiting - o.waiting > 0 ? 1 : -1;
        return this.sent ? 1 : -1;
    }

    PC(int id) {
        this.sendable = false;
        this.sent = false;
        this.waiting = 0;
        this.id = id;
    }
}
