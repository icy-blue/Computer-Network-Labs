package exp601;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class Server {
    String name;
    int port;
    ServerSocket server;
    ArrayList<ServerTCPHandler> handlers = new ArrayList<>();

    public Server(String name, int port) {
        this.name = name;
        this.port = port;
        try {
            this.server = new ServerSocket(port);
            Thread listener = new Thread(() -> {
                while (true) {
                    Socket socket = null;
                    try {
                        socket = server.accept();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    handlers.add(new ServerTCPHandler(this, socket));
                }
            });
            listener.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void broadcast(ServerTCPHandler handler, Message message) {
        Iterator<ServerTCPHandler> it = handlers.iterator();
        try {
            while (it.hasNext()) {
                ServerTCPHandler i = it.next();
                if (i == null || i.socket.isClosed()) {
                    it.remove();
                    continue;
                }
                if (handler.equals(i)) continue;
                i.writer.writeObject(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
