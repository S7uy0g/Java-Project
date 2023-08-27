import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.Socket;

public class Client1 {
    public static JTextArea textArea = new JTextArea();
    public static JPanel messagePanel = new JPanel();
    public static JScrollPane messageScrollPane=new JScrollPane(messagePanel);
    public static Socket clientSocket;
    public static DataOutputStream outputStream;
    public static JFrame frame = new JFrame();

    public static void main(String[] args) {

        JPanel navigationBar = new JPanel();
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu viewMenu = new JMenu("View");
        JMenu editMenu = new JMenu("Edit");
        JMenuItem saveMenu = new JMenuItem("Save");
        JMenuItem loadMenu = new JMenuItem("Load");
        JMenuItem exitMenu = new JMenuItem("Exit");
        JPanel leftPanel = new JPanel();
        //JPanel workingPanel = new JPanel();
        JPanel rightPanel = new JPanel();
        JButton sendButton = new JButton("Send");
        JButton chooseFileButton = new JButton("Choose File");

        frame.setSize(500,500);
        frame.setLayout(new BorderLayout(5,5));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setTitle("Google Docs Inspired Real-Time Text Editor");
        //frame.setSize(800, 600);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));

        //---------------navigation bar----------------------------
        navigationBar.setBackground(Color.lightGray);
        navigationBar.setPreferredSize(new Dimension(100,40));
        navigationBar.setLayout(new BorderLayout());
        frame.getContentPane().add(navigationBar,BorderLayout.NORTH);

        //-------------adding menu options--------------------------
        fileMenu.add(saveMenu);
        fileMenu.add(loadMenu);
        fileMenu.add(exitMenu);
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(viewMenu);
        navigationBar.add(menuBar);
        menuBar.add(sendButton);
        menuBar.add(chooseFileButton);

        //---------------adding shortcut keys--------------------------
        fileMenu.setMnemonic(KeyEvent.VK_F);//press Alt+f for file menu
        exitMenu.setMnemonic(KeyEvent.VK_E);//press Alt+f and than Alt+e to exit directly
        saveMenu.setMnemonic(KeyEvent.VK_S);//press Alt+F and than Alt+s to save the file
        loadMenu.setMnemonic(KeyEvent.VK_L);//press Alt+f and then Alt+l to load the saved data

        //---------------left panel--------------------------------------
        leftPanel.setBackground(Color.lightGray);
        leftPanel.setPreferredSize(new Dimension(130,100));
        frame.add(leftPanel,BorderLayout.WEST);

        //---------------right panel------------------------------------
        rightPanel.setBackground(Color.lightGray);
        rightPanel.setPreferredSize(new Dimension(130,100));
        frame.add(rightPanel,BorderLayout.EAST);

        // Create a scroll pane for the message area
        messageScrollPane.setPreferredSize(new Dimension(10, 100));
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));

        // Add the message scroll pane to the frame at the top
        frame.add(messageScrollPane, BorderLayout.CENTER);

        //-----------scroll pane-----------------------------------
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(10,20));
        //JPanel buttonPanel = new JPanel();
        //frame.setLayout(new BorderLayout());
        frame.add(scrollPane, BorderLayout.SOUTH);
        //frame.add(buttonPanel, BorderLayout.SOUTH);

        try {
            clientSocket = new Socket("localhost", 12345); // Change to your server's IP and port
            outputStream = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendTextToServer();
                //textArea.setText("");
            }
        });

        // Start a thread to read from the server
        new Thread(() -> {
            try {
                DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());

                while (true) {
                    String receivedText = inputStream.readUTF();
                    String text="Received:"+receivedText;
                    JLabel label = new JLabel(text);
                    messagePanel.add(label);
                    messagePanel.revalidate();
                    messagePanel.repaint(); // Ensure proper repainting
                    messageScrollPane.getVerticalScrollBar().setValue(messageScrollPane.getVerticalScrollBar().getMaximum()); // Scroll to the bottom
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        SwingUtilities.invokeLater(() -> frame.setVisible(true));
    }

    private static void sendTextToServer() {
        try {
            String msg = textArea.getText();
            if (!msg.equals("")) {
                String text=msg;
                JLabel label = new JLabel(text);
                messagePanel.add(label);
                messagePanel.revalidate();
                messagePanel.repaint(); // Ensure proper repainting
                messageScrollPane.getVerticalScrollBar().setValue(messageScrollPane.getVerticalScrollBar().getMaximum()); // Scroll to the bottom
                outputStream.writeUTF(text);
                outputStream.flush();
                textArea.setText("");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
