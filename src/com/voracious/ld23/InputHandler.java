package com.voracious.ld23;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class InputHandler extends KeyAdapter {
    
    public static boolean W_down = false;
    public static boolean A_down = false;
    public static boolean S_down = false;
    public static boolean D_down = false;
    public static boolean SPACE_down = false;
    public static boolean K_down = false;
    
    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
        switch(e.getKeyChar()){
        case 'w':
            W_down = true;
            break;
        case 'a':
            A_down = true;
            break;
        case 's':
            S_down = true;
            break;
        case 'd':
            D_down = true;
            break;
        case 'k':
            K_down = true;
            break;
        case ' ':
            SPACE_down = true;
            break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        super.keyReleased(e);
        switch(e.getKeyChar()){
        case 'w':
            W_down = false;
            break;
        case 'a':
            A_down = false;
            break;
        case 's':
            S_down = false;
            break;
        case 'd':
            D_down = false;
            break;
        case 'k':
            K_down = false;
            break;
        case ' ':
            SPACE_down = false;
            break;
        }
    }
}
