package exp601;

import exp503.IP;

import java.io.Serializable;
import java.util.Date;

public class ChatData implements Serializable {
    public String data, name;
    public long time, ip;

    public ChatData(String data, String name, long time, long ip) {
        this.data = data;
        this.name = name;
        this.time = time;
        this.ip = ip;
    }

    public ChatData(String data, String name, long ip) {
        this.data = data;
        this.name = name;
        this.time = System.currentTimeMillis();
        this.ip = ip;
    }

    @Override
    public String toString() {
        Date date = new Date();
        date.setTime(time);
        return "IP " + IP.getStringIP(ip) + " at " + date + " in chatroom " + name + " said: " + data + ".";
    }
}
