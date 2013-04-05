package com.voracious.ld23.tiles;

import java.awt.image.BufferedImage;

import com.voracious.ld23.gfx.Tile;

public class TopTreeBackground extends Tile {
    public static final int ID = 14;
    public static BufferedImage image = Tile.getTileImage(ID);
    
    public TopTreeBackground(){
        super(image);
    }
}
