package com.voracious.ld23;

import java.util.Random;

import com.voracious.ld23.gfx.Tile;
import com.voracious.ld23.tiles.BeginBridge;
import com.voracious.ld23.tiles.BottomTree;
import com.voracious.ld23.tiles.Bridge;
import com.voracious.ld23.tiles.CornerWall;
import com.voracious.ld23.tiles.CornerWater;
import com.voracious.ld23.tiles.Grass;
import com.voracious.ld23.tiles.InsideCornerWater;
import com.voracious.ld23.tiles.SideBridge;
import com.voracious.ld23.tiles.SideWall;
import com.voracious.ld23.tiles.SideWater;
import com.voracious.ld23.tiles.TallGrass;
import com.voracious.ld23.tiles.TopTree;
import com.voracious.ld23.tiles.Water;

public class World {
    public int[] map;
    public int[] damage;
    public boolean[][] flips;

    private int mapWidth;
    private int mapHeight;

    public World(int w, int h, long seed) {
        mapWidth = w;
        mapHeight = h;
        map = new int[w * h];
        damage = new int[w * h];
        flips = new boolean[w * h][3];
        generateWorld(seed);
    }

    public World(int w, int h) {
        mapWidth = w;
        mapHeight = h;
        map = new int[w * h];
        damage = new int[w * h];
        flips = new boolean[w * h][3];
        generateWorld();
    }

    public void generateWorld() {
        generateWorld(System.currentTimeMillis());
    }

    public void generateWorld(long seed) {
        Random rand = new Random();
        rand.setSeed(seed);

        generateGround(rand);
        generateRiver(rand);
        generateTrees(rand);
    }

    public void generateGround(Random rand) {
        for (int i = 0; i < map.length; i++) {
            int x = i % mapWidth;
            int y = i / mapWidth;
            if (x == 1 && y > 1 && y < mapHeight - 2) {
                map[i] = SideWall.ID;
            } else if (y == 1 && x < mapWidth - 2 && x > 1) {
                map[i] = SideWall.ID;
                flips[i][2] = true;
            } else if (x == mapWidth - 2 && y > 1 && y < mapHeight - 2) {
                map[i] = SideWall.ID;
                flips[i][0] = true;
            } else if (y == mapHeight - 2 && x > 1 && x < mapWidth - 2) {
                map[i] = SideWall.ID;
                flips[i][0] = true;
                flips[i][2] = true;
            } else if ((y == 1 || y == mapHeight - 2) && (x == 1 || x == mapWidth - 2)) {
                map[i] = CornerWall.ID;

                boolean[] flip = { false, false, false };

                if (x == mapWidth - 2 && y == 1) { //Special case for the upper-right corner
                    flip[0] = true;
                }

                if (mapWidth * (y + 1) + x < map.length && map[mapWidth * (y + 1) + x] == SideWall.ID) { //Never called because the next row isn't generated yet
                    if (mapWidth * y + (x - 1) >= 0 && map[mapWidth * y + (x - 1)] == SideWall.ID) {
                        flip[0] = true;
                    }
                } else if (mapWidth * (y - 1) + x >= 0 && map[mapWidth * (y - 1) + x] == SideWall.ID) {
                    flip[1] = true;
                    if (mapWidth * y + (x - 1) >= 0 && map[mapWidth * y + (x - 1)] == SideWall.ID) {
                        flip[0] = true;
                    }
                }
                flips[i] = flip;
            } else {
                if (rand.nextInt(100) == 0) {
                    map[i] = TallGrass.ID;
                } else {
                    map[i] = Grass.ID;
                    if (rand.nextInt(2) == 0) {
                        flips[i][0] = true;
                    }
                    if (rand.nextInt(2) == 0) {
                        flips[i][1] = true;
                    }
                }
            }
        }
    }

