package model;

public class BoardParameter {

    private int heightCell = 0;
    private int widthCell = 0;
    private int numberOfBombs = 0;

    public int getHeightCell() {
        return heightCell;
    }

    public void setHeightCell(int heightCell) {
        this.heightCell = heightCell;
    }

    public int getWidthCell() {
        return widthCell;
    }

    public void setWidthCell(int widthCell) {
        this.widthCell = widthCell;
    }

    public int getNumberOfBombs() {
        return numberOfBombs;
    }

    public void setNumberOfBombs(int numberOfBombs) {
        this.numberOfBombs = numberOfBombs;
    }
}
