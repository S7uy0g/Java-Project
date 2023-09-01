import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class Register implements ActionListener {
    JFrame frame = new JFrame("ChatApp");
    JPanel mainPanel = new JPanel();
    JPanel registerForm = new JPanel(new GridLayout(3,2));
    JLabel nameLabel = new JLabel("UserName: ");
    JTextField userName = new JTextField();
    JLabel passwordLabel = new JLabel("Password: ");
    JPasswordField userPassword = new JPasswordField();
    JButton register = new JButton("Register");

    Register(){
        mainPanel.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
        mainPanel.add(registerForm);

        registerForm.setPreferredSize(new Dimension(300,150));
        registerForm.add(nameLabel);
        registerForm.add(userName);
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

            // Send username and password to the server

            String enteredName = userName.getText();
            String enteredPassword = userPassword.getText();
            printWriter.println(enteredName);
            printWriter.flush();
            printWriter.println(enteredPassword);
            printWriter.flush();

            // Receive and process the server response
            String receivedResponse = computerResponse.readLine();
            System.out.println("Response: " + receivedResponse);
            if(receivedResponse.equals("Registered")){
                JOptionPane.showMessageDialog(null,"sucessfull");
            }
            else {
                JOptionPane.showMessageDialog(null,"unsucessfull");
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }
}
