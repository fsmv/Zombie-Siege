package com.voracious.ld23.tiles;

import java.awt.image.BufferedImage;

import com.voracious.ld23.gfx.Tile;

public class BeginBridge extends Tile {

    public static final int ID = 11;
    public static BufferedImage image = Tile.getTileImage(ID);
    
    public BeginBridge(){
        super(image);
    }
}
