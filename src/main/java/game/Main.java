package game;

import org.teavm.jso.browser.Window;

public class Main {
    // Static instance accessible from JavaScript
    public static GameCanvas g_GameCanvas = new GameCanvas();

    // Entry point called from JavaScript (e.g., main();)
    public static void main(String[] args) {
        Window.setTimeout(() -> {
            g_GameCanvas.start();
        }, 0);
    }

    // Optional: JS can call this (if you add @JSBody to expose it, not
    // @JSProperty)
    public static GameCanvas getG_GameCanvas() {
        return g_GameCanvas;
    }
}
