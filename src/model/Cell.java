package model;

public class Cell {

    private boolean isBomb = false;
    private boolean isPressed = false;
    private int numOfNeighborBombs = 0;

    public boolean isBomb() {
        return isBomb;
    }

    public void setBomb(boolean bomb) {
        isBomb = bomb;
    }

    public boolean isPressed() {
        return isPressed;
    }

    public void setPressed(boolean pressed) {
        isPressed = pressed;
    }

    public int getNumOfNeighborBombs() {
        return numOfNeighborBombs;
    }

    public void setNumOfNeighborBombs(int numOfNeighborBombs) {
        this.numOfNeighborBombs = numOfNeighborBombs;
    }
}
