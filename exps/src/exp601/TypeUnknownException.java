package exp601;

public class TypeUnknownException extends Exception {
    TypeUnknownException(String data) {
        super("Type " + data + " not found.");
    }
}
