package com.voracious.ld23;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.HashMap;

import javax.swing.JFrame;

import kuusisto.tinysound.Sound;
import kuusisto.tinysound.TinySound;

import com.voracious.ld23.gfx.Screen;
import com.voracious.ld23.screen.Menu;

public class Game extends Canvas implements Runnable {
    private static final long serialVersionUID = 1L;
    public static final int FPS = 60;
    public static final String NAME = "LD23";
    public static final int WIDTH = 200;
    public static final int HEIGHT = 150;
    public static final int SCALE = 3;
    public static HashMap<String, Sound> sounds;
    private static BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    private static Screen currentScreen;
    private static boolean running = false;

    public Game() {
        init();
    }

    public static void main(String[] args) {
        Dimension size = new Dimension(WIDTH * SCALE, HEIGHT * SCALE);
        Game game = new Game();
        game.setMinimumSize(size);
        game.setMaximumSize(size);
        game.setPreferredSize(size);

        JFrame window = new JFrame(NAME);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());
        window.add(game, BorderLayout.CENTER);
        window.pack();
        window.setResizable(false);
        window.setLocationRelativeTo(null);
        window.setFocusable(true);
        window.setVisible(true);

        game.start();
    }

    public void init() {
        setCurrentScreen(new Menu(400, 400));
        sounds = new HashMap<String, Sound>();
        TinySound.init();
        sounds.put("break", TinySound.loadSound("/break.wav"));
        sounds.put("gameover", TinySound.loadSound("/gameover.wav"));
        sounds.put("hit", TinySound.loadSound("/hit.wav"));
        sounds.put("pickup", TinySound.loadSound("/pickup.wav"));
        sounds.put("place", TinySound.loadSound("/place.wav"));
        this.addKeyListener(new InputHandler());
    }

    public void start() {
        running = true;
        new Thread(this).start();
    }

    public void stop() {
        TinySound.shutdown();
        running = false;
    }

    private int ticksProcessed = 0;
    private int framesRendered = 0;

    @Override
    public void run() {
        double unprocessedTicks = 0;
        long last = System.nanoTime();
        long lastMs = System.currentTimeMillis();
        double timeForTick = 1000000000 / FPS;
        boolean needsRender = false;
        while (running) {
            if (!this.hasFocus()) {
                 System.out.println("not focused");
            }

            long now = System.nanoTime();
            unprocessedTicks += (now - last) / timeForTick;
            last = now;

            if (unprocessedTicks >= 1.0) {
                needsRender = true;
                tick();
                ticksProcessed++;
                unprocessedTicks -= 1.0;
            }

            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (needsRender) {
                render();
                framesRendered++;
                needsRender = false;
            }

            if (System.currentTimeMillis() - lastMs > 1000) {
                System.out.println(framesRendered + " fps; " + ticksProcessed + " tps");
                framesRendered = 0;
                ticksProcessed = 0;
                lastMs = System.currentTimeMillis();
            }
        }
    }

    private void render() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            requestFocus();
            return;
        }

        currentScreen.render();
        currentScreen.draw(pixels);

        Graphics g = bs.getDrawGraphics();
        g.fillRect(0, 0, getWidth(), getHeight());
        g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
        g.dispose();
        bs.show();
    }

    private void tick() {
        currentScreen.tick();
    }

    public static Screen getCurrentScreen() {
        return currentScreen;
    }

    public static void setCurrentScreen(Screen currentScreen) {
        if (Game.currentScreen != null) {
            Game.currentScreen.stop();
        }
        Game.currentScreen = currentScreen;
        currentScreen.start();
    }

    public static boolean isRunning() {
        return running;
    }
}
