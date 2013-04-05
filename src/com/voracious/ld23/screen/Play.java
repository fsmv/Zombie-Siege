package com.voracious.ld23.screen;

import java.util.ArrayList;
import java.util.Random;

import com.voracious.ld23.Game;
import com.voracious.ld23.World;
import com.voracious.ld23.entities.Player;
import com.voracious.ld23.entities.Zombie;
import com.voracious.ld23.gfx.Entity;
import com.voracious.ld23.gfx.Screen;
import com.voracious.ld23.gfx.Sprite;
import com.voracious.ld23.gfx.Tile;

public class Play extends Screen {

    public Entity player;
    private ArrayList<Entity> entities;
    private World world;
    private Random rand;
    private boolean gameover = false;
    private Sprite gameoverImage;
    public static final int SCROLL_BOUNDRY_WIDTH = 32;
    public static final int MOB_LIMIT = 25;

    public Play(int width, int height) {
        super(width, height);
        gameoverImage = new Sprite(200, 150, "/gameover.png");
        entities = new ArrayList<Entity>();
        rand = new Random();
        player = new Player(this);
        entities.add(player);
        player.setY(38);//getHeight() / 2) - (player.getHeight() / 2));
        player.setX(38);//getWidth() / 2) - (player.getWidth() / 2));
    }

    @Override
    public void render() {
        clear(0xffffff);
        ArrayList<Tile> drawLaterTiles = new ArrayList<Tile>();
        ArrayList<int[]> drawLaterCoords = new ArrayList<int[]>();
        for (int i = 0; i < (getHeight() / Tile.SIZE) * (getWidth() / Tile.SIZE); i++) {
            int x = (i * Tile.SIZE) % getWidth();
            int y = Tile.SIZE * ((i * Tile.SIZE) / (getWidth()));
            if (x - getOffsetX() < Game.WIDTH && y - getOffsetY() < Game.HEIGHT && y + getOffsetY() >= 0 && x + getOffsetX() >= 0) {
                try {
                    Tile nextTile = Tile.getTileById(world.map[i]);
                    if (nextTile.shouldDrawFirst()) {
                        nextTile.draw(this, x, y, world.flips[i][0], world.flips[i][1], world.flips[i][2]);
                    } else {
                        int[] coordinates = { x, y };
                        drawLaterTiles.add(nextTile);
                        drawLaterCoords.add(coordinates);
                        nextTile.drawBackground(this, x, y, world.flips[i][0], world.flips[i][1], world.flips[i][2]);
                    }
                } catch (IllegalArgumentException e) {
                    if (Integer.parseInt(e.getMessage().substring(e.getMessage().length() - 1)) != 0) {
                        e.printStackTrace();
                        System.exit(1);
                    }
                }
            }
        }

        for(int i = 0; i < entities.size(); i++){
            entities.get(i).draw(this);
        }
        
        for(int i = 0; i < drawLaterTiles.size(); i++){
            Tile nextTile = drawLaterTiles.get(i);
            int[] coordinates = drawLaterCoords.get(i);
            nextTile.draw(this, coordinates[0], coordinates[1]);
        }
        
        if(gameover){
            gameoverImage.draw(this, getOffsetX(), getOffsetY());
        }
    }

    @Override
    public void tick() {
        for(int i = 0; i < entities.size(); i++){
            entities.get(i).tick();
        }
        
        tryMobSpawn();
        
        if (player.getY() < 0) {
            player.setY(0);
        } else if (player.getY() + player.getHeight() > getHeight()) {
            player.setY(getHeight() - player.getHeight());
        }
        if (player.getX() < 0) {
            player.setX(0);
        } else if (player.getX() + player.getWidth() > getWidth()) {
            player.setX(getWidth() - player.getWidth());
        }

        int x = (int)player.getX() - getOffsetX();
        int y = (int)player.getY() - getOffsetY();

        int newOffsetX = getOffsetX();
        int newOffsetY = getOffsetY();
        if (x < SCROLL_BOUNDRY_WIDTH) {
            newOffsetX = getOffsetX() - (SCROLL_BOUNDRY_WIDTH - x);
        }
        if (x > Game.WIDTH - SCROLL_BOUNDRY_WIDTH - player.getWidth()) {
            newOffsetX = getOffsetX() + (x - (Game.WIDTH - SCROLL_BOUNDRY_WIDTH - player.getWidth()));
        }

        if (y < SCROLL_BOUNDRY_WIDTH) {
            newOffsetY = getOffsetY() - (SCROLL_BOUNDRY_WIDTH - y);
        }
        if (y > Game.HEIGHT - SCROLL_BOUNDRY_WIDTH - player.getHeight()) {
            newOffsetY = getOffsetY() + (y - (Game.HEIGHT - SCROLL_BOUNDRY_WIDTH - player.getHeight()));
        }

        if (newOffsetX >= 0 && newOffsetX <= getWidth() - Game.WIDTH) {
            setOffsetX(newOffsetX);
        }
        if (newOffsetY >= 0 && newOffsetY <= getHeight() - Game.HEIGHT) {
            setOffsetY(newOffsetY);
        }
    }
    
