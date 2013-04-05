package com.voracious.ld23.tiles;

import java.awt.image.BufferedImage;

import com.voracious.ld23.gfx.Tile;

public class SideBridge extends Tile {

    public static final int ID = 10;
    public static BufferedImage image = Tile.getTileImage(ID);
    
    public SideBridge(){
        super(image);
    }
}