    public void generateRiver(Random rand) {
        int[] side = { 1, 0 };//TODO{ rand.nextInt(2), 0 };
        side[1] = side[0] + 2;
        int[] offset = { 0, 0 };
        while (Math.abs(offset[0] - offset[1]) < 6) {
            for (int i = 0; i < side.length; i++) {
                if (side[i] % 2 == 0) {
                    offset[i] = rand.nextInt(getMapHeight() - 12) + 6;
                } else {
                    offset[i] = rand.nextInt(getMapWidth() - 12) + 6;
                }
            }
        }

        int[][] x = { { 0, 0 }, { 0, 0 }, { 0, 0 } };
        int[][] y = { { 0, 0 }, { 0, 0 }, { 0, 0 } };
        for (int j = 0; j < side.length; j++) {
            int x0 = 0;
            int y0 = 0;
            switch (side[j]) {
            case 0:
                y0 = 0;
                x0 = -1;
                break;
            case 1:
                y0 = -1;
                x0 = getMapWidth() - 1;
                break;
            case 2:
                x0 = -1;
                y0 = getMapHeight() - 1;
                break;
            case 3:
                x0 = 0;
                y0 = -1;
                break;
            }

            if (x0 == -1) {
                x0 = offset[j];
                int multiplier = 1;
                if (side[j] == 2) {
                    multiplier = -1;
                }
                for (int k = 0; k < 2; k++) {
                    setTile(Water.ID, x0 + 1, y0 + (k * multiplier));
                }
                x0 += 1;
                y0 += multiplier;
            } else if (y0 == -1) {
                y0 = offset[j];
                int multiplier = 1;
                if (side[j] == 1) {
                    multiplier = -1;
                }
                for (int k = 0; k < 2; k++) {
                    setTile(Water.ID, x0 + (k * multiplier), y0 + 1);
                }
                y0 += 1;
                x0 += multiplier;
            }

            x[0][j] = x0;
            y[0][j] = y0;
        }

        boolean virtical;
        double slope = Math.abs((double) (y[0][1] - y[0][0]) / (double) (x[0][1] - x[0][0]));
        int midX = (x[0][0] + x[0][1]) / 2;
        int midY = (y[0][0] + y[0][1]) / 2;

        if (slope > 1.0) {
            virtical = true;
        } else {
            virtical = false;
        }

        x[2][1] = x[0][1];
        y[2][1] = y[0][1];
        int x1, y1;
        if (virtical) {
            midY += rand.nextInt(8) - 4;
            if (midY > getMapHeight() / 2) {
                y1 = midY - 7;
            } else {
                y1 = midY + 7;
            }
            x1 = midX;
        } else {
            midX += rand.nextInt(8) - 4;
            if (midX > getMapWidth() / 2) {
                x1 = midX - 7;
            } else {
                x1 = midX + 7;
            }
            y1 = midY;
        }

        double dist1 = Math.sqrt((x[0][1] - midX) * (x[0][1] - midX) + (y[0][1] - midY) * (y[0][1] - midY));
        double dist2 = Math.sqrt((x[0][1] - x1) * (x[0][1] - x1) + (y[0][1] - y1) * (y[0][1] - y1));

        if (dist2 > dist1) {
            int tempX = midX;
            int tempY = midY;

            midX = x1;
            midY = y1;
            x1 = tempX;
            y1 = tempY;
        }
        x[0][1] = x[1][0] = midX;
        y[0][1] = y[1][0] = midY;
        x[1][1] = x[2][0] = x1;
        y[1][1] = y[2][0] = y1;

        if (virtical) {
            for (int k = 0; k < y[1][1] - y[1][0]; k++) {
                setTile(Water.ID, x[1][0], y[1][0] + k);
            }
        } else {
            for (int k = 0; k < x[1][0] - x[1][1]; k++) {
                setTile(Water.ID, x[1][1] + k, y[1][0]);
            }

            generateBridge(x[1][1], y[1][0]);
        }

        for (int k = 0; k < 3; k++) {
            //Bresenham's Line Algorithm
            //Pseudo code credit Wikipedia
            int dx = Math.abs(x[k][1] - x[k][0]);
            int dy = Math.abs(y[k][1] - y[k][0]);
            int sx, sy;
            if (x[k][0] < x[k][1]) {
                sx = 1;
            } else {
                sx = -1;
            }
            if (y[k][0] < y[k][1]) {
                sy = 1;
            } else {
                sy = -1;
            }

            int err = dx - dy;

            while (x[k][0] != x[k][1] && y[k][0] != y[k][1]) {
                setTile(Water.ID, x[k][0], y[k][0]);

                int e2 = 2 * err;
                if (e2 > -1 * dy) {
                    err = err - dy;
                    x[k][0] += sx;
                }
                if (e2 < dx) {
                    err = err + dx;
                    y[k][0] = y[k][0] + sy;
                }
            }
            setTile(Water.ID, x[k][0], y[k][0]);
            //End line algorithm
        }

        for (int c = 0; c < getMapHeight(); c++) {
            for (int r = 0; r < getMapWidth(); r++) {
                if (getTile(r, c) == Water.ID) {
                    if (virtical) {
                        if (getTile(r, c + 1) == Water.ID || getTile(r, c - 1) == Water.ID) {
                            if (getTile(r - 1, c) < Water.ID) {
                                setTile(SideWater.ID, r - 1, c);
                                setTileFlips(r - 1, c, false, false, false);
                            }

                            if (getTile(r + 1, c) < Water.ID) {
                                setTile(SideWater.ID, r + 1, c);
                                setTileFlips(r + 1, c, true, false, false);
                            }

                            if (getTile(r + 1, c + 1) == Water.ID) {
                                setTile(InsideCornerWater.ID, r + 1, c);
                                setTileFlips(r + 1, c, false, false, false);
                                setTile(CornerWater.ID, r + 2, c);
                                setTileFlips(r + 2, c, false, true, true);

                                setTile(InsideCornerWater.ID, r, c + 1);
                                setTileFlips(r, c + 1, false, false, true);
                                setTile(CornerWater.ID, r - 1, c + 1);
                                setTileFlips(r - 1, c + 1, false, true, false);
                            }

                            if (getTile(r - 1, c + 1) == Water.ID) {
                                setTile(InsideCornerWater.ID, r - 1, c);
                                setTileFlips(r - 1, c, true, false, false);
                                setTile(CornerWater.ID, r - 2, c);
                                setTileFlips(r - 2, c, false, false, false);

                                setTile(InsideCornerWater.ID, r, c + 1);
                                setTileFlips(r, c + 1, false, true, false);
                                setTile(CornerWater.ID, r + 1, c + 1);
                                setTileFlips(r + 1, c + 1, true, true, true);
                            }
                        }
                    } else {
                        if (getTile(r + 1, c) == Water.ID || getTile(r - 1, c) == Water.ID) {
                            if (getTile(r, c - 1) < Water.ID) {
                                setTile(SideWater.ID, r, c - 1);
                                setTileFlips(r, c - 1, false, false, true);
                            }
                            if (getTile(r, c + 1) < Water.ID) {
                                setTile(SideWater.ID, r, c + 1);
                                setTileFlips(r, c + 1, true, false, true);
                            }
                        }

                        if (getTile(r + 1, c - 1) == Water.ID) {
                            setTile(InsideCornerWater.ID, r, c - 1);
                            setTileFlips(r, c - 1, true, false, false);
                            setTile(CornerWater.ID, r, c - 2);
                            setTileFlips(r, c - 2, false, false, false);

                            setTile(InsideCornerWater.ID, r + 1, c);
                            setTileFlips(r + 1, c, false, true, false);
                            setTile(CornerWater.ID, r + 1, c + 1);
                            setTileFlips(r + 1, c + 1, true, true, true);
                        }

                        if (getTile(r + 1, c + 1) == Water.ID) {
                            setTile(InsideCornerWater.ID, r + 1, c);
                            setTileFlips(r + 1, c, false, false, false);
                            setTile(CornerWater.ID, r + 1, c - 1);
                            setTileFlips(r + 1, c - 1, false, true, true);

                            setTile(InsideCornerWater.ID, r, c + 1);
                            setTileFlips(r, c + 1, true, true, false);
                            setTile(CornerWater.ID, r, c + 2);
                            setTileFlips(r, c + 2, true, false, true);
                        }
                    }
                }
            }
        }
    }

