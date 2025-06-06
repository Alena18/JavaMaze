
// Basic canvas setup
import org.teavm.jso.canvas.*;
import org.teavm.jso.dom.html.*;

public class GameCanvas {
    private HTMLCanvasElement canvas;
    private CanvasRenderingContext2D context;

    public void start() {
        canvas = (HTMLCanvasElement) Window.current().getDocument().getElementById("gameCanvas");
        context = (CanvasRenderingContext2D) canvas.getContext("2d");
        draw();
    }

    public void draw() {
        context.setFillStyle("black");
        context.fillRect(0, 0, 600, 600);
        // draw maze here
    }
}
