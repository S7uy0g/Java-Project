import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server1 {
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

                // Handle login and registration
                String loginResponse = handleLogin(clientSocket);
                if (loginResponse.equals("access")) {
                    // If login is successful, start the chat thread
                    ClientHandler clientHandler = new ClientHandler(clientSocket, clientName);
                    Thread clientThread = new Thread(clientHandler);
                    clientThread.start();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Method to handle login and registration
    private static String handleLogin(Socket clientSocket) {
        try {
            BufferedReader socketDataReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            OutputStream outputStream = clientSocket.getOutputStream();
            PrintWriter printWriter = new PrintWriter(outputStream, true);

            String command = socketDataReader.readLine();
            if (command.equals("register")) {
                String userName = socketDataReader.readLine();
                String password = socketDataReader.readLine();
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    String url = "jdbc:mysql://localhost/java_db";

                    try (Connection conn = DriverManager.getConnection(url, "root", "root")) {
                        System.out.println("Database connected: ");

                        // Use PreparedStatement to safely insert data
                        String insertQuery = "INSERT INTO Login (UserName, Password) VALUES (?, ?)";
                        try (PreparedStatement pstm = conn.prepareStatement(insertQuery)) {
                            pstm.setString(1, userName);
                            pstm.setString(2, password);

                            // Execute the INSERT statement
                            int rowsAffected = pstm.executeUpdate();

                            if (rowsAffected > 0) {
                                printWriter.println("Registered");
                            } else {
                                printWriter.println("unsuccessful");
                            }
                        } catch (SQLException e) {
                            printWriter.println("unsuccessful");
                            e.printStackTrace();
                        }
                    } catch (SQLException e) {
                        printWriter.println("unsuccessful");
                        e.printStackTrace();
                    }
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                return "Registered";
            } else if (command.equals("login")) {
                String userName = socketDataReader.readLine();
                String password = socketDataReader.readLine();
                Boolean access = false;

                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    String url = "jdbc:mysql://localhost/java_db";
                    Connection conn = DriverManager.getConnection(url, "root", "root");
                    System.out.println("Connected to the database");

                    Statement stm = conn.createStatement();
                    ResultSet rs = stm.executeQuery("select * from Login");
                    while (rs.next()) {
                        String name = rs.getString("UserName");
                        String passwordCheck = rs.getString("Password");
                        if (userName.equals(name) && password.equals(passwordCheck)) {
                            access = true;
                            break;
                        }
                    }
                    if (access) {
                        return "access";

                    } else {
                        return "denied";
                    }
                } catch (ClassNotFoundException ex) {
                    // Handle exceptions
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                return "access";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "denied";
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
