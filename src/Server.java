import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static List<Socket> clientSockets = new ArrayList<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(12345); // Use the same port as in the client
            System.out.println("Server started. Waiting for client...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected!");
                clientSockets.add(clientSocket);

                // Handle client's input
                new Thread(() -> {
                    try {
                        InputStream inputStream = clientSocket.getInputStream();
                        byte[] buffer = new byte[1024];
                        int bytesRead;

                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            String receivedText = new String(buffer, 0, bytesRead);
                            System.out.println("Received: " + receivedText);

                            // Broadcast the received text to all clients
                            for (Socket socket : clientSockets) {
                                try {
                                    socket.getOutputStream().write(receivedText.getBytes());
                                    socket.getOutputStream().flush();
                                } catch (IOException e) {
                                    // Handle potential client disconnection here
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
