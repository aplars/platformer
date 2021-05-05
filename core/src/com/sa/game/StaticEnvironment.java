package com.sa.game;

import java.util.ArrayList;
import java.util.EnumMap;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.sa.game.collision.CollisionDetection;

public class StaticEnvironment {
    public enum LayerId {
        Visible,
        Floor,
        Wall,
        LeftWall,
        RightWall,
        Back
    }

    public class Entity {
        public String name;
        public Vector2 position = new Vector2();
        public Vector2 size = new Vector2();

        public Entity(final String name, final Vector2 position, final Vector2 size) {
            this.name = name;
            this.position.set(position);
            this.size.set(size);
        }
    }

    public ArrayList<Entity> entities = new ArrayList<>();

    public TiledMap tiledMap;

    public int tileSizeInPixels;

    public StaticEnvironment() {}

    public StaticEnvironment(final TiledMap tiledMap) {
        setTiledLevel(tiledMap);
    }

    public void dispose() {
        if(tiledMap != null)
            tiledMap.dispose();
        tiledMap = null;
    }

     public  TiledMap getMap() {
        return tiledMap;
    }

    public void setTiledLevel(final TiledMap tiledMap) {
        if(this.tiledMap != null)
            this.tiledMap.dispose();
        this.tiledMap = null;
        this.entities.clear();
        

        this.tiledMap = tiledMap;
        final TiledMapTileLayer inLayer =  (TiledMapTileLayer)tiledMap.getLayers().get("base");
        tileSizeInPixels = (int)inLayer.getTileWidth();

        final TiledMapTileLayer floorLayer = new TiledMapTileLayer(inLayer.getWidth(), inLayer.getHeight(), tileSizeInPixels, tileSizeInPixels);
        floorLayer.setName("floor");
        tiledMap.getLayers().add(floorLayer);
        final TiledMapTileLayer wallLayer = new TiledMapTileLayer(inLayer.getWidth(), inLayer.getHeight(), tileSizeInPixels, tileSizeInPixels);
        wallLayer.setName("wall");
        tiledMap.getLayers().add(wallLayer);
        final TiledMapTileLayer leftWallLayer = new TiledMapTileLayer(inLayer.getWidth(), inLayer.getHeight(), tileSizeInPixels, tileSizeInPixels);
        leftWallLayer.setName("leftwall");
        tiledMap.getLayers().add(leftWallLayer);
        final TiledMapTileLayer rightWallLayer = new TiledMapTileLayer(inLayer.getWidth(), inLayer.getHeight(), tileSizeInPixels, tileSizeInPixels);
        rightWallLayer.setName("rightwall");
        tiledMap.getLayers().add(rightWallLayer);

        for(int y = 0; y < inLayer.getHeight(); y++) {
            for(int x = 0; x < inLayer.getWidth(); x++) {
                final Cell incell = inLayer.getCell(x, y);
                if (incell == null)
                    continue;
                final int tileId = incell.getTile().getId();

                final TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                cell.setTile(tiledMap.getTileSets().getTileSet(0).getTile(tileId));

                cell.getTile().setId(tileId);
                if(y < inLayer.getHeight()-1 && inLayer.getCell(x, y+1) == null)
                    floorLayer.setCell(x, y, cell);

                boolean emptyLeft = false;
                if(x > 0 && inLayer.getCell(x-1, y) == null)
                    emptyLeft = true;
                boolean emptyRight = false;
                if(x < inLayer.getWidth()-1 && inLayer.getCell(x+1, y) == null)
                    emptyRight = true;
                if(emptyLeft && emptyRight && inLayer.getCell(x, y) != null) {
                    wallLayer.setCell(x, y, cell);
                }
                else if(emptyLeft) {
                    rightWallLayer.setCell(x, y, cell);
                }
                else if(emptyRight) {
                    leftWallLayer.setCell(x, y, cell);
                }
            }
        }

        for (final MapLayer mapLayer : tiledMap.getLayers()) {
            for (final MapObject mapObject : mapLayer.getObjects()) {

                if (mapObject.isVisible()) {
                    final RectangleMapObject rectangleMapObject = (RectangleMapObject) mapObject;
                    final Vector2 center = new Vector2();
                    final Vector2 size = new Vector2();
                    entities.add(new Entity(mapObject.getProperties().get("type", String.class),
                            rectangleMapObject.getRectangle().getCenter(center),
                            rectangleMapObject.getRectangle().getSize(size)));
                }
            }
        }
    }

