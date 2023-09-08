import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;

public class Dummy2 {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Login obj = new Login();
            obj.render();
        });
    }
}
