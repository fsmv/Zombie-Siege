package com.voracious.ld23.tiles;

import java.awt.image.BufferedImage;

import com.voracious.ld23.gfx.Tile;

public class SideWater extends Tile {
    public static final int ID = 6;
    public static BufferedImage image = getTileImage(ID);
    
    public SideWater(){
        super(image);
        setSolid(true);
    }
}
