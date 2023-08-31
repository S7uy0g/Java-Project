import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.sql.*;

public class Login implements ActionListener {
    JPanel top = new JPanel();
    JPanel left = new JPanel();
    JPanel right = new JPanel();
    JPanel bottom = new JPanel();
    JFrame loginFrame = new JFrame("ChatApp");
    JPanel loginForm = new JPanel();
    JLabel usernameLabel = new JLabel("Username:");
    JTextField usernameField = new JTextField(20);
    JLabel passwordLabel = new JLabel("Password:");
    JPasswordField passwordField = new JPasswordField(20);
    JButton loginButton = new JButton("Login");
    JButton registerButton = new JButton("Register");

    public void render(){
        loginButton.addActionListener(this);

        // Set up the main frame
        loginFrame.setSize(500, 500);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLayout(new BorderLayout());

        // Create empty space components for top, bottom, left, and right
        loginForm.setPreferredSize(new Dimension(50,50));
        top.setPreferredSize(new Dimension(100,175));
        left.setPreferredSize(new Dimension(150,100));
        right.setPreferredSize(new Dimension(150,100));
        bottom.setPreferredSize(new Dimension(100,175));

        // Set up the login form panel
        loginForm.setLayout(new GridLayout(3, 2));
        loginForm.add(usernameLabel);
        loginForm.add(usernameField);
        loginForm.add(passwordLabel);
        loginForm.add(passwordField);
        loginForm.add(loginButton);
        loginForm.add(registerButton);

        // Add the empty space components and loginForm to the frame
        loginFrame.add(top, BorderLayout.NORTH);
        loginFrame.add(bottom, BorderLayout.SOUTH);
        loginFrame.add(left, BorderLayout.WEST);
        loginFrame.add(right, BorderLayout.EAST);
        loginFrame.add(loginForm, BorderLayout.CENTER);

        loginFrame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == loginButton){
            String userNameEntered;
            String passwordEnterd;

            //------------------Sending user entered data and storing response from server-----------------------
            try {
                Socket socket = new Socket("localhost",12300);
                BufferedReader computerResponse = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                OutputStream outputStream = socket.getOutputStream();
                PrintWriter printWriter  = new PrintWriter(outputStream,true);

                String sendingData , receivedResponse;

               while (true){
                   userNameEntered = usernameField.getText();
                   passwordEnterd = passwordField.getText();

                   sendingData = userNameEntered;
                   printWriter.println(sendingData);
                   printWriter.flush();

                   sendingData = passwordEnterd;
                   printWriter.println(sendingData);
                   printWriter.flush();

                   receivedResponse = computerResponse.readLine();
                   System.out.println("Response:"+receivedResponse);

               }
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }
        }
    }
}

