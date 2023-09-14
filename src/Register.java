import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Register implements ActionListener {
    UserInfo info = new UserInfo();
    JFrame frame = new JFrame("ChatApp");
    JPanel mainPanel = new JPanel();
    JPanel registerForm = new JPanel(new GridLayout(4,2));
    JLabel nameLabel = new JLabel("Username: ");
    JTextField userName = new JTextField();
    JLabel emailLabel = new JLabel("Email: ");
    JTextField userEmail = new JTextField();
    JLabel passwordLabel = new JLabel("Password: ");
    JPasswordField userPassword = new JPasswordField();
    JButton register = new JButton("Register");

    public void render(){
        mainPanel.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
        mainPanel.add(registerForm);

        registerForm.setPreferredSize(new Dimension(350,150));

        // Set custom font size
        Font customFont = new Font("Arial", Font.BOLD, 20); // You can adjust the size

        Dimension buttonSize = new Dimension(200, 50); // Adjust the size as needed
        register.setPreferredSize(buttonSize);

        nameLabel.setFont(customFont);
        emailLabel.setFont(customFont);
        passwordLabel.setFont(customFont);
        userName.setFont(new Font("Arial", Font.PLAIN, 20));
        userEmail.setFont(new Font("Arial", Font.PLAIN, 20));
        userPassword.setFont(customFont);
        register.setFont(customFont);

        // Set custom colors
        Color backgroundColor = new Color(240, 240, 240); // Light gray background
        Color labelColor = new Color(0, 102, 204); // Blue labels
        Color textColor = Color.WHITE; // White text
        Color buttonColor = new Color(20, 102, 0); // green buttons

        frame.getContentPane().setBackground(backgroundColor);
        registerForm.setBackground(backgroundColor);
        nameLabel.setForeground(labelColor);
        emailLabel.setForeground(labelColor);
        passwordLabel.setForeground(labelColor);
        userName.setBackground(textColor);
        userEmail.setBackground(textColor);
        userPassword.setBackground(textColor);
        register.setBackground(buttonColor);
        register.setForeground(textColor);

        registerForm.add(nameLabel);
        registerForm.add(userName);
        registerForm.add(emailLabel);
        registerForm.add(userEmail);
        registerForm.add(passwordLabel);
        registerForm.add(userPassword);

        // Add the registration form in the CENTER region
        mainPanel.add(registerForm, BorderLayout.CENTER);

        // Add the "Register" button in the SOUTH region
        mainPanel.add(register, BorderLayout.SOUTH);

        register.addActionListener(this);

        frame.setSize(450,300);
        frame.setLayout(new BorderLayout());
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(mainPanel,BorderLayout.CENTER);

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            Socket socket = new Socket("localhost",12340);
            BufferedReader computerResponse = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader tableResponse = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            OutputStream outputStream = socket.getOutputStream();
            PrintWriter printWriter = new PrintWriter(outputStream, true);

            // Send the "register" command to the server
            /*printWriter.println("register");
            printWriter.flush();*/

            // Send username and password to the server

            String enteredName = userName.getText();
            String enteredEmail = userEmail.getText();
            String enteredPassword = userPassword.getText();
            printWriter.println(enteredName);
            printWriter.flush();
            printWriter.println(enteredEmail);
            printWriter.flush();
            printWriter.println(enteredPassword);
            printWriter.flush();
//            createNameTable(enteredName);
            // Receive and process the server response
            /*String table=tableResponse.readLine();
            System.out.println(table);*/
            String receivedResponse = computerResponse.readLine();
            System.out.println("Response: " + receivedResponse);
            if(receivedResponse.equals("Registered")){
                JOptionPane.showMessageDialog(null,"sucessfull");
                frame.dispose();
                Login obj = new Login();
                obj.render();
            }
            else {
                JOptionPane.showMessageDialog(null,"unsucessfull");
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }
}
