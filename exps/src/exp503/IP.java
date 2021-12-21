package exp503;

public class IP {
    public static long getIp(String ip) {
        long tmp = 0;
        String[] data = ip.split("\\.");
        assert data.length == 4;
        for (int i = 0; i < 4; i++) {
            int x = Integer.parseInt(data[i]);
            tmp <<= 8;
            tmp += x;
        }
        return tmp;
    }
}
