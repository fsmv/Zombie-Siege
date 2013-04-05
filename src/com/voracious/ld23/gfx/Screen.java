package com.voracious.ld23.gfx;

import com.voracious.ld23.Game;

public class Screen {
    private int[] pixels;
    private int width, height;
    private int offsetX, offsetY = 0;

    public Screen(int width, int height) {
        pixels = new int[width * height];
        this.width = width;
        this.height = height;
    }

    public void setPixel(int color, int x, int y) {
        if(color != -1){
            pixels[width * y + x] = color;
        }
    }

    public void clear(int color) {
        for (int i = 0; i < pixels.length; i++) {
            pixels[i] = color;
        }
    }

    public void draw(int[] drawSurface) {
        int y = 0;
        int x = 0;
        for (int i = 0; i < drawSurface.length; i++) {
            int myIndex = width * (offsetY + y) + offsetX + x;
            if (pixels[myIndex] != -1) {
                drawSurface[i] = pixels[myIndex];
            }
            x++;
            if(x >= Game.WIDTH){
                y++;
                x = 0;
            }
        }
    }
    
    public int getWidth(){
        return width;
    }
    
    public int getHeight(){
        return height;
    }
    
    public int getOffsetX(){
        return offsetX;
    }
    
    public int getOffsetY(){
        return offsetY;
    }
    
    public void setOffsetX(int offset){
        this.offsetX = offset;
    }
    
    public void setOffsetY(int offset){
        this.offsetY = offset;
    }

    public void tick() {
    }

    public void render() {
    }
    
    public void start() {
    }
    
    public void stop() {
    }
}