    public String getLayerName(final LayerId t) {
        if(t == LayerId.Visible) return "base";
        if(t == LayerId.Floor) return "floor";
        else if(t == LayerId.Wall) return "wall";
        else if(t == LayerId.LeftWall) return "leftwall";
        else if(t == LayerId.RightWall) return "rightwall";
        else if(t == LayerId.Back) return "back";
        else return "";
    }

    public int getLayerIndex(final LayerId t) {
        return getMap().getLayers().getIndex(getLayerName(t));
    }

    public int getTileId(final LayerId layer, final int x, final int y) {
        final TiledMapTileLayer mapLayer = (TiledMapTileLayer)getMap().getLayers().get(getLayerName(layer));
        final TiledMapTileLayer.Cell cell = mapLayer.getCell(x, y);
        int id = 0;
        if(cell != null)
            id = cell.getTile().getId();

        return id;
    }

    public int getTileIdFromWorldCoordinate(final LayerId layer, final Vector2 pos) {
        final TiledMapTileLayer mapLayer = (TiledMapTileLayer)getMap().getLayers().get(getLayerName(layer));
        final int x = (int)(pos.x/mapLayer.getTileWidth());
        final int y = (int)(pos.y/mapLayer.getTileHeight());
        return getTileId(layer, x, y);
    }

    public GridPoint2 getGridPointFromWorldCoordinate(final LayerId layer, final Vector2 pos, final GridPoint2 gridPnt) {
        final TiledMapTileLayer mapLayer = (TiledMapTileLayer)getMap().getLayers().get(getLayerName(layer));
        final int x = (int)(pos.x/mapLayer.getTileWidth());
        final int y = (int)(pos.y/mapLayer.getTileHeight());
        gridPnt.set(x, y);
        return gridPnt;
    }

    public int getNumTilesX() {
        if(getMap() == null) return 0;
        if(getMap().getLayers() == null || getMap().getLayers().getCount() <= 0)
            return 0;
        final TiledMapTileLayer layer = (TiledMapTileLayer)getMap().getLayers().get("base");
        return layer.getWidth();
    }

    public int getNumTilesY() {
        if(getMap() == null) return 0;
        if(getMap().getLayers() == null || getMap().getLayers().getCount() <= 0) return 0;
        final TiledMapTileLayer layer = (TiledMapTileLayer)getMap().getLayers().get("base");
        return layer.getHeight();
    }

    public int getWidth() {
        return getNumTilesX() * tileSizeInPixels;
    }

    public int getHeight() {
        return getNumTilesY() * tileSizeInPixels;
    }
    /**
     *
     * @return Where the world ends in world coordinates.
     */
    public float getWorldBoundY() {
        if (getMap().getLayers() == null || getMap().getLayers().getCount() <= 0)
            return 0;
        final TiledMapTileLayer layer = (TiledMapTileLayer) getMap().getLayers().get("base");

        return getNumTilesY() * layer.getTileHeight();
    }

    /**
     *
     * @return Where the world ends in world coordinates.
     */
    public float getWorldBoundX() {
        if(getMap().getLayers() == null || getMap().getLayers().getCount() <= 0) return 0;
        final TiledMapTileLayer layer = (TiledMapTileLayer)getMap().getLayers().get("base");

        return getNumTilesX()*layer.getTileWidth();
    }
}
