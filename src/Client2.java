import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

public class Client2 {
    public static JTextArea textArea = new JTextArea();
    public static Socket clientSocket;
    public static DataOutputStream outputStream;

    public static void main(String[] args) {
        JFrame frame = new JFrame();

        frame.setTitle("Google Docs Inspired Real-Time Text Editor");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(textArea);

        JPanel buttonPanel = new JPanel();

        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

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
                    int messageLength = inputStream.readInt();
                    byte[] buffer = new byte[messageLength];
                    inputStream.readFully(buffer);

                    String receivedText = new String(buffer);
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
                outputStream.writeInt(text.length());
                outputStream.writeBytes(text);
                outputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
