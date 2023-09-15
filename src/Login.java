import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.sql.*;

public class Login implements ActionListener {
    UserInfo info = new UserInfo();
    public boolean isLoggedIn;
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
    String userName;
    public void render(){
        loginButton.addActionListener(this);
        registerButton.addActionListener(this);

        // Set custom colors
        Color backgroundColor = new Color(230, 240, 240); // Light gray background
        Color labelColor = new Color(0, 102, 204); // Blue labels
        Color buttonColor = new Color(20, 102, 0); // green buttons
        Color textColor = Color.WHITE; // White text

        // Apply custom colors to components
        loginFrame.getContentPane().setBackground(backgroundColor);
        top.setBackground(backgroundColor);
        left.setBackground(backgroundColor);
        right.setBackground(backgroundColor);
        bottom.setBackground(backgroundColor);

        usernameLabel.setForeground(labelColor);
        passwordLabel.setForeground(labelColor);
        usernameField.setBackground(textColor);

        loginButton.setBackground(buttonColor);
        loginButton.setForeground(textColor);
        registerButton.setBackground(buttonColor);
        registerButton.setForeground(textColor);

        // Set up custom font style
        Font customFont = new Font("Arial", Font.BOLD, 20); // You can choose a different font and size

        // Apply custom font to labels, text fields, and buttons
        usernameLabel.setFont(customFont);
        passwordLabel.setFont(customFont);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 20));
        passwordField.setFont(customFont);
        loginButton.setFont(customFont);
        registerButton.setFont(customFont);


        // Set up the main frame
        loginFrame.setSize(600, 500);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLayout(new BorderLayout());

        // Create empty space components for top, bottom, left, and right
        loginForm.setPreferredSize(new Dimension(50,50));
        top.setPreferredSize(new Dimension(100,150));
        left.setPreferredSize(new Dimension(150,100));
        right.setPreferredSize(new Dimension(150,100));
        bottom.setPreferredSize(new Dimension(100,150));

        // Set up the login form panel
        loginForm.setLayout(new GridLayout(3, 2,10,10));
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
        if(e.getSource() == loginButton) {
            try (Socket socket = new Socket(info.server, 12300)) {
                BufferedReader computerResponse = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                OutputStream outputStream = socket.getOutputStream();
                PrintWriter printWriter = new PrintWriter(outputStream, true);

                // Send the "register" command to the server
               /* printWriter.println("login");
                printWriter.flush();*/

                // Send username and password to the server
                String userNameEntered = usernameField.getText();
                String passwordEntered = passwordField.getText();
                printWriter.println(userNameEntered);
                printWriter.flush();
                printWriter.println(passwordEntered);
                printWriter.flush();

                // Receive and process the server response
                String receivedResponse = computerResponse.readLine();
                System.out.println("Response: " + receivedResponse);
                if(receivedResponse.equals("access")){
                    loginFrame.dispose();
                    Initialize d1=new Initialize();
                    d1.initializeApp(userNameEntered);
                    messageBox(receivedResponse,"Login accepted");
                }
                else {
                    messageBox("The login username or password is incorrect","Login denied");
                }
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            /*Initialize d1=new Initialize();
            d1.initializeApp(usernameField.getText());*/
        }
        if(e.getSource() == registerButton){
            loginFrame.dispose();
            // for localhost testing only
//            Runnable serverRunnable = new RegisterServer();
//            Thread serverTherad =new Thread(serverRunnable);
//            serverTherad.start();
            Register obj = new Register();
            obj.render();
        }
    }
    public static void messageBox(String message,String tittle){
        JOptionPane.showMessageDialog(null,message,"MessageBox:"+tittle,JOptionPane.INFORMATION_MESSAGE);
    }

}
