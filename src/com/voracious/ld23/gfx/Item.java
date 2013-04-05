package com.voracious.ld23.gfx;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.voracious.ld23.tiles.Grass;

public class Item {
    public static final int SIZE = 5;
    public static final String IMAGE_PATH = "/items.png";
    private static BufferedImage itemImage;
    private static Sprite sprite;
    private int x, y;
    
    public Item(){
        
    }
    
    public void draw(Screen screen){
        sprite.draw(screen, x, y);
    }
    
    public static BufferedImage geItemImage(int id) {
        if (itemImage == null) {
            try {
                itemImage = ImageIO.read(Grass.class.getResourceAsStream(IMAGE_PATH));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        int s = (id - 1) * SIZE;
        int x = s % itemImage.getWidth();
        int y = SIZE * (s / itemImage.getWidth());

        return itemImage.getSubimage(x, y, SIZE, SIZE);
    }
}
