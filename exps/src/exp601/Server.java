package exp601;

import exp503.IP;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Iterator;

public class Server {
    String name;
    int port;
    ServerSocket server;
    ArrayList<Socket> sockets = new ArrayList<>();

    public Server(String name, int port) {
        this.name = name;
        this.port = port;
        try {
            this.server = new ServerSocket(port);
            while (true) {
                Socket socket = server.accept();
                Thread thread = new Thread(new ServerTCPHandler(this, socket));
                thread.start();
            }
        } catch (SocketTimeoutException ignored) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void broadcast(Socket source, Message message) {
        Iterator<Socket> it = sockets.iterator();
        try {
            while (it.hasNext()) {
                Socket i = it.next();
                if (i == null || i.isClosed()) {
                    it.remove();
                    continue;
                }
                if (source.equals(i)) continue;
                ObjectOutputStream stream = new ObjectOutputStream(i.getOutputStream());
                stream.writeObject(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Socket getSocketByIP(long ip) {
        for (Socket i : sockets) {
            if (IP.getLongIP(i.getInetAddress().getHostAddress()) == ip) return i;
        }
        return null;
    }
}
