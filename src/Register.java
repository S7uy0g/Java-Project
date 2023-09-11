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
    JLabel nameLabel = new JLabel("UserName: ");
    JTextField userName = new JTextField();
    JLabel emailLabel = new JLabel("Email: ");
    JTextField userEmail = new JTextField();
    JLabel passwordLabel = new JLabel("Password: ");
    JPasswordField userPassword = new JPasswordField();
    JButton register = new JButton("Register");

    public void render(){
        mainPanel.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
        mainPanel.add(registerForm);

        registerForm.setPreferredSize(new Dimension(300,150));
        registerForm.add(nameLabel);
        registerForm.add(userName);
        registerForm.add(emailLabel);
        registerForm.add(userEmail);
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
    public void createNameTable(String name)
    {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost/java_db";
            Connection conn = DriverManager.getConnection(url, info.userNameDB, info.passwordDB);
            System.out.println("Database connected: ");
            //Create Friend List Table in database
            //Create Table
            String createTableSQL = "CREATE TABLE " + name + " (ID INT PRIMARY KEY, userName VARCHAR(255))";
            try (Statement stm = conn.createStatement()) {
                stm.executeUpdate(createTableSQL);
                System.out.println("Table");
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("No Table");
            }
            conn.close();
        }catch (ClassNotFoundException ex) {
            // Handle exceptions
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
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
            createNameTable(enteredName);
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
