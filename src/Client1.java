import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.Socket;
import javax.swing.*;

public class Client1 {
    public static JTextArea textArea = new JTextArea();
    public static Socket clientSocket;
    public static DataOutputStream outputStream;

    public static void main(String[] args) {
        JFrame frame = new JFrame();
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

        //-----------scroll pane-----------------------------------
        JScrollPane scrollPane = new JScrollPane(textArea);
        //JPanel buttonPanel = new JPanel();
        //frame.setLayout(new BorderLayout());
        frame.add(scrollPane, BorderLayout.CENTER);

        try {
            clientSocket = new Socket("localhost", 12345); // Change to your server's IP and port
            outputStream = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                sendTextToServer();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                sendTextToServer();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Not used for plain text documents
            }
        });

        // Start a thread to read from the server
        new Thread(() -> {
            try {
                DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());

                while (true) {
                    String receivedText = inputStream.readUTF();
                    if (!receivedText.equals(textArea.getText())) {
                        SwingUtilities.invokeLater(() -> textArea.setText(receivedText));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        SwingUtilities.invokeLater(() -> frame.setVisible(true));
    }

    private static void sendTextToServer() {
        try {
            String text = textArea.getText();
            if (!text.equals("")) {
                outputStream.writeUTF(text);
                outputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
