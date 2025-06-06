package game;

import org.teavm.jso.browser.Window;

public class Main {
    public static void main(String[] args) {
        Window.setTimeout(() -> {
            GameCanvas game = new GameCanvas();
            game.start();
        }, 0);
    }
}
