package game;

import java.util.*;

public class Solver {
    private final Maze maze;
    private final int[][] directions = {
            { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } // right, down, left, up
    };

    public List<int[]> path; // List of [row, col] for the path

    public Solver(Maze maze) {
        this.maze = maze;
        path = solve();
    }

    private List<int[]> solve() {
        int[][] grid = maze.getGrid();
        int rows = grid.length;
        int cols = grid[0].length;
        boolean[][] visited = new boolean[rows][cols];
        Map<String, String> parentMap = new HashMap<>();

        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[] { 1, 1 }); // start
        visited[1][1] = true;

        int[] end = { rows - 2, cols - 2 };

        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            if (curr[0] == end[0] && curr[1] == end[1]) {
                break;
            }

            for (int[] d : directions) {
                int nr = curr[0] + d[0];
                int nc = curr[1] + d[1];

                if (inBounds(nr, nc, rows, cols) &&
                        grid[nr][nc] == 0 &&
                        !visited[nr][nc]) {

                    visited[nr][nc] = true;
                    queue.add(new int[] { nr, nc });
                    parentMap.put(nr + "," + nc, curr[0] + "," + curr[1]);
                }
            }
        }

        return reconstructPath(parentMap, end);
    }

    private List<int[]> reconstructPath(Map<String, String> parentMap, int[] end) {
        List<int[]> result = new ArrayList<>();
        String current = end[0] + "," + end[1];

        while (parentMap.containsKey(current)) {
            String[] parts = current.split(",");
            result.add(new int[] { Integer.parseInt(parts[0]), Integer.parseInt(parts[1]) });
            current = parentMap.get(current);
        }

        Collections.reverse(result);
        return result;
    }

    private boolean inBounds(int r, int c, int rows, int cols) {
        return r >= 0 && c >= 0 && r < rows && c < cols;
    }
}
