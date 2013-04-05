package com.voracious.ld23.tiles;

import java.awt.image.BufferedImage;

import com.voracious.ld23.gfx.Tile;

public class SideWall extends Tile {
    public static final int ID = 3;
    public static BufferedImage image = getTileImage(ID);
    
    public SideWall(){
        super(image);
        setSolid(true);
    }
}
