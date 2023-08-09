import javax.swing.*;
import java.awt.*;

public class Interface {
    JFrame frame = new JFrame("Doc");
    JPanel navigationBar = new JPanel();
    JPanel leftPanel = new JPanel();
    JPanel workingPanel = new JPanel();
    JPanel rightPanel = new JPanel();
    JTextArea textArea = new JTextArea();

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
