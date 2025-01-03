package tileengine;

import core.WorldExperimental;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;

/**
 * Utility class for rendering tiles. You do not need to modify this file. You're welcome
 * to, but be careful. We strongly recommend getting everything else working before
 * messing with this renderer, unless you're trying to do something fancy like
 * allowing scrolling of the screen or tracking the avatar or something similar.
 */
public class TERenderer {
    private static final int TILE_SIZE = 16;
    private int width;
    private int height;
    private int xOffset;
    private int yOffset;

    /**
     * Same functionality as the other initialization method. The only difference is that the xOff
     * and yOff parameters will change where the renderFrame method starts drawing. For example,
     * if you select w = 60, h = 30, xOff = 3, yOff = 4 and then call renderFrame with a
     * TETile[50][25] array, the renderer will leave 3 tiles blank on the left, 7 tiles blank
     * on the right, 4 tiles blank on the bottom, and 1 tile blank on the top.
     * @param w width of the window in tiles
     * @param h height of the window in tiles.
     */
    public void initialize(int w, int h, int xOff, int yOff) {
        this.width = w;
        this.height = h;
        this.xOffset = xOff;
        this.yOffset = yOff;
        StdDraw.setCanvasSize(width * TILE_SIZE, height * TILE_SIZE);
        resetFont();
        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, height);

        StdDraw.clear(new Color(0, 0, 0));

        StdDraw.enableDoubleBuffering();
        StdDraw.show();
    }

    /**
     * Initializes StdDraw parameters and launches the StdDraw window. w and h are the
     * width and height of the world in number of tiles. If the TETile[][] array that you
     * pass to renderFrame is smaller than this, then extra blank space will be left
     * on the right and top edges of the frame. For example, if you select w = 60 and
     * h = 30, this method will create a 60 tile wide by 30 tile tall window. If
     * you then subsequently call renderFrame with a TETile[50][25] array, it will
     * leave 10 tiles blank on the right side and 5 tiles blank on the top side. If
     * you want to leave extra space on the left or bottom instead, use the other
     * initializatiom method.
     * @param w width of the window in tiles
     * @param h height of the window in tiles.
     */
    public void initialize(int w, int h) {
        initialize(w, h, 0, 0);
    }

    /**
     * Takes in a 2d array of TETile objects and renders the 2d array to the screen, starting from
     * xOffset and yOffset.
     *
     * If the array is an NxM array, then the element displayed at positions would be as follows,
     * given in units of tiles.
     *
     *              positions   xOffset |xOffset+1|xOffset+2| .... |xOffset+world.length
     *                     
     * startY+world[0].length   [0][M-1] | [1][M-1] | [2][M-1] | .... | [N-1][M-1]
     *                    ...    ......  |  ......  |  ......  | .... | ......
     *               startY+2    [0][2]  |  [1][2]  |  [2][2]  | .... | [N-1][2]
     *               startY+1    [0][1]  |  [1][1]  |  [2][1]  | .... | [N-1][1]
     *                 startY    [0][0]  |  [1][0]  |  [2][0]  | .... | [N-1][0]
     *
     * By varying xOffset, yOffset, and the size of the screen when initialized, you can leave
     * empty space in different places to leave room for other information, such as a GUI.
     * This method assumes that the xScale and yScale have been set such that the max x
     * value is the width of the screen in tiles, and the max y value is the height of
     * the screen in tiles.
     * @param world the 2D TETile[][] array to render
     */
    public void renderFrame(TETile[][] world) {
        StdDraw.clear(new Color(0, 0, 0));
        drawTiles(world);

        StdDraw.show();

    }

    /**
     * Draws all world tiles without clearing the canvas or showing the tiles.
     * @param world the 2D TETile[][] array to render
     */
    public void drawTiles(TETile[][] world) {
        int numXTiles = world.length;
        int numYTiles = world[0].length;
        for (int x = 0; x < numXTiles; x += 1) {
            for (int y = 0; y < numYTiles; y += 1) {
                if (world[x][y] == null) {
                    throw new IllegalArgumentException("Tile at position x=" + x + ", y=" + y
                            + " is null.");
                }
                world[x][y].draw(x + xOffset, y + yOffset);

            }
        }
    }

    public void drawTilesSight(TETile[][] world, int x ,int y) {

        for (int i = x - 6; i <  x + 6; i += 1) {
            for (int j = y - 6; j < y + 6; j += 1) {
                if (world[i][j] == null) {
                    throw new IllegalArgumentException("Tile at position x=" + x + ", y=" + y
                            + " is null.");
                }
                double v = Math.pow((i - (x)), 2) + Math.pow((j - (y)), 2);
                if (v <= Math.pow(5, 2)){
                    world[i][j].draw(i + xOffset, j + yOffset);

                }

            }
        }
    }

    public void wallsBlockView(TETile[][] world, int x, int y, int radius) {
        if (radius <= 0) {
            return;
        }

        int[] up = {0, 1};
        int[] down = {0, -1};
        int[] left = {-1, 0};
        int[] right = {1, 0};

        int[][] allDirections = {up, down, left, right};
        for(int[] direction : allDirections) {
            int newX = x + direction[0];
            int newY = y + direction[1];

            if (newX < 0 || newY < 0 || newX >= world.length || newY >= world[0].length) {
                continue;
            }

            if (world[newX][newY] != Tileset.WALL) {
                world[newX + 1][newY].draw(newX + 1 + xOffset, newY + yOffset);
                world[newX - 1][newY].draw(newX - 1 + xOffset, newY + yOffset);
                world[newX][newY + 1].draw(newX + xOffset, newY + 1 + yOffset);
                world[newX][newY - 1].draw(newX + xOffset, newY - 1 + yOffset);
                world[newX][newY].draw(newX + xOffset, newY + yOffset);
                wallsBlockView(world, newX, newY, radius - 1);
            }
        }
    }

    public void renderFrameSight(TETile[][] world, int x, int y, int health, WorldExperimental newWorld) {
        StdDraw.clear(new Color(0, 0, 0));
        wallsBlockView(world, x, y, 4);
        StdDraw.show();

    }


    /**
     * Resets the font to default settings. You should call this method before drawing any tiles
     * if you changed the pen settings.
     */
    public void resetFont() {
        Font font = new Font("Monaco", Font.BOLD, TILE_SIZE - 2);
        StdDraw.setFont(font);
    }
}
