package com.voracious.ld23.tiles;

import java.awt.image.BufferedImage;

import com.voracious.ld23.World;
import com.voracious.ld23.gfx.Tile;

public class BottomTree extends Tile{
    public static final int ID = 13;
    public static BufferedImage image = Tile.getTileImage(ID);
    
    public BottomTree(){
        super(image);
        setSolid(true);
        setBreakable(true);
        setDamage(16);
    }
    
    @Override
    public void onBreak(World world, int x, int y){
        System.out.println(world.getTile(x, y-1) + " " + TopTree.ID);
        if(world.getTile(x, y-1) == TopTree.ID){
            world.breakTile(x, y - 1, false);
        }
    }
}
