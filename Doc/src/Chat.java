import javax.swing.*;
import java.awt.*;

public class Chat {
    JFrame frame =  new JFrame("Chat App");
    JPanel panel1 = new JPanel();
    JPanel panel2 = new JPanel();
    JPanel panel3 = new JPanel();
    JPanel panel4 = new JPanel();
    JPanel panel5 = new JPanel();
    JMenuBar navigation = new JMenuBar();
    JMenu File = new JMenu("FILE");
    JMenu otherOptions = new JMenu("Other Options");
    JTextArea messageDisplay = new JTextArea();
    JTextPane messageInput = new JTextPane();

    JButton sendMessage = new JButton("Send");
    public void render(){
        navigation.add(File);
        navigation.add(otherOptions);

        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500,500);
        frame.setLayout(new BorderLayout(5,5));
        frame.setVisible(true);

        //panel color
        panel1.setBackground(Color.lightGray);
        panel2.setBackground(Color.red);
        panel3.setBackground(Color.green);
//        panel4.setBackground(Color.yellow);
//        panel5.setBackground(Color.blue);

        panel1.setPreferredSize(new Dimension(100,100));
        panel2.setPreferredSize(new Dimension(100,100));
        panel3.setPreferredSize(new Dimension(100,50));
        panel4.setPreferredSize(new Dimension(100,100));
        panel5.setPreferredSize(new Dimension(100,50));


        frame.add(panel1,BorderLayout.WEST);
        frame.add(panel2,BorderLayout.EAST);
        frame.add(panel3,BorderLayout.NORTH);
        frame.add(panel4,BorderLayout.CENTER);
        frame.add(panel5,BorderLayout.SOUTH);

        frame.setJMenuBar(navigation);

        panel4.setLayout(new GridLayout());
        panel4.add(messageDisplay);
        panel5.setLayout(new BorderLayout(0,0));
        panel5.add(messageInput,BorderLayout.NORTH);
        panel5.add(sendMessage,BorderLayout.SOUTH);

    }
}
