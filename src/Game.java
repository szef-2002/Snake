import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

//Class for game logic
public class Game extends JPanel implements KeyListener, Runnable {

    private final Board board = new Board();
    private Snake snake = new Snake();
    private Fruit fruit = new Fruit();
    public boolean end = false;
    private int score = 0;
    private final ScoreManager scoreManager;
    private int speed = 100;

    public Game() {
        setPreferredSize(new Dimension(500, 500));

        scoreManager = new ScoreManager();

        //Starting the game
        new Thread(this).start();
        MyFrame.score.setText("Your Score: " + snake.getFruitsEaten());

        setFocusable(true);
        addKeyListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Board.drawBoard(g);
        snake.drawSnake(g);
        fruit.drawFruit(g);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                if (snake.getDirection() != Direction.DOWN) {
                    snake.setDirection(Direction.UP);
                }
                break;
            case KeyEvent.VK_DOWN:
                if (snake.getDirection() != Direction.UP) {
                    snake.setDirection(Direction.DOWN);
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (snake.getDirection() != Direction.LEFT) {
                    snake.setDirection(Direction.RIGHT);
                }
                break;
            case KeyEvent.VK_LEFT:
                if (snake.getDirection() != Direction.RIGHT) {
                    snake.setDirection(Direction.LEFT);
                }
                break;
            case KeyEvent.VK_R:
                reset();
                break;
            case KeyEvent.VK_S:
                faster();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            if (!end) {
                MyFrame.bestScore.setText("Best Score is: " + scoreManager.getHighScore());
                snake.moveSnake();
                if (snake.eating(fruit)) {
                    fruit = new Fruit();
                    score++;
                    if (score % 3 == 0) {
                        faster();
                    }
                }

                if (snake.deadSnake()) {
                    end = true;
                }

                repaint();
                scoreManager.updateHighScore(score);
            }
            try {
                Thread.sleep(speed);
            } catch (InterruptedException e) {
                throw new RuntimeException("Thread was interrupted");
            }
        }
    }

    public void reset() {
        end = false;
        snake = new Snake();
        fruit = new Fruit();
        repaint();
        score = 0;
        speed = 100;
    }

    public void faster() {
        if (speed > 20) {
            speed -= 20;
        }
    }
}
