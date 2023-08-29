import javax.swing.*;
import java.awt.*;

public class Login {
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

}

