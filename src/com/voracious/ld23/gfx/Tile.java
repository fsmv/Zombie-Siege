package com.voracious.ld23.gfx;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.voracious.ld23.World;
import com.voracious.ld23.tiles.BeginBridge;
import com.voracious.ld23.tiles.BottomTree;
import com.voracious.ld23.tiles.Bridge;
import com.voracious.ld23.tiles.CornerWall;
import com.voracious.ld23.tiles.CornerWater;
import com.voracious.ld23.tiles.Grass;
import com.voracious.ld23.tiles.InsideCornerWater;
import com.voracious.ld23.tiles.SideBridge;
import com.voracious.ld23.tiles.SideWall;
import com.voracious.ld23.tiles.SideWater;
import com.voracious.ld23.tiles.TallGrass;
import com.voracious.ld23.tiles.TopTree;
import com.voracious.ld23.tiles.TopTreeBackground;
import com.voracious.ld23.tiles.Water;

public class Tile extends Sprite {

    public static final int SIZE = 16;
    public static final Grass grass = new Grass();
    public static final TallGrass tallGrass = new TallGrass();
    public static final SideWall sideWall = new SideWall();
    public static final CornerWall cornerWall = new CornerWall();
    public static final Water water = new Water();
    public static final SideWater sideWater = new SideWater();
    public static final CornerWater cornerWater = new CornerWater();
    public static final InsideCornerWater insideCornerWater = new InsideCornerWater();
    public static final Bridge bridge = new Bridge();
    public static final SideBridge sideBridge = new SideBridge();
    public static final BeginBridge beginBridge = new BeginBridge();
    public static final TopTree topTree = new TopTree();
    public static final BottomTree bottomTree = new BottomTree();
    public static final TopTreeBackground topTreeBackground = new TopTreeBackground();

    private boolean solid = false;
    private boolean breakable = false;
    private boolean drawFirst = true;
    private int damage = 0;
    
    public static BufferedImage TileImage;

    public Tile(BufferedImage image) {
        super(SIZE, SIZE, image);
    }

    public static Tile getTileById(int id) {
        Tile result;

        switch (id) {
        case Grass.ID:
            result = grass;
            break;
        case TallGrass.ID:
            result = tallGrass;
            break;
        case SideWall.ID:
            result = sideWall;
            break;
        case CornerWall.ID:
            result = cornerWall;
            break;
        case Water.ID:
            result = water;
            break;
        case SideWater.ID:
            result = sideWater;
            break;
        case CornerWater.ID:
            result = cornerWater;
            break;
        case InsideCornerWater.ID:
            result = insideCornerWater;
            break;
        case Bridge.ID:
            result = bridge;
            break;
        case SideBridge.ID:
            result = sideBridge;
            break;
        case BeginBridge.ID:
            result = beginBridge;
            break;
        case TopTree.ID:
            result = topTree;
            break;
        case BottomTree.ID:
            result = bottomTree;
            break;
        case TopTreeBackground.ID:
            result = topTreeBackground;
            break;
        default:
            throw new IllegalArgumentException("Tile id not found: " + id);
        }

        return result;
    }

    public static BufferedImage getTileImage(int id) {
        if (TileImage == null) {
            try {
                TileImage = ImageIO.read(Grass.class.getResourceAsStream("/tiles.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        int s = (id - 1) * SIZE;
        int x = s % TileImage.getWidth();
        int y = SIZE * (s / TileImage.getWidth());

        return TileImage.getSubimage(x, y, SIZE, SIZE);
    }

    public boolean isSolid() {
        return solid;
    }

    protected void setSolid(boolean solid) {
        this.solid = solid;
    }

    public boolean shouldDrawFirst() {
        return drawFirst;
    }

    protected void setDrawFirst(boolean flag) {
        drawFirst = flag;
    }
    
    protected void setBreakable(boolean breakable){
        this.breakable = breakable;
    }
    
    public boolean isBreakable(){
        return breakable;
    }
    
    @Override
    public void draw(Screen screen, int x, int y, boolean flipX, boolean flipY, boolean rotate90) {
        if(drawFirst){
            drawBackground(screen, x, y, flipX, flipY, rotate90);
            super.draw(screen, x, y, flipX, flipY, rotate90);
        }else{
            super.draw(screen, x, y, flipX, flipY, rotate90);
        }
    }
    
    protected void setDamage(int damage){
        this.damage = damage;
    }
    
    public int getDamage(){
        return damage;
    }

    public void drawBackground(Screen screen, int x, int y, boolean flipX, boolean flipY, boolean rotate90) {

    }
    
    public void onBreak(World world, int x, int y){
        
    }
}
