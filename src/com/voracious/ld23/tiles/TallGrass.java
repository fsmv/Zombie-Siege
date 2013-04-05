package com.voracious.ld23.tiles;

import java.awt.image.BufferedImage;

import com.voracious.ld23.gfx.Tile;

public class TallGrass extends Tile {
    
    public static final int ID = 2;
    public static BufferedImage image = getTileImage(ID);
            
    public TallGrass() {
        super(image);
    }
}
