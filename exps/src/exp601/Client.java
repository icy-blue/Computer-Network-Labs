package exp601;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Client {
    private static final String remoteIP = "0.0.0.0";
    private static final int remoteUDPPort = 4046;
    DatagramSocket UDPSocket;
    DatagramPacket receive, send;
    ArrayList<ClientTCPHandler> chatrooms = new ArrayList<>();

    public Client() {
        try {
            Random random = new Random();
            int localUDPPort = random.nextInt(200) + 40000;
            UDPSocket = new DatagramSocket(localUDPPort);
            Thread thread = new Thread(this::UDPHandler);
            Scanner scan = new Scanner(System.in);

            while (true) {
                MessageType command = getCommand(scan);
                switch (command) {
                    case Creat:
                    case Join: {
                        System.out.print("Chatroom name:");
                        String name = scan.nextLine().strip();
                        Message message = new Message(command, name);
                        sendMessage(message, InetAddress.getByAddress(remoteIP.getBytes(StandardCharsets.UTF_8)),
                                remoteUDPPort);
                        break;
                    }
                    case Quit: {
                        System.out.print("Chatroom name:");
                        String name = scan.nextLine().strip();
                        ClientTCPHandler chatroom = getChatroomByName(name);
                        if (chatroom == null) {
                            System.out.println("You have not joined chatroom " + name);
                            break;
                        }
                        Message message = new Message(MessageType.Quit, name);
                        ObjectOutputStream output = chatroom.writer;
                        output.writeObject(message);
                        break;
                    }
                    case Send: {
                        System.out.print("Chatroom name:");
                        String name = scan.nextLine().strip();
                        ClientTCPHandler chatroom = getChatroomByName(name);
                        System.out.print("Message:");
                        String data = scan.nextLine().strip();
                        Message message = new Message(MessageType.Send, data);
                        ObjectOutputStream output = chatroom.writer;
                        output.writeObject(message);
                        break;
                    }
                    default: {
                        System.out.println("Command " + command + " is invalid.");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Client();
    }

    private MessageType getCommand(Scanner scan) {
        while (true) {
            try {
                System.out.print("Command:");
                String command = scan.nextLine().strip();
                return MessageType.getTypeByName(command);
            } catch (Exception ignored) {
            }
        }
    }

    private void sendMessage(Message message, InetAddress address, int port) throws IOException {
        byte[] buff = message.encode();
        send = new DatagramPacket(buff, 0, buff.length, address, port);
        UDPSocket.send(send);
    }

    ClientTCPHandler getChatroomByName(String name) {
        for (ClientTCPHandler i : chatrooms) {
            if (i.name.equals(name)) return i;
        }
        return null;
    }

    void UDPHandler() {
        while (true) {
            try {
                UDPSocket.receive(receive);
                Message message = Message.decode(receive.getData());
                System.out.println();
                switch (message.type) {
                    case Created:
                    case Joined:
                    case Existed: {
                        int port = Integer.parseInt(message.message);
                        ClientTCPHandler chatroom = new ClientTCPHandler(this, remoteIP, port);
                        chatrooms.add(chatroom);
                        break;
                    }
                    case Error: {
                        System.out.println("Error: " + message.message);
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
