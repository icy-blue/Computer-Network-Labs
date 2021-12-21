package exp601;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class ClientTCPHandler extends TCPHandler implements Runnable {
    Client client;
    String name;
    Socket socket;

    public ClientTCPHandler(Client client, String name, String ip, int port) throws IOException {
        socket = new Socket(ip, port);
        this.name = name;
        this.client = client;
        writer = new ObjectOutputStream(socket.getOutputStream());
        reader = new ObjectInputStream(socket.getInputStream());
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Message message = (Message) reader.readObject();
                switch (message.type) {
                    case Sent: {
                        System.out.println("Message sent successfully.");
                        break;
                    }
                    case Send: {
                        System.out.println(message.getMessage());
                        break;
                    }
                    case Quited: {
                        ClientTCPHandler chatroom = client.getChatroomByName(message.getMessage());
                        chatroom.socket.close();
                        client.chatrooms.remove(chatroom);
                        System.out.println("Chatroom " + message.getMessage() + " quit successfully.");
                        break;
                    }
                    case Error: {
                        System.out.println("Error: " + message.getMessage());
                        break;
                    }
                }

            } catch (SocketException ignored) {
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
