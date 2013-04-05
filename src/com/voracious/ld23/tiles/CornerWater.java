package com.voracious.ld23.tiles;

import java.awt.image.BufferedImage;

import com.voracious.ld23.gfx.Tile;

public class CornerWater extends Tile {
    public static final int ID = 7;
    public static BufferedImage image = getTileImage(ID);
    
    public CornerWater(){
        super(image);
        setSolid(true);
    }
}
