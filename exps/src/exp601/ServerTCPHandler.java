package exp601;

import exp503.IP;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerTCPHandler extends TCPHandler implements Runnable {
    Server server;

    @Override
    public void run() {
        try {
            long ip = IP.getLongIP(socket.getInetAddress().getHostAddress());
            writer = new ObjectOutputStream(socket.getOutputStream());
            reader = new ObjectInputStream(socket.getInputStream());
            while (true) {
                Message data = (Message) reader.readObject();
                switch (data.type) {
                    case Send: {
                        data.message = new ChatData(data.message, server.name, ip).toString();
                        chatData.add(data.message);
                        Message response = new Message(MessageType.Sent, "");
                        writer.writeObject(response);
                        server.broadcast(socket, data);
                        break;
                    }
                    case Quit: {
                        Message response = new Message(MessageType.Quited, server.name);
                        writer.writeObject(response);
                        socket.close();
                        server.sockets.remove(socket);
                        return;
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    ServerTCPHandler(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;
    }
}
