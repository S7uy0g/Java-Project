import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class Initialize {
    public static JPanel messagePanel = new JPanel();
    public static JScrollPane messageScrollPane = new JScrollPane(messagePanel);
    public static Socket clientSocket;
    public static DataOutputStream outputStream;
    public static JFrame frame = new JFrame();
    public static String Receiver = null;
    JTextField inputTextField;
    JLabel msgLabel;
    Map<String, JFrame> openMessageFrames = new HashMap<>();
    String clientName;
    public void initializeApp(String LoginName) {
        this.clientName = LoginName;
        JPanel navigationBar = new JPanel();
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Settings");
        JMenu viewMenu = new JMenu("Search");
        JMenu editMenu = new JMenu("Profile");
       /* JMenuItem saveMenu = new JMenuItem("Save");
        JMenuItem loadMenu = new JMenuItem("Load");
        JMenuItem exitMenu = new JMenuItem("Exit");*/
        JPanel leftPanel = new JPanel();
        JPanel rightPanel = new JPanel();
        /*JButton sendButton = new JButton("Send");*/
       /* JButton chooseFileButton = new JButton("Choose File");*/

        frame.setSize(500, 500);
        frame.setLayout(new BorderLayout(5, 5));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setTitle("Chat App");

        // Navigation bar
        navigationBar.setBackground(Color.lightGray);
        navigationBar.setPreferredSize(new Dimension(100, 40));
        navigationBar.setLayout(new BorderLayout());
        frame.getContentPane().add(navigationBar, BorderLayout.NORTH);

        // Menu options
       /* fileMenu.add(saveMenu);
        fileMenu.add(loadMenu);
        fileMenu.add(exitMenu);*/
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(viewMenu);
        navigationBar.add(menuBar);
       /* menuBar.add(sendButton);
        menuBar.add(chooseFileButton);*/

        // Left panel
        leftPanel.setBackground(Color.lightGray);
        leftPanel.setPreferredSize(new Dimension(130, 100));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost/java_db";
            Connection conn = DriverManager.getConnection(url, "root", "Joker1245780");
            System.out.println("Connected to the database");
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("select * from Login");
            while (rs.next()) {
                String name = rs.getString("UserName");
                if (name.equals(LoginName)) {
                    continue;
                }
                JLabel person1 = new JLabel(name);
                leftPanel.add(person1);
                frame.add(leftPanel, BorderLayout.WEST);
                person1.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        Receiver = person1.getText();
                        frame.dispose();
                        createMessageFrame(Receiver,LoginName);
                    }
                });
            }
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }

        // Right panel
        rightPanel.setBackground(Color.lightGray);
        rightPanel.setPreferredSize(new Dimension(130, 100));
        frame.add(rightPanel, BorderLayout.EAST);
        frame.setVisible(true);
    }

    private static String getConversationTableName(String clientName, String recipient) {
        return (clientName.compareTo(recipient) < 0 ? clientName + "_" + recipient : recipient + "_" + clientName);
    }

    private void createMessageFrame(String recipient,String loginName) {
        // Connection
        try {
            clientSocket = new Socket("localhost", 12345);
            outputStream = new DataOutputStream(clientSocket.getOutputStream());
            outputStream.writeUTF(loginName);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JFrame messageFrame = new JFrame("Messenger");
        openMessageFrames.put(recipient, messageFrame); // Store the message frame in the map
        messageFrame.setSize(400, 300);
        //messageFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Prevent closing by default close operation
        JPanel msgPanel = new JPanel();
        JPanel bottomPanel = new JPanel();
        inputTextField = new JTextField(30);
        JButton sendButton = new JButton("Send");
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(inputTextField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);
        messageFrame.setVisible(true);
        //Load Previous Msg
        String conversationTable = getConversationTableName(clientName, recipient);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost/java_db";
            Connection conn = DriverManager.getConnection(url, "root", "Joker1245780");
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT Sender, Message FROM " + conversationTable);
            System.out.println("Executed");
            msgPanel.removeAll();
            //messagePanel.removeAll(); // Clear existing messages before loading new ones
            while (rs.next()) {
                String sender = rs.getString("Sender");
                String message = rs.getString("Message");
                String msg = sender + ": " + message;
                System.out.println(msg);
                // Display the previous message in the message panel
                msgLabel = new JLabel(msg);
                msgPanel.add(msgLabel);
            }
            msgPanel.revalidate();
            msgPanel.repaint();
            scrollToBottom(messageScrollPane);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        // Create a DataInputStream to receive messages from the server
        DataInputStream inputStream;

        try {
            inputStream = new DataInputStream(clientSocket.getInputStream());

            // Start a thread to continuously receive messages
            new Thread(() -> {
                try {
                    while (true) {
                        String receivedText = inputStream.readUTF();
                        // Display received messages in msgLabel
                        msgLabel = new JLabel(receivedText);
                        msgPanel.add(msgLabel);
                        msgPanel.revalidate();
                        msgPanel.repaint();
                        scrollToBottom(messageScrollPane);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Send button action listener
       /* sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendTextToServer(inputTextField.getText());
                String msg = "Sent:" + inputTextField.getText();
                // Display sent messages in msgLabel
                msgLabel = new JLabel(msg);
                msgPanel.add(msgLabel);
                msgPanel.revalidate();
                msgPanel.repaint();
                inputTextField.setText("");
                scrollToBottom(messageScrollPane);
            }
        });*/

        msgPanel.setLayout(new BoxLayout(msgPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(msgPanel);
        messageFrame.add(scrollPane, BorderLayout.CENTER);
        messageFrame.add(bottomPanel, BorderLayout.SOUTH);

        // Handle closing of the message frame
        // Handle closing of the message frame
        messageFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                openMessageFrames.remove(recipient);
                e.getWindow().dispose();
                initializeApp(clientName);
            }
        });

    }

    private void sendTextToServer(String msg) {
        try {
            String recipient = Receiver;
            String messageText = msg;
            if (!messageText.equals("")) {
                String text = recipient + ":" + messageText;
                outputStream.writeUTF(text);
                outputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void scrollToBottom(JScrollPane scrollPane) {
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setValue(verticalScrollBar.getMaximum());
    }
}
