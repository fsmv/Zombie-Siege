package com.voracious.ld23.entities;

import com.voracious.ld23.Game;
import com.voracious.ld23.InputHandler;
import com.voracious.ld23.gfx.Entity;
import com.voracious.ld23.screen.Play;

public class Player extends Entity {
    public static final int WIDTH = 12;
    public static final int HEIGHT = 20;
    public static final int FRAMES[] = { 2, 2, 4, 4 };
    public static final String IMAGE_FILE = "/entities/player.png";

    public Player(Play play) {
        super(WIDTH, HEIGHT, FRAMES, IMAGE_FILE, play);
    }

    public void tick() {
        super.tick();
        boolean keyAlreadyDown = false;

        if (InputHandler.SPACE_down || InputHandler.K_down) {
            setAttacking(true);
        } else {
            setAttacking(false);
        }

        if (InputHandler.W_down) {
            getPlayScreen().tryMove(this, 0);
            if (getCurrentAnimation() != 1) {
                setCurrentAnimation(1);
            }
            nextFrame();
            keyAlreadyDown = true;
        }
        if (InputHandler.A_down) {
            getPlayScreen().tryMove(this, 3);
            if (!keyAlreadyDown) {
                if (getCurrentAnimation() != 2) {
                    setCurrentAnimation(2);
                }
                nextFrame();
            }
            keyAlreadyDown = true;
        }
        if (InputHandler.S_down) {
            getPlayScreen().tryMove(this, 2);
            if (!keyAlreadyDown) {
                if (getCurrentAnimation() != 0) {
                    setCurrentAnimation(0);
                }
                nextFrame();
            }
            keyAlreadyDown = true;
        }
        if (InputHandler.D_down) {
            getPlayScreen().tryMove(this, 1);
            if (!keyAlreadyDown) {
                if (getCurrentAnimation() != 3) {
                    setCurrentAnimation(3);
                }
                nextFrame();
            }
            keyAlreadyDown = true;
        }
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
    
    public void onDie(){
        super.onDie();
        getPlayScreen().gameover();
    }
}
