package com.voracious.ld23.tiles;

import java.awt.image.BufferedImage;

import com.voracious.ld23.gfx.Tile;

public class Water extends Tile {
    public static final int ID = 5;
    public static BufferedImage image = getTileImage(ID);
    
    public Water(){
        super(image);
        setSolid(true);
    }
}
