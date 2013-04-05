package com.voracious.ld23.tiles;

import java.awt.image.BufferedImage;

import com.voracious.ld23.gfx.Tile;

public class CornerWall extends Tile {
    public static final int ID = 4;
    public static BufferedImage image = getTileImage(ID);
    
    public CornerWall(){
        super(image);
        setSolid(true);
    }
}
