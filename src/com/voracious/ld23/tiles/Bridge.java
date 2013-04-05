package com.voracious.ld23.tiles;

import java.awt.image.BufferedImage;

import com.voracious.ld23.gfx.Tile;

public class Bridge extends Tile {

    public static final int ID = 9;
    public static BufferedImage image = Tile.getTileImage(ID);
    
    public Bridge(){
        super(image);
    }
}
