package exp601;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;

public class Message implements Serializable {
    MessageType type;
    String message;
    long time;

    public static Message decode(byte[] data) throws TypeUnknownException {
        assert data != null && data.length > 0;
        int type = data[0];
        return new Message(MessageType.getTypeByNumber(type), new String(data, 1, data.length - 1));
    }

    public static byte[] encode(Message message) {
        assert message != null;
        byte[] bytes = message.message.getBytes(StandardCharsets.UTF_8), ans = new byte[bytes.length + 1];
        ans[0] = (byte) message.type.ordinal();
        System.arraycopy(bytes, 0, ans, 1, bytes.length);
        return ans;
    }

    Message(MessageType type, String message, long time) {
        this.type = type;
        this.message = message;
        this.time = time;
    }

    Message(MessageType type, String message) {
        this.type = type;
        this.message = message;
        this.time = System.nanoTime();
    }

    public byte[] encode() {
        return encode(this);
    }
}