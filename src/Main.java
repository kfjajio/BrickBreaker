import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BrickBreakerGame game = new BrickBreakerGame();
            game.setVisible(true);
        });
    }
}