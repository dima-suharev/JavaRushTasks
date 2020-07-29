package com.javarush.games.minesweeper;
import com.javarush.engine.cell.*;

public class GameObject extends Game {
    public int x;
    public int y;
    public boolean isMine;
    public int countMineNeighbors = 0;
    public boolean isOpen = false;
    public boolean isFlag = false;

    public GameObject (int x, int y, boolean isMine) {
        this.x = x;
        this.y = y;
        this.isMine = isMine;
    }

}

