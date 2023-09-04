import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.sql.*;

public class Dummy1 {
    public static JTextArea textArea = new JTextArea();
    public static JPanel messagePanel = new JPanel();
    public static JScrollPane messageScrollPane = new JScrollPane(messagePanel);
    public static Socket clientSocket;
    public static DataOutputStream outputStream;
    public static JFrame frame = new JFrame();
    public static String Receiver=null;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Login obj = new Login();
            obj.render();
        });
    }

    public void initializeApp(String LoginName) {
        String LoginN=LoginName;
        JPanel navigationBar = new JPanel();
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu viewMenu = new JMenu("View");
        JMenu editMenu = new JMenu("Edit");
        JMenuItem saveMenu = new JMenuItem("Save");
        JMenuItem loadMenu = new JMenuItem("Load");
        JMenuItem exitMenu = new JMenuItem("Exit");
        JPanel leftPanel = new JPanel();
        JPanel rightPanel = new JPanel();
        JButton sendButton = new JButton("Send");
        JButton chooseFileButton = new JButton("Choose File");

        frame.setSize(500, 500);
        frame.setLayout(new BorderLayout(5, 5));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setTitle("Chat App");
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));

        // Navigation bar
        navigationBar.setBackground(Color.lightGray);
        navigationBar.setPreferredSize(new Dimension(100, 40));
        navigationBar.setLayout(new BorderLayout());
        frame.getContentPane().add(navigationBar, BorderLayout.NORTH);

        // Menu options
        fileMenu.add(saveMenu);
        fileMenu.add(loadMenu);
        fileMenu.add(exitMenu);
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(viewMenu);
        navigationBar.add(menuBar);
        menuBar.add(sendButton);
        menuBar.add(chooseFileButton);

        // Adding shortcut keys
        fileMenu.setMnemonic(KeyEvent.VK_F); // Press Alt+F for file menu
        exitMenu.setMnemonic(KeyEvent.VK_E); // Press Alt+F and then Alt+E to exit directly
        saveMenu.setMnemonic(KeyEvent.VK_S); // Press Alt+F and then Alt+S to save the file
        loadMenu.setMnemonic(KeyEvent.VK_L); // Press Alt+F and then Alt+L to load the saved data

        // Left panel
        leftPanel.setBackground(Color.lightGray);
        leftPanel.setPreferredSize(new Dimension(130, 100));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.revalidate();
        leftPanel.repaint();
        JScrollPane leftScrollPane = new JScrollPane();
        leftScrollPane.getVerticalScrollBar().setValue(leftScrollPane.getVerticalScrollBar().getMaximum());

        /*JLabel person0 = new JLabel("Shyam");
        leftPanel.add(person0);
        frame.add(leftPanel, BorderLayout.WEST);
        person0.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Receiver = person0.getText();
            }
        });

        JLabel person1 = new JLabel("Hari");
        leftPanel.add(person1);
        frame.add(leftPanel, BorderLayout.WEST);
        person1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Receiver = person1.getText();
            }
        });*/

        try {
            Login login=new Login();
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost/java_db";
            Connection conn = DriverManager.getConnection(url, "root", "Joker1245780");
            System.out.println("Connected to the database");
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("select * from Login");
            while (rs.next()) {
                String name = rs.getString("UserName");
                if(name.equals(LoginName))
                {
                    continue;
                }
                JLabel person1 = new JLabel(name);
                leftPanel.add(person1);
                frame.add(leftPanel, BorderLayout.WEST);
                person1.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        Receiver = person1.getText();
                    }
                });
            }
        } catch (ClassNotFoundException ex) {
            // Handle exceptions
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }


        // Right panel
        rightPanel.setBackground(Color.lightGray);
        rightPanel.setPreferredSize(new Dimension(130, 100));
        frame.add(rightPanel, BorderLayout.EAST);

        // Create a scroll pane for the message area
        messageScrollPane.setPreferredSize(new Dimension(10, 100));
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));

        // Add the message scroll pane to the frame at the top
        frame.add(messageScrollPane, BorderLayout.CENTER);

        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(10, 20));
        frame.add(scrollPane, BorderLayout.SOUTH);

        // Connection
        try {
            clientSocket = new Socket("localhost", 12345);
            outputStream = new DataOutputStream(clientSocket.getOutputStream());
            /*Login login=new Login();
            String name = login.userName;*/
            String name="Ram";
            outputStream.writeUTF(name);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendTextToServer();
            }
        });

        // Start a thread to read from the server
        new Thread(() -> {
            try {
                DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());

                while (true) {
                    String receivedText = inputStream.readUTF();
                    String text = "Received:" + receivedText;
                    JLabel label = new JLabel(text);
                    messagePanel.add(label);
                    messagePanel.revalidate();
                    messagePanel.repaint();
                    messageScrollPane.getVerticalScrollBar().setValue(messageScrollPane.getVerticalScrollBar().getMaximum());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        frame.setVisible(true);
    }

    private static void sendTextToServer() {
        try {
            String recipient = Receiver;
            String messageText = textArea.getText();
            if (!messageText.equals("")) {
                String text = recipient + ":" + messageText;
                JLabel label = new JLabel(text);
                messagePanel.add(label);
                messagePanel.revalidate();
                messagePanel.repaint();
                messageScrollPane.getVerticalScrollBar().setValue(messageScrollPane.getVerticalScrollBar().getMaximum());
                outputStream.writeUTF(text);
                outputStream.flush();
                textArea.setText("");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
