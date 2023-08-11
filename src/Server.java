import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static List<DataOutputStream> clientOutputStreams = new ArrayList<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(12345); // Choose a port
            System.out.println("Server started. Waiting for clients...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                DataOutputStream clientOutputStream = new DataOutputStream(clientSocket.getOutputStream());
                clientOutputStreams.add(clientOutputStream);

                Thread clientThread = new Thread(new ClientHandler(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private DataInputStream inputStream;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
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
                    System.out.println("Received from client: " + receivedText);

                    distributeText(receivedText, clientSocket);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void distributeText(String text, Socket senderSocket) {
            for (DataOutputStream outputStream : clientOutputStreams) {
                if (!outputStream.equals(senderSocket)) {
                    try {
                        outputStream.writeUTF(text);
                        outputStream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
