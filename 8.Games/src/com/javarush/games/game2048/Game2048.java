package com.javarush.games.game2048;

import com.javarush.engine.cell.*;

public class Game2048 extends Game {

    private static final int SIDE = 4;
    private int[][] gameField = new int[SIDE][SIDE];
    private boolean isGameStopped = false;
    private int score = 0;
    private static final String joker = "\u2606";
    private static final int countNewNumbers = 1;
    private boolean useJOKER = true;

    public void initialize() {
        setScreenSize(SIDE, SIDE);
        createGame();
        drawScene();
    }

    private void createGame() {
        for (int i=0; i<SIDE; i++) for (int j=0; j < SIDE; j++) gameField[i][j]=0;
        score = 0; setScore(score);
        createNewNumber();
        createNewNumber();
    }

    private void drawScene() {
        for (int i=0; i<SIDE; i++)
            for (int j = 0; j < SIDE; j++)
                setCellColoredNumber(i, j, gameField[j][i]);
    }

    private void createNewNumber() {
        int x, y;
        do {
            x = getRandomNumber(SIDE);
            y = getRandomNumber(SIDE);
        } while (gameField[y][x] != 0);

        if (useJOKER) {
            if (getRandomNumber(10) == 9)
                gameField[y][x] = 2;
            else if (getRandomNumber(10) == 8)
                gameField[y][x] = 100000;
            else gameField[y][x] = 1;
        }
        else {
            if (getRandomNumber(10) == 9)
                gameField[y][x] = 2;
            else gameField[y][x] = 1;
        }

        if (getMaxTileValue() == 144) win();
    }

    private Color getColorByValue(int value) {
        switch (value) {
            case 0: return Color.WHITE;
            case 1: return Color.LIGHTPINK;
            case 2: return Color.BLUEVIOLET;
            case 3: return Color.BLUE;
            case 5: return Color.CYAN;
            case 8: return Color.LIGHTSEAGREEN;
            case 13: return Color.LIMEGREEN;
            case 21: return Color.ORANGE;
            case 34: return Color.INDIANRED;
            case 55: return Color.RED;
            case 89: return Color.MAGENTA;
            case 144: return Color.MEDIUMVIOLETRED;
            case 100000: return Color.YELLOW;
        }
        return Color.GREEN;
    }

    private void setCellColoredNumber(int x, int y, int value) {
        String string = String.valueOf(value);
        if (value == 0) string = "";
        else if (value == 100000) string = joker;
        setCellValueEx(x, y, getColorByValue(value), string);
    }

    private boolean compressRow(int[] row) {
        boolean compress = false;
        int p = 0;
        for (int i=0; i<row.length-1; i++) {
            if (row[i]!=0 && row[i+1]==0) p = i+1;
            else if (row[i]==0 && row[i+1]!=0) {
                row[p] = row[i+1];
                row[i+1] = 0;
                p++;
                compress = true;
            }
        }
        return compress;
    }

    private boolean mergeRow(int[] row) {
        boolean merge = false;

        if (row[0]==100000 && row[1]!=0) row[0] = row[1];
        for (int i=0; i<row.length-1; i++) {
            if (row[i+1]==100000 && row[i]!=0) row[i+1] = row[i];
            if (row[i]==row[i+1] && row[i]!=0) {
                row[i] = nextFibonacci(row[i]);
                row[i+1] = 0;
                score += row[i]; setScore(score);
                merge = true;
                i++;
            }
        }
        return merge;
    }

    private int nextFibonacci(int n) {
        int a=1, b=2, temp = 1;
        while (a<=n) {
            temp = b;
            b = b + a;
            a = temp;
        }
        return a;
    }

    private void moveLeft() {
        boolean a = false, b = false, c = false, n = false;
        for (int i=0; i<SIDE; i++) {
            a = compressRow(gameField[i]);
            b = mergeRow(gameField[i]);
            c = compressRow(gameField[i]);
            if (a || b || c) n=true;
        }
        if (n) for (int i=0; i<countNewNumbers; i++) createNewNumber();
    }

    private void moveRight() {
        rotateClockwise(); rotateClockwise();
        moveLeft();
        rotateClockwise(); rotateClockwise();
    }

    private void moveUp() {
        rotateClockwise(); rotateClockwise(); rotateClockwise();
        moveLeft();
        rotateClockwise();
    }

    private void moveDown() {
        rotateClockwise();
        moveLeft();
        rotateClockwise(); rotateClockwise(); rotateClockwise();
    }

    private void rotateClockwise() {
        int[][] result = new int[SIDE][SIDE];
        for (int i=0; i<SIDE; i++) {
            for (int j=0; j<SIDE; j++) {
                result[i][j] = gameField[SIDE-1-j][i];
            }
        }
        gameField = result;
    }

    @Override
    public void onKeyPress(Key key) {
        if (isGameStopped) {
            if (key == Key.SPACE) {
                isGameStopped = false;
                createGame();
                drawScene();
            }
            else return;
        }
        else {
            if (!canUserMove()) gameOver();
            else {
                if (key == Key.LEFT) moveLeft();
                else if (key == Key.RIGHT) moveRight();
                else if (key == Key.UP) moveUp();
                else if (key == Key.DOWN) moveDown();
                if (key == Key.DOWN || key == Key.UP || key == Key.LEFT || key == Key.RIGHT) drawScene();
            }
        }
    }

    private int getMaxTileValue() {
        int max = 0;
        for (int i=0; i<SIDE; i++)
            for (int j=0; j < SIDE; j++)
                if (gameField[i][j] > max) max = gameField[i][j];
        return max;
    }

    private void win() {
        isGameStopped = true;
        showMessageDialog(Color.YELLOW, "YOU WON! Congrats!", Color.BLUE, 20);
    }

    private boolean canUserMove() {
        boolean result = false;
        for (int i=0; i < SIDE; i++)
            for (int j=0; j < SIDE; j++)
                if (gameField[i][j] == 0) {
                    result = true;
                    break;
                }
        if (!result) {
            for (int i = 0; i < SIDE; i++)
                for (int j = 0; j < SIDE-1; j++) {
                    if (gameField[i][j] == gameField[i][j+1]) {
                        result = true;
                        break;
                    }
                }
        }
        if (!result) {
            rotateClockwise();
            for (int i = 0; i < SIDE; i++)
                for (int j = 0; j < SIDE-1; j++) {
                    if (gameField[i][j] == gameField[i][j+1]) {
                        result = true;
                        break;
                    }
                }
            rotateClockwise(); rotateClockwise(); rotateClockwise();
        }
        return result;
    }

    private void gameOver() {
        isGameStopped = true;
        showMessageDialog(Color.YELLOW, "YOU LOST! Try again!", Color.BLUE, 20);
    }

}
