package exp503;

public class Item implements Comparable<Item> {
    long ip;
    int prefix;
    int outline;

    @Override
    public String toString() {
        return ((ip >> 24) & 255) + "." + ((ip >> 16) & 255) + "." +
                ((ip >> 8) & 255) + "." + ((ip) & 255) + "/" + prefix;
    }

    public Item(String ip, String prefix, int outline) {
        this.ip = IP.getLongIP(ip);
        this.prefix = Long.bitCount(IP.getLongIP(prefix));
        this.outline = outline;
    }

    @Override
    public int compareTo(Item o) {
        if (this.prefix != o.prefix) return o.prefix - this.prefix;
        return Long.compare(this.ip, o.ip);
    }

    private long getMask(int prefix) {
        long tmp = 0;
        for (int i = 0; i < prefix; i++) {
            tmp <<= 1;
            tmp |= 1;
        }
        tmp <<= 32 - prefix;
        return tmp;
    }

    public boolean match(String ip) {
        return match(IP.getLongIP(ip));
    }

    public boolean match(long ip) {
        long mask = getMask(this.prefix);
        return (ip & mask) == this.ip;
    }
}
