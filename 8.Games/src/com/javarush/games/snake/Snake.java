package com.javarush.games.snake;

import com.javarush.engine.cell.*;

import java.util.ArrayList;
import java.util.List;

public class Snake {

    private List<GameObject> snakeParts = new ArrayList<GameObject>();
    private static final String HEAD_SIGN = "\uD83D\uDC7E";
    private static final String BODY_SIGN = "\u26AB";
    public boolean isAlive = true;
    private Direction direction = Direction.LEFT;


    public Snake(int x, int y) {
        GameObject first = new GameObject(x, y);
        GameObject second = new GameObject(x+1, y);
        GameObject third = new GameObject(x+2, y);
        snakeParts.add(first);
        snakeParts.add(second);
        snakeParts.add(third);
    }

    public void draw(Game game) {
        Color color = Color.BLACK;
        if (!isAlive) color = Color.RED;
        for (int i=0; i<snakeParts.size(); i++) {
            GameObject part = snakeParts.get(i);
            if (i!=0) game.setCellValueEx(part.x, part.y, Color.NONE, BODY_SIGN, color, 75);
            else game.setCellValueEx(part.x, part.y, Color.NONE, HEAD_SIGN, color, 75);
        }
    }

    public void setDirection(Direction direction) {
        if ((this.direction == Direction.LEFT && direction == Direction.RIGHT) ||
                (this.direction == Direction.RIGHT && direction == Direction.LEFT) ||
                (this.direction == Direction.UP && direction == Direction.DOWN) ||
                (this.direction == Direction.DOWN && direction == Direction.UP) ||
                (this.direction == Direction.LEFT && snakeParts.get(0).x == snakeParts.get(1).x) ||
                (this.direction == Direction.RIGHT && snakeParts.get(0).x == snakeParts.get(1).x) ||
                (this.direction == Direction.UP && snakeParts.get(0).y == snakeParts.get(1).y) ||
                (this.direction == Direction.DOWN && snakeParts.get(0).y == snakeParts.get(1).y)) return;
        else this.direction = direction;
    }

    public void move(Apple apple) {
        GameObject newHead = createNewHead();
        if (newHead.x > SnakeGame.WIDTH-1 || newHead.y > SnakeGame.HEIGHT-1 || newHead.x < 0 || newHead.y < 0) isAlive = false;
        else if (checkCollision(newHead)) isAlive = false;
        else {
            snakeParts.add(0, newHead);
            if (apple.x == newHead.x && apple.y == newHead.y) apple.isAlive = false;
            else removeTail();
        }
    }

    public GameObject createNewHead() {
        int a=snakeParts.get(0).x, b = snakeParts.get(0).y;
        switch (direction) {
            case LEFT: { a-=1; break;}
            case UP: {b-=1; break;}
            case RIGHT: {a+=1; break;}
            case DOWN: {b+=1; break;}
        }
        GameObject newHead = new GameObject(a, b);
        return newHead;
    }

    public void removeTail() {
        snakeParts.remove(snakeParts.size()-1);
    }

    public boolean checkCollision(GameObject game) {
        for (GameObject parts: snakeParts) if (game.x == parts.x && game.y == parts.y) return true;
        return false;
    }

    public int getLength() {
        return snakeParts.size();
    }
}
