package exp501;

public class SendingData {
    public int data, time, tries;

    public SendingData(int data, int time, int tries) {
        this.data = data;
        this.time = time;
        this.tries = tries;
    }

    @Override
    public String toString() {
        return "SendingData{" + "data=" + data + ", time=" + time + ", tries=" + tries + '}';
    }
}
