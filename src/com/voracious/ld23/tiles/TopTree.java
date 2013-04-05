package com.voracious.ld23.tiles;

import java.awt.image.BufferedImage;

import com.voracious.ld23.gfx.Screen;
import com.voracious.ld23.gfx.Tile;

public class TopTree extends Tile {
    public static final int ID = 12;
    public static BufferedImage image = Tile.getTileImage(ID);

    public TopTree() {
        super(image);
        setDrawFirst(false);
    }
    
    @Override
    public void drawBackground(Screen screen, int x, int y, boolean flipX, boolean flipY, boolean rotate90){
        Tile.topTreeBackground.draw(screen, x, y, flipX, flipY, rotate90);
    }
}