    /**
     * x and y are in tiles not pixels
     */
    public void generateBridge(int x, int y) {
        for (int k = -1; k < 2; k++) {
            setTile(SideBridge.ID, x + 2, y + k);
            setTileFlips(x + 2, y + k, true, false, false);
        }
        for (int k = -1; k < 2; k++) {
            setTile(SideBridge.ID, x + 5, y + k);
            setTileFlips(x + 5, y + k, false, false, false);
        }
        setTile(BeginBridge.ID, x + 5, y - 2);
        setTileFlips(x + 5, y - 2, false, true, false);
        setTile(BeginBridge.ID, x + 5, y + 2);
        setTileFlips(x + 5, y + 2, false, false, false);

        setTile(BeginBridge.ID, x + 2, y - 2);
        setTileFlips(x + 2, y - 2, true, true, false);
        setTile(BeginBridge.ID, x + 2, y + 2);
        setTileFlips(x + 2, y + 2, true, false, false);
        for (int k = -2; k < 3; k++) {
            setTile(Bridge.ID, x + 3, y + k);
            setTileFlips(x + 3, y + k, false, false, false);
            setTile(Bridge.ID, x + 4, y + k);
            setTileFlips(x + 4, y + k, false, false, false);
        }
    }

    public void generateTrees(Random rand) {
        for (int c = 1; c < getMapHeight(); c++) {
            for (int r = 0; r < getMapWidth(); r++) {
                if (getTile(r, c) < SideWall.ID && getTile(r, c - 1) < SideWall.ID && rand.nextInt(35) == 0) {
                    setTile(TopTree.ID, r, c - 1);
                    setTileFlips(r, c - 1, false, false, false);
                    setTile(BottomTree.ID, r, c);
                    setTileFlips(r, c, false, false, false);
                }
            }
        }
    }

    public void setTile(int id, int x, int y) {
        map[getMapWidth() * y + x] = id;
        damage[getMapWidth() * y + x] = Tile.getTileById(id).getDamage();
    }

    public int getTile(int x, int y) {
        if (getMapWidth() * y + x < map.length) {
            return map[getMapWidth() * y + x];
        } else {
            return -1;
        }
    }

    public int getTileDamage(int x, int y) {
        return damage[getMapWidth() * y + x];
    }

    public void setTileFlips(int x, int y, boolean flipX, boolean flipY, boolean rotate90) {
        flips[getMapWidth() * y + x][0] = flipX;
        flips[getMapWidth() * y + x][1] = flipY;
        flips[getMapWidth() * y + x][2] = rotate90;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public void attackTile(int x, int y, int power) {
        if (Tile.getTileById(getTile(x, y)).isBreakable()) {
            damage[getMapWidth() * y + x] -= power;
        }
    }

    public void breakTile(int x, int y) {
        breakTile(x, y, true);
    }

    public void breakTile(int x, int y, boolean playsound) {
        Tile.getTileById(getTile(x, y)).onBreak(this, x, y);
        setTile(Grass.ID, x, y);

        if (playsound) {
            Game.sounds.get("break").play();
        }
    }
}
