package org.maxgamer.sticks.core.world;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class Zone {
    private static final byte COLLIDE_NONE = 0x00;
    private static final byte COLLIDE_GROUND = 0x01;

    private static final byte[] MARKER_TILES = {
            COLLIDE_NONE,
            COLLIDE_GROUND
    };

    private TiledMap map;
    private byte[][] collision;

    public Zone(TiledMap map, TiledMap collisionMap) {
        this.map = map;

        MapProperties properties = map.getProperties();
        this.collision = new byte[properties.get("width", Integer.class)][properties.get("height", Integer.class)];

        TiledMapTileLayer collisionLayer = (TiledMapTileLayer) collisionMap.getLayers().get("Collision");
        int[] collisonData = new int[collisionLayer.getWidth() * collisionLayer.getHeight()];
        for (int i = 0; i < collisionLayer.getWidth(); i++) {
            for (int j = 0; j < collisionLayer.getHeight(); j++) {
                TiledMapTileLayer.Cell cell = collisionLayer.getCell(i, j);
                if (cell == null) {
                    continue;
                }

                int tileId = cell.getTile().getId();

                int cellX = collisionLayer.getHeight() - j - 1;
                int cellY = i;

                collisonData[cellX * collisionLayer.getWidth() + cellY] = tileId;
            }
        }

        for (int i = 0; i < collision.length; i++) {
            for (int j = 0; j < collision[i].length; j++) {

                for (MapLayer mapLayer : map.getLayers()) {
                    TiledMapTileLayer tiledMapTileLayer = (TiledMapTileLayer) mapLayer;
                    TiledMapTileLayer.Cell cell = tiledMapTileLayer.getCell(i, j);

                    if (cell == null) {
                        continue;
                    }

                    int tileId = cell.getTile().getId();

                    if (tileId >= collisonData.length) {
                        // Might cause a bug
                        continue;
                    }

                    int type = collisonData[tileId];
                    byte flag = MARKER_TILES[type];

                    collision[i][j] |= flag;
                }
            }
        }
    }

    public boolean isCollision(int x, int y) {
        try {
            return (this.collision[x][y] & COLLIDE_GROUND) != 0;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }
}
