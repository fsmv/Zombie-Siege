package com.voracious.ld23.entities;

import java.util.Random;

import com.voracious.ld23.gfx.Entity;
import com.voracious.ld23.gfx.Tile;
import com.voracious.ld23.screen.Play;

public class Zombie extends Entity {
    public static final int FPS = 5;
    public static final int WIDTH = 12;
    public static final int HEIGHT = 20;
    public static final int FRAMES[] = { 2, 2, 4, 4 };
    public static final double WALK_SPEED = 0.5;
    public static final String IMAGE_FILE = "/entities/zombie.png";

    private Random rand;
    private int direction1;
    private int direction2;
    private int avoidingTimer = 0;

    public Zombie(Play play) {
        super(WIDTH, HEIGHT, FRAMES, IMAGE_FILE, play);
        rand = new Random();
    }

    @Override
    public void tick() {
        boolean move1 = true;
        boolean move2 = true;
        super.tick();
        if (direction1 != -1) {
            move1 = getPlayScreen().tryMove(this, direction1, Zombie.WALK_SPEED);
        } else {
            move1 = false;
        }
        if (direction1 != direction2 && direction2 != -1) {
            move2 = getPlayScreen().tryMove(this, direction2, Zombie.WALK_SPEED);
        } else {
            move2 = false;
        }

        if (avoidingTimer == 0) {
            if (!move1 && !move2) {
                int tileInFront = getPlayScreen().getWorld().getTile((int) this.getX() / Tile.SIZE, (int) this.getY() / Tile.SIZE);
                boolean killPlayer = false;
                this.setX(this.getX() - 1);
                this.setY(this.getY() - 1);
                this.setWidth(getWidth() + 2);
                this.setHeight(getHeight() + 2);

                killPlayer = this.hitTest(getPlayScreen().player);

                this.setX(this.getX() + 1);
                this.setY(this.getY() + 1);
                this.setWidth(getWidth() - 2);
                this.setHeight(getHeight() - 2);

                if (killPlayer) {
                    setAttacking(true);
                } else {
                    setAttacking(false);
                    setDirection(rand.nextInt(4), rand.nextInt(4));
                    avoidingTimer++;
                }
            } else {
                int d1 = -1;
                int d2 = -1;
                if (getPlayScreen().player.getX() > getX()) {
                    d1 = d2 = 1;
                } else if (getPlayScreen().player.getX() < getX()) {
                    d1 = d2 = 3;
                }
                if (getPlayScreen().player.getY() > getY()) {
                    d2 = 2;
                } else if (getPlayScreen().player.getY() < getY()) {
                    d2 = 0;
                }
                setDirection(d1, d2);
                setAttacking(false);
            }
        }

        if (avoidingTimer != 0) {
            avoidingTimer++;
        }

        if (avoidingTimer > 100) {
            avoidingTimer = 0;
        }

        updateAnimation();
    }

    @Override
    public void play() {
        play(Zombie.FPS);
    }

    public int[] getDirection() {
        int[] dirs = { direction1, direction2 };
        return dirs;
    }

    public void setDirection(int d1, int d2) {
        direction1 = d1;
        direction2 = d2;
    }

    public void updateAnimation() {
        int newAnimation = 0;

        int direction = direction1;
        if (direction1 != direction2 && direction2 % 2 == 2) {
            direction = direction2;
        }

        switch (direction) {
        case 0:
            newAnimation = 1;
            break;
        case 1:
            newAnimation = 3;
            break;
        case 2:
            newAnimation = 0;
            break;
        case 3:
            newAnimation = 2;
            break;
        }

        setCurrentAnimation(newAnimation);
    }

    @Override
    public int getDirectionFacing() {
        int result = 0;

        switch (getCurrentAnimation()) {
        case 0:
            result = 2;
            break;
        case 1:
            result = 0;
            break;
        case 2:
            result = 3;
            break;
        case 3:
            result = 1;
            break;
        }

        return result;
    }
}
