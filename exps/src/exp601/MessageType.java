package exp601;

public enum MessageType {
    Creat, Join, Quit, Send, Created, Joined, Quited, Sent, Error, Existed;

    static MessageType getTypeByNumber(int id) throws TypeUnknownException {
        for (MessageType type : MessageType.values()) {
            if (type.ordinal() == id) return type;
        }
        throw new TypeUnknownException(Integer.toString(id));
    }

    static MessageType getTypeByName(String str) throws TypeUnknownException {
        for (MessageType type : MessageType.values()) {
            if (type.toString().equalsIgnoreCase(str.strip())) return type;
        }
        throw new TypeUnknownException(str);
    }
}