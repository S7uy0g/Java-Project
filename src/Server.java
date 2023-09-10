import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server {
    private static List<String> clients = new ArrayList<>();
    private static Map<String, Socket> clientMap = new HashMap<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(12345); // Choose a port
            System.out.println("Server started. Waiting for clients...");

            Runnable loginServer = new LoginServer();
            Thread loginThread = new Thread(loginServer);
            loginThread.start();

            Runnable registerServer = new RegisterServer();
            Thread registerThread = new Thread(registerServer);
            registerThread.start();

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());
                String clientName = inputStream.readUTF();
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

    public ResultSet getFriends(String name) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost/java_db";
            Connection conn = DriverManager.getConnection(url, "root", "Joker1245780");
            System.out.println("Connected to the database");
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("select * from Login where UserName='"+name+"'");
            return rs;
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            ResultSet rs=null;
            return rs;
        }
    }
    public void addFriend(String tableName,String friendName,String SID){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost/java_db";
            Connection conn = DriverManager.getConnection(url, "root", "Joker1245780");
            System.out.println("Connected to the database");
            // Insert the message into the conversation table
            String insertQuery = "INSERT INTO " + tableName + " (ID, userName) VALUES (?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(insertQuery);
            int id=Integer.parseInt(SID);
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, friendName);
            preparedStatement.executeUpdate();

            conn.close();
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }
    public ResultSet getMyFriends(String LoginName){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost/java_db";
            Connection conn = DriverManager.getConnection(url, "root", "Joker1245780");
            System.out.println("Connected to the database");
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("select * from "+LoginName);
            return rs;
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            ResultSet rs=null;
            return rs;
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

        private void broadcastText(String text) {
            String[] parts = text.split(":", 2); // Split the message into recipient and message text
            String recipientName = null;
            if (parts.length == 2) {
                recipientName = parts[0].trim();
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

            String msg=parts[1].trim();
            // Save the message in the conversation table or create one if it doesn't exist
            saveMessageToConversation(clientName, recipientName, msg);
        }

        private void saveMessageToConversation(String sender, String recipient, String message) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                String url = "jdbc:mysql://localhost/java_db";
                Connection conn = DriverManager.getConnection(url, "root", "Joker1245780");

                // Combine sender and recipient names to create a unique table name
                String tableName = sender.compareTo(recipient) < 0 ? sender + "_" + recipient : recipient + "_" + sender;

                // Check if the conversation table exists, if not, create one
                if (!doesTableExist(conn, tableName)) {
                    createConversationTable(conn, tableName);
                }

                // Insert the message into the conversation table
                String insertQuery = "INSERT INTO " + tableName + " (Sender, Message) VALUES (?, ?)";
                PreparedStatement preparedStatement = conn.prepareStatement(insertQuery);
                preparedStatement.setString(1, sender);
                preparedStatement.setString(2, message);
                preparedStatement.executeUpdate();

                conn.close();
            } catch (ClassNotFoundException | SQLException ex) {
                ex.printStackTrace();
            }
        }

        private boolean doesTableExist(Connection conn, String tableName) throws SQLException {
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet tables = metaData.getTables(null, null, tableName, null);
            return tables.next();
        }

        private void createConversationTable(Connection conn, String tableName) throws SQLException {
            Statement stmt = conn.createStatement();
            String createTableQuery = "CREATE TABLE " + tableName + " (ID INT AUTO_INCREMENT PRIMARY KEY, Sender VARCHAR(255), Message TEXT)";
            stmt.executeUpdate(createTableQuery);
        }
    }
}