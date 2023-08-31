import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

    }
}
