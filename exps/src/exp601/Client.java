package exp601;

import exp503.IP;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

public class Client {
    private static final String remoteIP = "127.0.0.1";
    private static final int remoteUDPPort = 35501;
    private static final int BUFF_LEN = 4096;
    DatagramSocket UDPSocket;
    DatagramPacket receive, send;
    ArrayList<ClientTCPHandler> chatrooms = new ArrayList<>();

    public Client() {
        try {
            Random random = new Random();
            int localUDPPort = random.nextInt(200) + 40000;
            UDPSocket = new DatagramSocket(localUDPPort);
            byte[] tmp = new byte[BUFF_LEN];
            receive = new DatagramPacket(tmp, tmp.length);
            Thread thread = new Thread(this::UDPHandler);
            Scanner scan = new Scanner(System.in);
            thread.start();
            while (true) {
                MessageType command = getCommand(scan);
                switch (command) {
                    case Creat:
                    case Join: {
                        System.out.print("Chatroom name:");
                        String name = scan.nextLine().strip();
                        ClientTCPHandler chatroom = getChatroomByName(name);
                        if (chatroom != null) {
                            System.out.println("You have joined chatroom " + name);
                            break;
                        }
                        Message message = new Message(command, name);
                        sendMessage(message, InetAddress.getByAddress(IP.getBytesIP(remoteIP)),
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
                        if (chatroom == null) {
                            System.out.println("You have not joined chatroom " + name);
                            break;
                        }
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
        Iterator<ClientTCPHandler> it = chatrooms.iterator();
        while (it.hasNext()) {
            ClientTCPHandler i = it.next();
            if (i == null) {
                it.remove();
                continue;
            }
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
                        String[] strs = message.getMessage().strip().split(" ", 2);
                        int port = Integer.parseInt(strs[0]);
                        ClientTCPHandler chatroom = new ClientTCPHandler(this, strs[1], remoteIP, port);
                        chatrooms.add(chatroom);
                        System.out.println("You have joined chatroom " + strs[1] + ".");
                        break;
                    }
                    case Error: {
                        System.out.println("Error: " + message.getMessage());
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
