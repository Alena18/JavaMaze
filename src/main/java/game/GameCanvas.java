package game;

import org.teavm.jso.dom.html.*;
import org.teavm.jso.browser.Window;
import org.teavm.jso.JSBody;
import org.teavm.jso.canvas.CanvasRenderingContext2D;
import org.teavm.jso.dom.events.KeyboardEvent;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.dom.html.HTMLDocument;

public class GameCanvas {
    private final int cellSize = 20;
    private HTMLCanvasElement canvas;
    private CanvasRenderingContext2D ctx;
    private Maze maze;
    private Player player;
    private int finishRow;
    private int finishCol;
    private int timeLeft = 60; // seconds
    private int timerId = -1;
    private boolean gameActive = true;

    public void start() {
        stopTimer(); // <-- Clear any existing timer first
        timeLeft = 60; // <-- Set timer to 1:00 (60 seconds)
        gameActive = true; // Reset game state
        canvas = (HTMLCanvasElement) Window.current().getDocument().getElementById("gameCanvas");
        ctx = (CanvasRenderingContext2D) canvas.getContext("2d");

        maze = new Maze(21, 21);
        player = new Player(1, 1);
        finishRow = maze.getRows() - 2;
        finishCol = maze.getCols() - 2;

        canvas.setWidth(maze.getCols() * cellSize);
        canvas.setHeight(maze.getRows() * cellSize);

        draw();

        Window.current().addEventListener("keydown", e -> {
            if (!gameActive)
                return;

            KeyboardEvent evt = (KeyboardEvent) e;
            String code = evt.getKey();

            switch (code) {
                case "ArrowUp":
                    player.move(-1, 0, maze.getGrid());
                    break;
                case "ArrowDown":
                    player.move(1, 0, maze.getGrid());
                    break;
                case "ArrowLeft":
                    player.move(0, -1, maze.getGrid());
                    break;
                case "ArrowRight":
                    player.move(0, 1, maze.getGrid());
                    break;
            }

            draw();
            checkWin();
        });

        startTimer();
    }

    private void draw() {
        int[][] grid = maze.getGrid();
        for (int r = 0; r < maze.getRows(); r++) {
            for (int c = 0; c < maze.getCols(); c++) {
                ctx.setFillStyle(grid[r][c] == 1 ? "#c78ef8" : "white");
                ctx.fillRect(c * cellSize, r * cellSize, cellSize, cellSize);
            }
        }

        // DRAW FINISH AREA
        // Position it so that the LEFT square is inside the maze (reachable), and RIGHT
        // is over wall
        ctx.setFillStyle("black");
        ctx.fillRect(finishCol * cellSize, finishRow * cellSize, cellSize * 2, cellSize); // 2 cells wide

        // DRAW "END" TEXT CENTERED
        ctx.setFillStyle("#c78ef8"); // light purple
        ctx.setFont("bold " + (cellSize * 0.4)
                + "px 'Orbitron', 'Segoe UI Emoji', 'Noto Color Emoji', 'Apple Color Emoji', sans-serif");
        ctx.setTextAlign("left"); // we calculate spacing manually
        ctx.setTextBaseline("middle");

        String text = "END";
        double spacing = cellSize * 0.1;

        // Calculate total width of the whole word with spacing
        double totalWidth = 0;
        for (int i = 0; i < text.length(); i++) {
            totalWidth += ctx.measureText(String.valueOf(text.charAt(i))).getWidth();
            if (i < text.length() - 1)
                totalWidth += spacing;
        }

        // Start X for centering across the 2-cell area
        double startX = finishCol * cellSize + (2 * cellSize - totalWidth) / 2;
        double y = finishRow * cellSize + cellSize / 2;

        // Draw each letter spaced manually
        double x = startX;
        for (int i = 0; i < text.length(); i++) {
            String letter = String.valueOf(text.charAt(i));
            ctx.fillText(letter, x, y);
            x += ctx.measureText(letter).getWidth() + spacing;
        }

        // DRAW PLAYER
        ctx.setFillStyle("#4a00e0"); // player color
        ctx.fillRect(player.getCol() * cellSize, player.getRow() * cellSize, cellSize, cellSize);
    }

    private void checkWin() {
        // Check if player reaches the FIRST (reachable) black square
        if (player.getRow() == finishRow && player.getCol() == finishCol) {
            gameActive = false;
            stopTimer();
            showResult("You won");

        }
    }

    private void startTimer() {
        stopTimer(); // Ensure any existing timer is stopped
        timeLeft = 45; // Reset timer to 40 seconds
        updateTimerDisplay();
        timerId = Window.setInterval(() -> {
            timeLeft--;
            updateTimerDisplay();
            if (timeLeft <= 0) {
                stopTimer();
                gameActive = false; // Stop further movement
                if (player.getRow() != finishRow || player.getCol() != finishCol) {
                    endGame("You lose");
                }
            }
        }, 1000);
    }

    private void stopTimer() {
        if (timerId != -1) {
            Window.clearInterval(timerId);
            timerId = -1;
        }
    }

    private void updateTimerDisplay() {
        int min = timeLeft / 60;
        int sec = timeLeft % 60;
        HTMLElement timer = (HTMLElement) Window.current().getDocument().getElementById("timer");
        timer.setInnerText(String.format("%02d:%02d", min, sec));
    }

    private void endGame(String resultText) {
        gameActive = false;
        stopTimer();
        showResult(resultText);
    }

    @JSBody(params = { "text" }, script = """
              if (typeof showResult === 'function') {
                showResult(text);
              }
            """)
    private static native void showResult(String text);

}
