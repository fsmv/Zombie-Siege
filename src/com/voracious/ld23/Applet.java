package com.voracious.ld23;

import java.awt.BorderLayout;

public class Applet extends java.applet.Applet {
    private static final long serialVersionUID = 1L;
    public static Game game;
    
    @Override
    public void init(){
        game = new Game();
        setLayout(new BorderLayout());
        this.add(game, BorderLayout.CENTER);
    }
    
    @Override
    public void start(){
        game.start();
    }
    
    @Override
    public void stop(){
        game.stop();
    }
}
