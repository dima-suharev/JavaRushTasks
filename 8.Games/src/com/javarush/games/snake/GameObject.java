package com.javarush.games.snake;

import com.javarush.engine.cell.Game;

public class GameObject extends Game {
    public int x;
    public int y;

    public GameObject (int x, int y) {
        this.x = x;
        this.y = y;
    }
}
