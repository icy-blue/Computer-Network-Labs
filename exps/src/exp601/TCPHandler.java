package exp601;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public abstract class TCPHandler {
    Socket socket;
    ObjectInputStream reader;
    ObjectOutputStream writer;
    ArrayList<String> chatData = new ArrayList<>();
}
