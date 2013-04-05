package com.voracious.ld23.tiles;

import java.awt.image.BufferedImage;

import com.voracious.ld23.gfx.Tile;

public class InsideCornerWater extends Tile {
    public static final int ID = 8;
    public static BufferedImage image = Tile.getTileImage(8);
    
    public InsideCornerWater(){
        super(image);
    }
}
