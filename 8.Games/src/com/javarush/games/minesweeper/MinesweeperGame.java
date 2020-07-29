package com.javarush.games.minesweeper;

import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;

import java.util.ArrayList;
import java.util.List;

public class MinesweeperGame extends Game {
    private static final int SIDE = 9;
    private GameObject[][] gameField = new GameObject[SIDE][SIDE];
    private int countMinesOnField;
    private int countClosedTiles = SIDE*SIDE;
    private int countFlags;
    private int score = 0;
    private static final String MINE = "\uD83D\uDCA3";
    private static final String FLAG = "\uD83D\uDEA9";
    private boolean isGameStopped;

    @Override
    public void initialize() {
        setScreenSize(SIDE, SIDE);
        createGame();
    }

    private void createGame() {
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                boolean isMine = getRandomNumber(10) < 1;
                if (isMine) {
                    countMinesOnField++;
                }
                gameField[y][x] = new GameObject(x, y, isMine);
                setCellColor(x, y, Color.ORANGE);
                setCellValue(x, y, "");
            }
        }
        countFlags = countMinesOnField;
        countMineNeighbors();
    }

    private void countMineNeighbors() {
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                if (!gameField[y][x].isMine) {
                    for (GameObject object: getNeighbors(gameField[y][x])) {
                        if (object.isMine) gameField[y][x].countMineNeighbors++;
                    }
                }

            }
        }
    }

    private List<GameObject> getNeighbors(GameObject gameObject) {
        List<GameObject> result = new ArrayList<>();
        for (int y = gameObject.y - 1; y <= gameObject.y + 1; y++) {
            for (int x = gameObject.x - 1; x <= gameObject.x + 1; x++) {
                if (y < 0 || y >= SIDE) continue;
                if (x < 0 || x >= SIDE) continue;
                if (gameField[y][x] == gameObject) continue;
                result.add(gameField[y][x]);
            }
        }
        return result;
    }

    private void openTile(int x, int y) {
        GameObject obj = gameField[y][x];
        if (obj.isOpen || obj.isFlag || isGameStopped) return;
        if (obj.isMine) {
            if (countClosedTiles == SIDE*SIDE) {
                restart();
                openTile(x, y);
            }
            else {
                setCellValueEx(x, y, Color.RED, MINE);
                gameOver();
            }
        }
        else {
            obj.isOpen = true;
            countClosedTiles--;
            score+=5; setScore(score);
            setCellColor(x, y, Color.GREEN);
            if (countClosedTiles==countMinesOnField && !obj.isMine) win();
            if (obj.countMineNeighbors != 0) setCellNumber(x, y, obj.countMineNeighbors);
            else {
                setCellValue(x, y, "");
                for (GameObject object: getNeighbors(obj)) {
                    if (!object.isOpen) openTile(object.x, object.y);
                }
            }
        }
    }

    private void markTile(int x, int y) {
        GameObject obj = gameField[y][x];
        if (isGameStopped || obj.isOpen) return;
        if (!obj.isFlag && countFlags != 0) {
            obj.isFlag = true;
            countFlags--;
            setCellValue(x, y, FLAG);
            setCellColor(x, y, Color.YELLOW);
        }
        else if (obj.isFlag) {
            obj.isFlag = false;
            countFlags++;
            setCellValue(x, y, "");
            setCellColor(x, y, Color.ORANGE);
        }
    }

    private void gameOver() {
        isGameStopped = true;
        showMessageDialog(Color.RED, "Game over!", Color.WHITE, 20);
    }

    private void win() {
        isGameStopped = true;
        showMessageDialog(Color.BEIGE, "You won! Congrats!", Color.BLUE, 20);
    }

    private void restart() {
        isGameStopped = false;
        countClosedTiles = SIDE*SIDE;
        countMinesOnField = 0;
        score = 0; setScore(score);
        createGame();

    }

    @Override
    public void onMouseLeftClick(int x, int y) {
        if (!isGameStopped) openTile(x, y);
        else restart();
    }

    @Override
    public void onMouseRightClick(int x, int y) {
        markTile(x, y);
    }
}