import javax.swing.*;
import java.awt.*;

public class Interface {
    JFrame frame = new JFrame("Doc");
    JPanel navigationBar = new JPanel();
    JPanel leftPanel = new JPanel();
    JPanel workingPanel = new JPanel();
    JPanel rightPanel = new JPanel();
    JTextArea textArea = new JTextArea();
    JLabel fileOption = new JLabel("File");
    JLabel editOption = new JLabel("Edit");
    JLabel viewOption = new JLabel("View");
    JLabel empty = new JLabel();
    ImageIcon logo = new ImageIcon("logo.jpg");
    JLabel imgLogo = new JLabel();

    public void render(){

        frame.setSize(500,500);
        frame.setLayout(new BorderLayout(5,5));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        //---------------navigation bar----------------------------
        navigationBar.setBackground(Color.lightGray);
        navigationBar.setPreferredSize(new Dimension(100,60));
        navigationBar.setLayout(new BorderLayout());
        frame.add(navigationBar,BorderLayout.NORTH);

        //-------------adding options--------------------------
        fileOption.setBounds(50,10,30,20);
        editOption.setBounds(100,10,30,20);
        viewOption.setBounds(150,10,30,20);
        imgLogo.setBounds(0,10,30,30);
        navigationBar.add(fileOption);
        navigationBar.add(editOption);
        navigationBar.add(viewOption);
        logo.setImage(logo.getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT));
        imgLogo.setIcon(logo);
        navigationBar.add(imgLogo);
        //-----------emptu label is added to adjust the bug----------
        navigationBar.add(empty);

        //---------------left panel--------------------------------------
        leftPanel.setBackground(Color.lightGray);
        leftPanel.setPreferredSize(new Dimension(130,100));
        frame.add(leftPanel,BorderLayout.WEST);

        //---------------center panel----------------------------------
        workingPanel.setBackground(Color.lightGray);
        workingPanel.setPreferredSize(new Dimension(100,100));
        workingPanel.setLayout(new BorderLayout());
        frame.add(workingPanel,BorderLayout.CENTER);

        //---------------adding text area in center panel------------
        textArea.setPreferredSize(new Dimension(100,100));
        workingPanel.add(textArea);

        //---------------right panel------------------------------------
        rightPanel.setBackground(Color.lightGray);
        rightPanel.setPreferredSize(new Dimension(130,100));
        frame.add(rightPanel,BorderLayout.EAST);
    }

}
