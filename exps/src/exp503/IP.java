package exp503;

public class IP {
    public static long getLongIP(String ip) {
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

    public static String getStringIP(long ip) {
        return ((ip >> 24) & 255) + "." + ((ip >> 16) & 255) + "." + ((ip >> 8) & 255) + "." + (ip & 255);
    }

    public static byte[] getBytesIP(String ip) {
        byte[] ans = new byte[4];
        String[] strs = ip.split("\\.");
        for (int i = 0; i < 4; i++) {
            ans[i] = (byte) (Integer.parseInt(strs[i]) & 0xff);
        }
        return ans;
    }

    public static byte[] getBytesIP(long ip) {
        return getBytesIP(getStringIP(ip));
    }
}
