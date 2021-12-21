package exp601;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

public class Coordinator {
    public static int port = 35501;
    int unusedPort = 30000;
    private static final int BUFF_LEN = 4096;
    ArrayList<Server> servers = new ArrayList<>();
    DatagramSocket datagramSocket;
    DatagramPacket receive, send;

    public Coordinator() {
        try {
            datagramSocket = new DatagramSocket(port);
            byte[] tmp = new byte[BUFF_LEN];
            receive = new DatagramPacket(tmp, tmp.length);
        } catch (SocketException e) {
            e.printStackTrace();
            return;
        }
        while (true) {
            try {
                datagramSocket.receive(receive);
                Message message = Message.decode(receive.getData());
                Message response;
                System.out.println("Received type=" + message.type.toString() + ", message=" + message.getMessage() + ".");
                switch (message.type) {
                    case Join: {
                        Server server = getServerByName(message.getMessage());
                        if (server != null) {
                            response = new Message(MessageType.Joined, server.port + " " + message.getMessage());
                        } else {
                            response = new Message(MessageType.Error, "Room not found.");
                        }
                        break;
                    }
                    case Creat: {
                        MessageType type = MessageType.Existed;
                        Server server = getServerByName(message.getMessage());
                        if (server == null) {
                            type = MessageType.Created;
                            server = new Server(message.getMessage(), ++unusedPort);
                            servers.add(server);
                        }
                        response = new Message(type, server.port + " " + message.getMessage());
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
