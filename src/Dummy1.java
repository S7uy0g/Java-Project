import javax.swing.*;

public class Dummy1 {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Login obj = new Login();
            obj.render();
        });
    }
}