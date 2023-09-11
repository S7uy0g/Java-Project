import com.sun.source.tree.WhileLoopTree;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;

public class LoginServer implements Runnable{
    String sendingResponse="access";
    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(12300)) {
            while (true) {
                try (Socket socket = serverSocket.accept()) {
                    BufferedReader socketDataReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    OutputStream outputStream = socket.getOutputStream();
                    PrintWriter printWriter = new PrintWriter(outputStream, true);

                    String userName, password;

                    userName = socketDataReader.readLine();
                    password = socketDataReader.readLine();

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
                            sendingResponse = "access";

                        } else {
                            sendingResponse = "denied";

                        }
                    } catch (ClassNotFoundException ex) {
                        // Handle exceptions
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }

                    // Send the response to the client
                    printWriter.println(sendingResponse);
                } catch (IOException e) {
                    // Handle exceptions
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}