    @SuppressWarnings("static-access")
    public boolean canMove(Entity e, int direction){
        return canMove(e, direction, e.WALK_SPEED);
    }
    
    public boolean canMove(Entity e, int direction, double walkspeed){
        double oldX = e.getX();
        double oldY = e.getY();
        boolean result = tryMove(e, direction, walkspeed);
        e.setX(oldX);
        e.setY(oldY);
        return result;
    }
    
    @SuppressWarnings("static-access")
    public boolean tryMove(Entity e, int direction){
        return tryMove(e, direction, e.WALK_SPEED);
    }

    /**
     * @param direction {up, right, down, left} = {0, 1, 2, 3}
     * @return true if moved false if did not
     */
    public boolean tryMove(Entity e, int direction, double walkSpeed) {
        boolean result = true;
        double oldX = e.getX();
        double oldY = e.getY();

        switch (direction) {
        case 0:
            e.setY(e.getY() - walkSpeed);
            break;
        case 1:
            e.setX(e.getX() + walkSpeed);
            break;
        case 2:
            e.setY(e.getY() + walkSpeed);
            break;
        case 3:
            e.setX(e.getX() - walkSpeed);
            break;
        default:
            throw new IllegalArgumentException("Direction must be between 0 and 3: " + direction);
        }

        for (int i = 0; i < world.map.length; i++) {
            if (Tile.getTileById(world.map[i]).isSolid()) {
                int x = (i * Tile.SIZE) % getWidth();
                int y = Tile.SIZE * ((i * Tile.SIZE) / (getWidth()));

                if (e.hitTest(Tile.getTileById(world.map[i]), x, y)) {
                    result = false;
                    break;
                }
            }
        }

        for(int i = 0; i < entities.size(); i++){
            Entity ent = entities.get(i);
            if(ent.hitTest(entities)){
                result = false;
            }
        }
        
        if(!result){
            e.setX(oldX);
            e.setY(oldY);
        }
        return result;
    }
    
    public void tryMobSpawn(){
        if(entities.size() < MOB_LIMIT + 1 && rand.nextInt(75) == 0){
            int x = rand.nextInt(world.getMapWidth() - 4) + 2;
            int y = rand.nextInt(world.getMapHeight() - 4) + 2;
            if(x < (getOffsetX())/Tile.SIZE || x > (getOffsetX() + Game.WIDTH)/Tile.SIZE){
                if(y < getOffsetY()/Tile.SIZE || y > (getOffsetY() + Game.HEIGHT)/Tile.SIZE){//if(off screen)
                    Zombie newMob = new Zombie(this);
                    newMob.setX(x*Tile.SIZE + Tile.SIZE/2 - newMob.getWidth()/2);
                    newMob.setY(y*Tile.SIZE + Tile.SIZE/2 - newMob.getHeight()/2);
                    if(!Tile.getTileById(world.getTile(x, y)).isSolid() && !Tile.getTileById(world.getTile(x, y - 1)).isSolid() && !Tile.getTileById(world.getTile(x, y + 1)).isSolid()){
                        newMob.setCurrentAnimation(rand.nextInt(4));
                        newMob.play();
                        entities.add(newMob);
                        System.out.println(entities.size() + " Zombie Spawn " + x + ", " + y);
                    }
                }
            }
        }
    }
    
    public void gameover(){
        stop();
        gameover = true;
    }
    
    public World getWorld(){
        return world;
    }
    
    public ArrayList<Entity> getEntities(){
        return entities;
    }

    public void start() {
        world = new World(getWidth() / Tile.SIZE, getHeight() / Tile.SIZE);
    }
}
