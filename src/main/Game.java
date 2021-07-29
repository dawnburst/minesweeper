package main;

import model.BoardParameter;
import model.Cell;
import model.ClickResponse;
import model.Coordinate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Game {

    public static void main(String[] args) {

        BoardParameter boardParameters = getBoardParameters();
        Cell[][] board = new Cell[boardParameters.getHeightCell()][boardParameters.getWidthCell()];
        int numberOfNotBombCells = initBoard(board, boardParameters.getNumberOfBombs());
        startGame(board, numberOfNotBombCells);
    }

    private static BoardParameter getBoardParameters() {

        Scanner in = new Scanner(System.in);
        BoardParameter boardParameter = new BoardParameter();

        boolean isInitParamsValid = false;

        while (!isInitParamsValid) {

            System.out.println("Lets create the board.\nPlease enter Height, Width and Number of Bombs.\nFor Example 10 8 30");

            boardParameter.setHeightCell(in.nextInt());
            boardParameter.setWidthCell(in.nextInt());
            boardParameter.setNumberOfBombs(in.nextInt());

            isInitParamsValid = validateParams(boardParameter.getHeightCell(), boardParameter.getWidthCell(), boardParameter.getNumberOfBombs());
            if (!isInitParamsValid) {
                System.out.println("Something with your parameters are wrong. Please enter valid parameters");
            }
        }

        System.out.println("heightCell=" + boardParameter.getHeightCell());
        System.out.println("widthCell=" + boardParameter.getWidthCell());
        System.out.println("numberOfBombs=" + boardParameter.getNumberOfBombs());

        return boardParameter;
    }

    private static boolean validateParams(int heightCell, int widthCell, int numberOfBombs) {

        return (heightCell > 0 && widthCell > 0 && numberOfBombs > 0 && numberOfBombs < heightCell * widthCell);
    }

    private static void startGame(Cell[][] board, int numberOfNotBombCells) {

        int totalCellsReveal = 0;

        boolean isClickedOnABomb = false;

        while (!isClickedOnABomb && numberOfNotBombCells != totalCellsReveal) {

            ClickResponse clickResponse = clickCell(board);

            isClickedOnABomb = clickResponse.isBombClicked();
            printBoard(board, true);
            if (isClickedOnABomb) {
                System.out.println("You clicked on a bomb. You lost :(");
                printBoard(board, false);
            }
            else {
                if (!clickResponse.isAlreadyCellClicked()) {
                    totalCellsReveal++;

                    if (numberOfNotBombCells == totalCellsReveal) {
                        System.out.println("You revealed all cells. You win!!");
                        printBoard(board, false);
                    }
                }
            }
        }
    }


    private static int initBoard(Cell[][] board, int numberOfBombs) {

        createCells(board);
        randomBombs(board, numberOfBombs);
        calcNeighborsBombs(board);
        printBoard(board, true);

        // Calculate the number of cells that are not bombs
        return (board.length * board[0].length) - numberOfBombs;
    }

    private static void calcNeighborsBombs(Cell[][] board) {
        int rows = board.length;

        for (int i = 0; i < rows; i++) {

            int cols = board[0].length;
            for (int j = 0; j < cols; j++) {

                if (board[i][j].isBomb()) {
                    board[i][j].setNumOfNeighborBombs(-1);
                } else {

                    // Loop to check all neighbors (9 cells)
                    int totalBombs = 0;
                    for (int offsetI = -1; offsetI <= 1; offsetI++) {
                        for (int offsetJ = -1; offsetJ <= 1; offsetJ++) {

                            //Check if the neighbors are in the board limit
                            if (offsetI + i > -1 && offsetI + i < rows && offsetJ + j > -1 && offsetJ + j < cols) {
                                if (board[offsetI + i][offsetJ + j].isBomb()) {
                                    totalBombs++;
                                }
                            }
                        }
                    }
                    board[i][j].setNumOfNeighborBombs(totalBombs);
                }
            }
        }
    }

    private static void randomBombs(Cell[][] board, int numberOfBombs) {

        List<Coordinate> allCoordinatesOptions = new ArrayList<>();

        // Initiate all coordinate to array list
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {

                allCoordinatesOptions.add(new Coordinate(i, j));
            }
        }

        Random random = new Random();

        while (numberOfBombs > 0) {

            int randomCoordinateNumber = random.nextInt(allCoordinatesOptions.size());

            board[allCoordinatesOptions.get(randomCoordinateNumber).getX()][allCoordinatesOptions.get(randomCoordinateNumber).getY()].setBomb(true);
                numberOfBombs--;
            allCoordinatesOptions.remove(randomCoordinateNumber);
        }
    }

    private static void createCells(Cell[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {

                board[i][j] = new Cell();
            }
        }
    }

    private static void printBoard(Cell[][] board, boolean hideCells) {

        System.out.print("     ");
        for (int i = 0; i < board[0].length; i++) {

            System.out.print("  " + i + "  ");
        }

        System.out.println();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {

                if (j == 0) {
                    System.out.print("  " + i + "  ");
                }

                if (!board[i][j].isPressed() && hideCells) {
                    System.out.print(" [░] ");
                } else {
                    if (board[i][j].isBomb()) {
                        System.out.print(" [X] ");
                    } else {
                        if (board[i][j].getNumOfNeighborBombs() > 0) {
                            System.out.print(" [" + board[i][j].getNumOfNeighborBombs() + "] ");
                        }
                        else {
                            System.out.print(" [ ] ");
                        }
                    }
                }
            }
            System.out.print("\n");
        }
    }

    private static ClickResponse clickCell(Cell[][] board) {

        Scanner in = new Scanner(System.in);
        boolean isClickedParamValid = false;
        int heightCell = 0;
        int widthCell = 0;

        while(!isClickedParamValid) {
            System.out.println("Please enter coordinate of cell you want to click. for example 0 1 for height=0 and width=1");

            heightCell = in.nextInt();
            widthCell = in.nextInt();

            isClickedParamValid = validateClickedParams(heightCell, widthCell, board.length, board[0].length);
            if (!isClickedParamValid) {
                System.out.println("Something wrong with coordinates. Please enter valid coordinates");
            }
        }

        if (board[heightCell][widthCell].isPressed()) {
            return(new ClickResponse(false, true));
        }

        revealCell(board, heightCell, widthCell);

        return(new ClickResponse(board[heightCell][widthCell].isBomb(), false));
    }

    private static boolean validateClickedParams(int heightCell, int widthCell, int maxRows, int maxCols) {
        return heightCell >= 0 && heightCell <= maxRows && widthCell >= 0 && widthCell <= maxCols;
    }


    private static void revealCell(Cell[][] board, int heightCell, int widthCell) {

        board[heightCell][widthCell].setPressed(true);

        if (board[heightCell][widthCell].getNumOfNeighborBombs() == 0) {
            revealNeighbors(board, heightCell, widthCell);
        }
    }

    private static void revealNeighbors(Cell[][] board, int i, int j) {
        int rows = board.length;
        int cols = board[0].length;

        for (int offsetI = -1; offsetI <= 1; offsetI++) {
            for (int offsetJ = -1; offsetJ <= 1; offsetJ++) {

                //Check if the neighbors are in the board limit
                int offI = offsetI + i;
                int offJ = offsetJ + j;

                if (offI > -1 && offI < rows && offJ > -1 && offJ < cols) {

                    Cell neighborCell = board[offI][offJ];
                    if (!neighborCell.isBomb() && !neighborCell.isPressed() && neighborCell.getNumOfNeighborBombs() == 0) {
                        revealCell(board, offI, offJ);
                    }
                }
            }
        }
    }
}
