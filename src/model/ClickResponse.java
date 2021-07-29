package model;

public class ClickResponse {

    private final boolean isBombClicked;
    private final boolean isAlreadyCellClicked;

    public ClickResponse(boolean isBombClicked, boolean isAlreadyCellClicked) {
        this.isBombClicked = isBombClicked;
        this.isAlreadyCellClicked = isAlreadyCellClicked;
    }

    public boolean isBombClicked() {
        return isBombClicked;
    }

    public boolean isAlreadyCellClicked() {
        return isAlreadyCellClicked;
    }
}
