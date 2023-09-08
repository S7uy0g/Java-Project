import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class Register implements ActionListener {
    JFrame frame = new JFrame("ChatApp");
    JPanel mainPanel = new JPanel();
<<<<<<< HEAD
    JPanel registerForm = new JPanel(new GridLayout(4,2));
    JLabel nameLabel = new JLabel("UserName: ");
    JTextField userName = new JTextField();
    JLabel emailLabel = new JLabel("Email: ");
    JTextField userEmail = new JTextField();
=======
    JPanel registerForm = new JPanel(new GridLayout(3,2));
    JLabel nameLabel = new JLabel("UserName: ");
    JTextField userName = new JTextField();
>>>>>>> Backup
    JLabel passwordLabel = new JLabel("Password: ");
    JPasswordField userPassword = new JPasswordField();
    JButton register = new JButton("Register");

<<<<<<< HEAD
    Register(){
=======
    public void render(){
>>>>>>> Backup
        mainPanel.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
        mainPanel.add(registerForm);

        registerForm.setPreferredSize(new Dimension(300,150));
        registerForm.add(nameLabel);
        registerForm.add(userName);
<<<<<<< HEAD
        registerForm.add(emailLabel);
        registerForm.add(userEmail);
=======
>>>>>>> Backup
        registerForm.add(passwordLabel);
        registerForm.add(userPassword);
        registerForm.add(register);

        register.addActionListener(this);


        frame.setSize(500,500);
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
            OutputStream outputStream = socket.getOutputStream();
            PrintWriter printWriter = new PrintWriter(outputStream, true);

<<<<<<< HEAD
            // Send username, email and password to the server

            String enteredName = userName.getText();
            String enteredEmail = userEmail.getText();
            String enteredPassword = userPassword.getText();
            printWriter.println(enteredName);
            printWriter.flush();
            printWriter.println(enteredEmail);
            printWriter.flush();
=======
            // Send the "register" command to the server
            /*printWriter.println("register");
            printWriter.flush();*/

            // Send username and password to the server

            String enteredName = userName.getText();
            String enteredPassword = userPassword.getText();
            printWriter.println(enteredName);
            printWriter.flush();
>>>>>>> Backup
            printWriter.println(enteredPassword);
            printWriter.flush();

            // Receive and process the server response
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
