package Game_Files;

import edu.princeton.cs.algs4.*;
import java.awt.*;


public class Start_Menu {
    public Start_Menu() {

        /**
         * This will be the menu's size for when starting, we can change the size later though
         */

        StdDraw.setCanvasSize(512, 512);
        StdDraw.setXscale(0, 512);
        StdDraw.setYscale(0, 512);

        StdDraw.filledSquare(256, 256, 256);

        Font titleFont = new Font("Arial", Font.BOLD, 30);
        StdDraw.setFont(titleFont);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(256, 422, "Some Kind of TD");

    }
}
