package com.voracious.ld23.screen;

import com.voracious.ld23.Game;
import com.voracious.ld23.InputHandler;
import com.voracious.ld23.gfx.Screen;
import com.voracious.ld23.gfx.Sprite;

public class Menu extends Screen {
    Sprite screen;
    
    public Menu(int width, int height){
        super(width, height);
        screen = new Sprite(200, 150, "/menu.png");
    }
    
    public void render(){
        screen.draw(this, 0, 0);
    }
    
    public void tick(){
        if(InputHandler.SPACE_down){
            Game.setCurrentScreen(new Play(getWidth(), getHeight()));
        }
    }
}
