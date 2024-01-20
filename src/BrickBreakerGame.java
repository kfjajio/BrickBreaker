import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

public class BrickBreakerGame extends JFrame implements ActionListener, KeyListener {

    private Timer timer;
    private int ballX, ballY, ballSpeedX, ballSpeedY;
    private int paddleX, paddleSpeed;
    private boolean[][] bricks;
    private int score;
    private int currentLevel;
    private BufferedImage brickTexture;

    public BrickBreakerGame() {
        setTitle("Brick Breaker Game");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            brickTexture = ImageIO.read(new URL("https://3djungle.ru/upload/resize_cache/iblock/a41/200_200_2/a414588fd463c450f26bd3b886f0d1bf.jpg")); // Загрузка текстуры кирпича
        } catch (IOException e) {
            e.printStackTrace();
        }

        initializeGame();

        timer = new Timer(10, this);
        timer.start();

        addKeyListener(this);
        setFocusable(true);
    }

    private void initializeGame() {
        bricks = new boolean[5][6]; // 5 уровней, каждый с 6 кирпичами
        for (int i = 0; i < bricks.length; i++) {
            for (int j = 0; j < bricks[i].length; j++) {
                bricks[i][j] = true;
            }
        }

        ballX = 200;
        ballY = 100;
        ballSpeedX = 2;
        ballSpeedY = 2;

        paddleX = 150;
        paddleSpeed = 25;

        score = 0;
        currentLevel = 0;
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;

        // Отрисовка уровня
        for (int i = 0; i < bricks.length; i++) {
            for (int j = 0; j < bricks[i].length; j++) {
                if (bricks[i][j]) {
                    g2d.drawImage(brickTexture, j * 65 + 5, i * 20 + 5, 60, 15, null);
                }
            }
        }

        // Отрисовка мяча
        g2d.fillOval(ballX, ballY, 20, 20);

        // Отрисовка платформы
        g2d.fillRect(paddleX, 350, 60, 10);

        // Отрисовка счета
        g2d.drawString("Score: " + score, 10, 380);

        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

    public void actionPerformed(ActionEvent e) {
        ballX += ballSpeedX;
        ballY += ballSpeedY;

        // Обработка столкновений с границами
        if (ballX <= 0 || ballX >= getWidth() - 20) {
            ballSpeedX = -ballSpeedX;
        }
        if (ballY <= 0) {
            ballSpeedY = -ballSpeedY;
        }

        // Обработка столкновений с платформой
        if (ballY >= 340 && ballY <= 360 && ballX >= paddleX && ballX <= paddleX + 60) {
            ballSpeedY = -ballSpeedY;

        }

        // Обработка столкновений с кирпичами
        for (int i = 0; i < bricks.length; i++) {
            for (int j = 0; j < bricks[i].length; j++) {
                if (bricks[i][j] && ballX + 20 >= j * 65 + 5 && ballX <= j * 65 + 65
                        && ballY + 20 >= i * 20 + 5 && ballY <= i * 20 + 20 + 5) {
                    ballSpeedY = -ballSpeedY;
                    bricks[i][j] = false;
                    score += 10;
                }
            }
        }

        // Проверка завершения уровня
        if (score % 60 == 0 && score != 0) {
            currentLevel++;
            if (currentLevel < bricks.length) {
                initializeGame();
            } else {
                timer.stop();
                JOptionPane.showMessageDialog(this, "Congratulations! You completed all levels. Your final score: " + score);
                System.exit(0);
            }
        }

        // Обработка завершения игры
        if (ballY >= getHeight()) {
            timer.stop();
            JOptionPane.showMessageDialog(this, "Game Over! Your Score: " + score);
            System.exit(0);
        }

        repaint();
    }

    public void keyTyped(KeyEvent e) {}

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT && paddleX > 0) {
            paddleX -= paddleSpeed;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && paddleX < getWidth() - 60) {
            paddleX += paddleSpeed;
        }
    }

    public void keyReleased(KeyEvent e) {}


}
