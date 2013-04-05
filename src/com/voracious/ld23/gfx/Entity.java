package com.voracious.ld23.gfx;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.voracious.ld23.Game;
import com.voracious.ld23.World;
import com.voracious.ld23.screen.Play;

public class Entity {

    public static final int FPS = 10;
    public static final double WALK_SPEED = 1.0;
    private int attackDelay = 30;
    private int attackCounter = 0;
    private boolean attacking = false;
    private int width, height;
    private double x, y = 0;
    private int power = 4;
    private int currentFrame = 0;
    private int currentAnimation = 0;
    private int directionFacing = 0;
    private int currentFps = FPS;
    private int tickCount = 0;
    private int damage = 64;
    private Play playScreen;
    private boolean play = false;
    private boolean showDamage = false;
    private Sprite[][] frames;

    public Entity(int width, int height, int numFrames[], String image, Play play) {
        this.width = width;
        this.height = height;
        this.playScreen = play;

        int largestNumFrames = 0;
        for (int i = 0; i < numFrames.length; i++) {
            if (numFrames[i] > largestNumFrames) {
                largestNumFrames = numFrames[i];
            }
        }

        try {
            BufferedImage file = ImageIO.read(Entity.class.getResourceAsStream(image));
            frames = new Sprite[numFrames.length][largestNumFrames];
            int framesProcessed = 0;
            for (int j = 0; j < frames.length; j++) {
                for (int i = 0; i < numFrames[j]; i++) {
                    BufferedImage frame = file.getSubimage(((framesProcessed) * width) % file.getWidth(), height * (((framesProcessed) * width) / file.getWidth()), width, height);
                    frames[j][i] = new Sprite(width, height, frame);
                    framesProcessed++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Screen screen) {
        draw(screen, false, false, false);
    }

    private int damageCounter = 0;

    public void draw(Screen screen, boolean flipX, boolean flipY, boolean rotate90) {
        if (!showDamage) {
            frames[currentAnimation][currentFrame].draw(screen, (int) x, (int) y, flipX, flipY, rotate90);
        } else {
            int[] toDraw = frames[currentAnimation][currentFrame].getPixels().clone();
            for (int i = 0; i < toDraw.length; i++) {
                if (toDraw[i] != -1) {
                    toDraw[i] = toDraw[i] & 0xff0000;
                }
            }
            new Sprite(getWidth(), getHeight(), toDraw).draw(screen, (int) x, (int) y, flipX, flipY, rotate90);
            damageCounter++;
            if (damageCounter > 5) {
                damageCounter = 0;
                showDamage = false;
            }
        }
    }

    public void tick() {
        if (play) {
            nextFrame(currentFps);
        }

        tickCount++;
        if (tickCount > Game.FPS * 2) {
            tickCount = 0;
        }

        if (attacking) {
            if (attackCounter == 0) {
                attack();
                attackCounter = attackDelay + 1;
            }
            attackCounter--;
        } else {
            attackCounter = 0;
        }
    }

    public int getWidth() {
        return width;
    }

    protected void setWidth(int w) {
        width = w;
    }

    public int getHeight() {
        return height;
    }

    protected void setHeight(int h) {
        height = h;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrame(int currentFrame) {
        if (currentFrame < frames[currentAnimation].length && currentFrame >= 0 && frames[currentAnimation][currentFrame] != null) {
            this.currentFrame = currentFrame;
        } else {
            throw new IllegalArgumentException("The frame specified is out of bounds: " + currentFrame);
        }
    }

    public void setCurrentAnimation(int currentAnimation) {
        if (currentAnimation < frames.length && currentAnimation >= 0) {
            this.currentAnimation = currentAnimation;
            currentFrame = 0;
            tickCount = -1;
        } else {
            throw new IllegalArgumentException("The frame specified is out of bounds: " + currentAnimation);
        }
    }

    public int getCurrentAnimation() {
        return currentAnimation;
    }

    public Play getPlayScreen() {
        return playScreen;
    }

    protected void attack() {
        int x0 = (int) x / Tile.SIZE;
        int y0 = (int) y / Tile.SIZE + this.getHeight() - Tile.SIZE; //TODO bad if I have an entity smaller than a tile
        int dx = 0;
        int dy = 0;
        int dir = getDirectionFacing();

        switch (dir) {
        case 0:
            dy = -1;
            break;
        case 1:
            dx = 1;
            break;
        case 2:
            dy = 1;
            break;
        case 3:
            dx = -1;
            break;
        }

        x0 += dx;
        y0 += dy;

        Game.sounds.get("hit").play();

        boolean entityHit = false;
        ArrayList<Entity> entities = playScreen.getEntities();

        boolean killPlayer = false;
        this.setX(this.getX() - 1);
        this.setY(this.getY() - 1);
        this.setWidth(getWidth() + 2);
        this.setHeight(getHeight() + 2);

        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            if (!e.equals(this)) {
                if (this.hitTest(e)) {
                    entityHit = true;
                    e.damage(power);
                }
            }
        }

        if (!entityHit) {
            World w = getPlayScreen().getWorld();

            for (int c = y0 - 2; c <= y0 + 2; c++) {
                for (int r = x0 - 2; r <= x0 + 2; r++) {
                    Tile temp = Tile.getTileById(w.getTile(r, c));
                    if (temp.isBreakable()) {
                        if (this.hitTest(temp, r * Tile.SIZE, c * Tile.SIZE)) {
                            w.attackTile(r, c, power);
                            if (w.getTileDamage(r, c) <= 0) {
                                w.breakTile(r, c);
                            }
                        }
                    }
                }
            }
        }

        this.setX(this.getX() + 1);
        this.setY(this.getY() + 1);
        this.setWidth(getWidth() - 2);
        this.setHeight(getHeight() - 2);
    }

    public void setAttacking(boolean flag) {
        this.attacking = flag;
    }

    public void damage(int power) {
        damage -= power;
        onDamage();
        if (damage <= 0) {
            onDie();
        }
    }

    public void onDamage() {
        showDamage = true;
    }

    public void onDie() {
        playScreen.getEntities().remove(this);
    }

    /**
     * Override me!
     */
    public int getDirectionFacing() {
        return 0;
    }

    public void nextFrame() {
        nextFrame(Entity.FPS);
    }

    public void nextFrame(int fps) {
        if (tickCount > Game.FPS / fps) {
            currentFrame++;
            if (currentFrame >= frames[currentAnimation].length || frames[currentAnimation][currentFrame] == null) {
                currentFrame = 0;
            }
            tickCount = -1;
        }
    }

    public boolean hitTest(Sprite test, int x, int y) {
        boolean result = false;

        if (this.getX() + this.getWidth() > x && this.getX() < test.getWidth() + x) {
            if (this.getY() + this.getHeight() > y && this.getY() < y + test.getHeight()) {
                result = true;
            }
        }

        return result;
    }

    public boolean hitTest(ArrayList<Entity> eList) {
        boolean result = false;

        for (int i = 0; i < eList.size(); i++) {
            Entity e = eList.get(i);
            if (!this.equals(e)) {
                if (this.hitTest(e)) {
                    result = true;
                    break;
                }
            }
        }

        return result;
    }

    public boolean hitTest(Entity e) {
        boolean result = false;

        if (this.getX() + this.getWidth() > e.getX() && this.getX() < e.getWidth() + e.getX()) {
            if (this.getY() + this.getHeight() > e.getY() && this.getY() < e.getY() + e.getHeight()) {
                result = true;
            }
        }

        return result;
    }

    public void play(int fps) {
        currentFps = fps;
        tickCount = 0;
        play = true;
    }

    public void play() {
        play(Entity.FPS);
    }

    public void stop() {
        play = false;
    }
}
