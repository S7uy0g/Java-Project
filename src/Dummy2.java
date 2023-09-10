import javax.swing.*;

public class Dummy2 {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            Login obj = new Login();
            obj.render();
        });
    }
}
