import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;

public class Interface implements ActionListener{
    JFrame frame = new JFrame("Doc");
    JPanel navigationBar = new JPanel();
    JPanel leftPanel = new JPanel();
    JPanel workingPanel = new JPanel();
    JPanel rightPanel = new JPanel();
    JTextArea textArea = new JTextArea();
    JMenuBar menuBar = new JMenuBar();
    JMenu fileMenu = new JMenu("File");
    JMenu viewMenu = new JMenu("View");
    JMenu editMenu = new JMenu("Edit");
    JMenuItem saveMenu = new JMenuItem("Save");
    JMenuItem loadMenu = new JMenuItem("Load");
    JMenuItem exitMenu = new JMenuItem("Exit");

    public void render(){

        frame.setSize(500,500);
        frame.setLayout(new BorderLayout(5,5));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        //---------------navigation bar----------------------------
        navigationBar.setBackground(Color.lightGray);
        navigationBar.setPreferredSize(new Dimension(100,40));
        navigationBar.setLayout(new BorderLayout());
        frame.add(navigationBar,BorderLayout.NORTH);

        //-------------adding menu options--------------------------
        fileMenu.add(saveMenu);
        fileMenu.add(loadMenu);
        fileMenu.add(exitMenu);
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(viewMenu);
        navigationBar.add(menuBar);

        //---------------adding events to file option-----------------
        exitMenu.addActionListener(this);
        loadMenu.addActionListener(this);
        saveMenu.addActionListener(this);

        //---------------adding shortcut keys--------------------------
        fileMenu.setMnemonic(KeyEvent.VK_F);//press Alt+f for file menu
        exitMenu.setMnemonic(KeyEvent.VK_E);//press Alt+f and than Alt+e to exit directly

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

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == exitMenu){
            frame.dispose();
        }
        if(e.getSource() == saveMenu){
            saveFile();
        }
        if(e.getSource() == loadMenu){
            loadFile();
        }
    }
    public void saveFile(){
        try{
            FileWriter writer = new FileWriter("Current file");
            writer.write(textArea.getText());
            System.out.println("data saved sucessfully in file");
            writer.close();
        }
        catch (Exception e){

        }
    }
    public void loadFile(){
        char[] array = new char[2000];
        try{
            FileReader input = new FileReader("Current file");

            input.read(array);
            System.out.println("Data in the file: ");
            textArea.setText(Arrays.toString(array));

            input.close();
        }
        catch (Exception e){
            System.out.println("No file found");
        }
    }
}
