import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ClentDuo {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Real-Time Text Receiver");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JTextArea textArea = new JTextArea();
        frame.add(new JScrollPane(textArea));
        frame.setSize(400, 300);
        frame.setVisible(true);

        try {
            Socket clientSocket = new Socket("localhost", 12345); // Use the same server IP and port
            InputStream inputStream = clientSocket.getInputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                String receivedText = new String(buffer, 0, bytesRead);
                textArea.setText(receivedText);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
