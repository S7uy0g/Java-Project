import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;

public class RegisterServer implements Runnable{
    UserInfo info = new UserInfo();
    @Override
    public void run() {
        try( ServerSocket serverSocket = new ServerSocket(12340)) {
            while (true) {
                try(Socket socket = serverSocket.accept()){
                    BufferedReader socketDataReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    OutputStream outputStream = socket.getOutputStream();
                    PrintWriter printWriter = new PrintWriter(outputStream, true);

                    String sendingResponse = "Registered";
                    String userName, password, email;

                    userName = socketDataReader.readLine();
                    email = socketDataReader.readLine();
                    password = socketDataReader.readLine();
                    System.out.println(userName);
                    System.out.println(email);
                    System.out.println(password);
                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        String url = "jdbc:mysql://localhost/java_db";
                        Connection conn = DriverManager.getConnection(url, info.userNameDB, info.passwordDB);
                        System.out.println("Database connected: ");
                        // Use PreparedStatement to safely insert data
                        String insertQuery = "INSERT INTO login (UserName, Email, Password) VALUES (?, ?, ?)";
                        try (PreparedStatement pstm = conn.prepareStatement(insertQuery)) {
                            pstm.setString(1, userName);
                            pstm.setString(2,email);
                            pstm.setString(3, password);

                            // Execute the INSERT statement
                            int rowsAffected = pstm.executeUpdate();

                            if (rowsAffected > 0) {
                                printWriter.println(sendingResponse);
                            } else {
                                printWriter.println("unsuccessful");
                            }
                        } catch (SQLException e) {
                            printWriter.println("unsuccessful");
                            e.printStackTrace();
                        }
                        conn.close();
                        }catch (ClassNotFoundException ex) {
                        // Handle exceptions
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }

                }catch (IOException e) {

                }
            }
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
