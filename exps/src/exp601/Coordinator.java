package exp601;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class Coordinator {
    public static final int port = 4046;
    int unusedPort = 30000;
    ArrayList<Server> servers = new ArrayList<>();
    DatagramSocket datagramSocket;
    DatagramPacket receive, send;

    public Coordinator() {
        while (true) {
            try {
                datagramSocket = new DatagramSocket(port);
                datagramSocket.receive(receive);
                Message message = Message.decode(receive.getData());
                Message response;
                switch (message.type) {
                    case Join: {
                        Server server = getServerByName(message.message);
                        if (server != null) {
                            response = new Message(MessageType.Joined, Integer.toString(server.port));
                        } else {
                            response = new Message(MessageType.Error, "Room not found.");
                        }
                        break;
                    }
                    case Creat: {
                        MessageType type = MessageType.Existed;
                        Server server = getServerByName(message.message);
                        if (server == null) {
                            type = MessageType.Created;
                            server = new Server(message.message, ++unusedPort);
                            servers.add(server);
                        }
                        response = new Message(type, Integer.toString(server.port));
                        break;
                    }
                    default: {
                        response = new Message(MessageType.Error, "Type of " + message.type + "is ignored");
                    }
                }
                sendMessage(response, receive.getAddress(), receive.getPort());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Server getServerByName(String name) {
        for (Server server : servers) {
            if (name.equals(server.name)) {
                return server;
            }
        }
        return null;
    }

    private void sendMessage(Message message, InetAddress address, int port) throws IOException {
        byte[] buff = message.encode();
        send = new DatagramPacket(buff, 0, buff.length, address, port);
        datagramSocket.send(send);
    }

    public static void main(String[] args) {
        new Coordinator();
    }
}
