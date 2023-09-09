package Server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static final int PORT = 12345;
    private static List<Socket> clients = new ArrayList<>();

    public static void main(String[] args) {

        Runnable serverRunnable = new RegisterServer();
        Thread serverTherad =new Thread(serverRunnable);
        serverTherad.start();

        Runnable loginRunnable = new LoginServer();
        Thread loginServer = new Thread(loginRunnable);
        loginServer.start();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server.Server started. Waiting for clients...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket);

                // Create a new thread to handle the client
                Thread clientThread = new Thread(new ClientHandler(clientSocket));
                clientThread.start();

                // Add the client socket to the list
                clients.add(clientSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private PrintWriter out;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("Received: " + message);

                    // Broadcast the message to all connected clients
                    for (Socket client : clients) {
                        if (client != clientSocket) {
                            PrintWriter clientOut = new PrintWriter(client.getOutputStream(), true);
                            clientOut.println(message);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // Remove the client socket from the list when disconnected
                clients.remove(clientSocket);
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
