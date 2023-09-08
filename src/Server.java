import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server {
    private static List<String> clients = new ArrayList<>();
    private static Map<String, Socket> clientMap = new HashMap<>();
    private static DataInputStream inputStream;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(12345); // Choose a port
            System.out.println("Server started. Waiting for clients...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                inputStream = new DataInputStream(clientSocket.getInputStream());
                String clientName;
                clientName = inputStream.readUTF();
                //String clientName = "Client" + clients.size();
                System.out.println(clientName);
                clients.add(clientName);
                clientMap.put(clientName, clientSocket);

                ClientHandler clientHandler = new ClientHandler(clientSocket, clientName);
                Thread clientThread = new Thread(clientHandler);
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private String clientName;
        private DataInputStream inputStream;

        public ClientHandler(Socket clientSocket, String clientName) {
            this.clientSocket = clientSocket;
            this.clientName = clientName;
            try {
                inputStream = new DataInputStream(clientSocket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                while (true) {
                    String receivedText = inputStream.readUTF();
                    System.out.println("Received from " + clientName + ": " + receivedText);

                    // Broadcast the received text to all other connected clients
                    broadcastText(receivedText);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Method to broadcast text to a specific client
        private void broadcastText(String text) {
            String[] parts = text.split(":", 2); // Split the message into recipient and message text
            if (parts.length == 2) {
                String recipientName = parts[0].trim();
                String messageText = parts[1].trim();

                Socket recipientSocket = clientMap.get(recipientName);
                if (recipientSocket != null) {
                    try {
                        DataOutputStream recipientOutputStream = new DataOutputStream(recipientSocket.getOutputStream());
                        recipientOutputStream.writeUTF(clientName + ": " + messageText);
                        recipientOutputStream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
