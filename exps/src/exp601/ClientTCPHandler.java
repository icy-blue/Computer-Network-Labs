package exp601;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientTCPHandler extends TCPHandler implements Runnable {
    Client client;
    String name;
    Socket socket;

    public ClientTCPHandler(Client client, String ip, int port) throws IOException {
        socket = new Socket(ip, port);
        this.client = client;
    }

    @Override
    public void run() {
        try {
            writer = new ObjectOutputStream(socket.getOutputStream());
            reader = new ObjectInputStream(socket.getInputStream());
            while (true) {
                Message message = (Message) reader.readObject();
                System.out.println();
                switch (message.type) {
                    case Sent: {
                        System.out.println("Message sent successfully.");
                        break;
                    }
                    case Send: {
                        System.out.println(message.message);
                        break;
                    }
                    case Quited: {
                        ClientTCPHandler chatroom = client.getChatroomByName(message.message);
                        chatroom.socket.close();
                        client.chatrooms.remove(chatroom);
                        System.out.println("Chatroom " + message.message + " quit successfully.");
                        break;
                    }
                    case Error: {
                        System.out.println("Error: " + message.message);
                        break;
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
