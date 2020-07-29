package com.javarush.games.snake;
import com.javarush.engine.cell.*;

public class SnakeGame extends Game{

    public static final int HEIGHT = 15;
    public static final int WIDTH = 15;
    private Snake snake;
    private int turnDelay;
    private Apple apple;
    private boolean isGameStopped;
    private static final int GOAL = 28;
    private int score;

    public void initialize() {
        setScreenSize(WIDTH, HEIGHT);
        createGame();
    }

    private void createGame() {
        snake = new Snake(WIDTH/2, HEIGHT/2);
        createNewApple();
        isGameStopped = false;
        drawScene();
        turnDelay = 300;
        setTurnTimer(turnDelay);
        score = 0; setScore(score);
    }

    private void drawScene() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                setCellValueEx(x, y, Color.DARKSEAGREEN, "");
            }
        }
        snake.draw(this);
        apple.draw(this);
    }

    private void createNewApple() {
        do {
            int x = getRandomNumber(WIDTH-1);
            int y = getRandomNumber(HEIGHT-1);
            apple = new Apple(x, y);
        } while (snake.checkCollision(apple));

    }

    private void gameOver() {
        stopTurnTimer();
        isGameStopped = true;
        showMessageDialog(Color.NONE, "GAME OVER", Color.ORANGE, 70);
    }

    private void win() {
        stopTurnTimer();
        isGameStopped = true;
        showMessageDialog(Color.NONE, "YOU WIN", Color.ORANGE, 70);
    }

    @Override
    public void onTurn(int step) {
        snake.move(apple);
        if (!apple.isAlive) {
            createNewApple();
            score += 5; setScore(score);
            turnDelay -= 10;
            setTurnTimer(turnDelay);
        }
        if (!snake.isAlive) gameOver();
        else if (snake.getLength()>GOAL) win();
        drawScene();
    }

    @Override
    public void onKeyPress(Key key) {
        Direction dir = null;
        switch (key) {
            case LEFT: {dir = Direction.LEFT; break;}
            case UP: {dir = Direction.UP; break;}
            case RIGHT: {dir = Direction.RIGHT; break;}
            case DOWN: {dir = Direction.DOWN; break;}
            case SPACE: {if (isGameStopped) createGame(); return;}
        }
        snake.setDirection(dir);
    }
}
