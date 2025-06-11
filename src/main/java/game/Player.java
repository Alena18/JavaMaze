package game;

public class Player {
    private int row;
    private int col;

    public Player(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public void move(int dRow, int dCol, int[][] grid) {
        int newRow = row + dRow;
        int newCol = col + dCol;
        if (newRow >= 0 && newRow < grid.length &&
                newCol >= 0 && newCol < grid[0].length &&
                grid[newRow][newCol] == 0) {
            row = newRow;
            col = newCol;
        }
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
