package com.voracious.ld23.tiles;

import java.awt.image.BufferedImage;

import com.voracious.ld23.gfx.Tile;

public class Grass extends Tile {
    
    public static final int ID = 1;
    public static BufferedImage image = getTileImage(ID);
            
    public Grass() {
        super(image);
        setSolid(false);
    }
}
