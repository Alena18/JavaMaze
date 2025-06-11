package game;

import java.util.Arrays;
import java.util.Random;

public class Maze {
    private final int[][] grid;
    private final int rows;
    private final int cols;

    public Maze(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new int[rows][cols];

        // Fill entire grid with walls
        for (int r = 0; r < rows; r++) {
            Arrays.fill(grid[r], 1);
        }

        // Start carving from cell (1,1)
        generateMaze(1, 1);
    }

    private void generateMaze(int r, int c) {
        grid[r][c] = 0;

        int[] dr = { -2, 2, 0, 0 };
        int[] dc = { 0, 0, -2, 2 };
        int[] dirs = { 0, 1, 2, 3 };

        Random rand = new Random();

        // Shuffle directions
        for (int i = 0; i < dirs.length; i++) {
            int j = rand.nextInt(dirs.length);
            int temp = dirs[i];
            dirs[i] = dirs[j];
            dirs[j] = temp;
        }

        for (int dir : dirs) {
            int nr = r + dr[dir];
            int nc = c + dc[dir];

            if (nr > 0 && nr < rows - 1 && nc > 0 && nc < cols - 1 && grid[nr][nc] == 1) {
                grid[r + dr[dir] / 2][c + dc[dir] / 2] = 0; // break wall
                grid[nr][nc] = 0; // mark cell
                generateMaze(nr, nc);
            }
        }
    }

    public int[][] getGrid() {
        return grid;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }
}
