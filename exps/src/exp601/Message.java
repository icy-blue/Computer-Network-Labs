package exp601;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class Message implements Serializable {
    public MessageType type;
    private String message;
    public long time;
    private int len;

    public static Message decode(byte[] data) throws TypeUnknownException {
        assert data != null && data.length > 0;
        int type = data[0];
        ByteBuffer buffer = ByteBuffer.allocate(Integer.SIZE / Byte.SIZE);
        buffer.put(data, 1, Integer.SIZE / Byte.SIZE);
        int len = buffer.flip().getInt();
        return new Message(MessageType.getTypeByNumber(type), new String(data, 5, len));
    }

    public static byte[] encode(Message message) {
        assert message != null;
        byte[] bytes = message.message.getBytes(StandardCharsets.UTF_8), ans = new byte[bytes.length + 5];
        ans[0] = (byte) message.type.ordinal();
        byte[] lenBytes = ByteBuffer.allocate(Integer.SIZE / Byte.SIZE).putInt(message.len).array();
        System.arraycopy(lenBytes, 0, ans, 1, lenBytes.length);
        System.arraycopy(bytes, 0, ans, 5, bytes.length);
        return ans;
    }

    Message(MessageType type, String message, long time) {
        this.type = type;
        this.message = message;
        this.time = time;
        this.len = message.length();
    }

    Message(MessageType type, String message) {
        this.type = type;
        this.message = message;
        this.time = System.nanoTime();
        this.len = message.length();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        this.len = message.length();
    }

    public byte[] encode() {
        return encode(this);
    }
}