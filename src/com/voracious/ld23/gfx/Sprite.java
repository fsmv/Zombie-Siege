package com.voracious.ld23.gfx;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Sprite {
    private int width, height;
    private int[] pixels;

    public Sprite(int width, int height, String image) {
        this.width = width;
        this.height = height;
        pixels = new int[width * height];

        try {
            BufferedImage file = ImageIO.read(Sprite.class.getResourceAsStream(image));

            for (int i = 0; i < width * height; i++) {
                int col = file.getRGB(i % width, i / width);
                if ((col & 0xff000000) == 0) {
                    pixels[i] = -1;
                } else {
                    pixels[i] = col & 0x00ffffff;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Sprite(int width, int height, BufferedImage image) {
        this.width = width;
        this.height = height;
        pixels = new int[width * height];

        for (int i = 0; i < width * height; i++) {
            int col = image.getRGB(i % width, i / width);
            if ((col & 0xff000000) == 0) {
                pixels[i] = -1;
            } else {
                pixels[i] = col & 0x00ffffff;
            }
        }
    }

    public Sprite(int width, int height, int[] pixels) {
        this.width = width;
        this.height = height;
        this.pixels = pixels;
    }

    public void draw(Screen screen, int x, int y) {
        draw(screen, x, y, false, false, false);
    }

    public void draw(Screen screen, int x, int y, boolean flipX, boolean flipY, boolean rotate90) {
        for (int i = 0; i < pixels.length; i++) {
            int iterator = i;
            int screenX = x + (i % width);
            int screenY = y + (i / width);

            if (rotate90) {
                int tempX = screenX;
                screenX = screenY - y + x;
                screenY = tempX - x + y;

                int tempWidth = getWidth();
                this.width = this.height;
                this.height = tempWidth;
            }

            if (flipX && flipY) {
                iterator = (height - 1 - (i / width)) * width + (width - 1 - (i % width));
            } else if (flipX) {
                iterator = (i / width) * width + (width - 1 - (i % width));
            } else if (flipY) {
                iterator = (height - 1 - (i / width)) * width + (i % width);
            }

            screen.setPixel(pixels[iterator], screenX, screenY);
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int[] getPixels() {
        return pixels;
    }
}
