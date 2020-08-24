package com.sa.game;


import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;
import com.sa.game.collision.CollisionDetection;
import com.sa.game.entities.Enemies;
import com.sa.game.entities.Enemy;
import com.sa.game.gfx.EnemyAnimations;

public class StaticEnvironment {
    public enum TileId {
        Visible,
        Floor,
        Wall
    }

    public TiledMap tiledMap;

    public int tileSizeInPixels;

    public StaticEnvironment() {
    }

     public  TiledMap getMap() {
        return tiledMap;
    }

    public void setTiledLevel(String file, Enemies enemies, CollisionDetection collisionDetection) {
        tiledMap = new TmxMapLoader().load(file);

        TiledMapTileLayer inLayer =  (TiledMapTileLayer)tiledMap.getLayers().get("base");
        tileSizeInPixels = (int)inLayer.getTileWidth();

        TiledMapTileLayer floorLayer = new TiledMapTileLayer(inLayer.getWidth(), inLayer.getHeight(), tileSizeInPixels, tileSizeInPixels);
        floorLayer.setName("floor");
        tiledMap.getLayers().add(floorLayer);
        TiledMapTileLayer wallLayer = new TiledMapTileLayer(inLayer.getWidth(), inLayer.getHeight(), tileSizeInPixels, tileSizeInPixels);
        wallLayer.setName("wall");
        tiledMap.getLayers().add(wallLayer);

        for(int y = 0; y < inLayer.getHeight(); y++) {
            for(int x = 0; x < inLayer.getWidth(); x++) {
                Cell incell = inLayer.getCell(x, y);
                if (incell == null)
                    continue;
                int tileId = incell.getTile().getId();

                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();

                cell.setTile(tiledMap.getTileSets().getTileSet(0).getTile(tileId));

                cell.getTile().setId(tileId);
                if(y < inLayer.getHeight()-1 && inLayer.getCell(x, y+1) == null)
                    floorLayer.setCell(x, y, cell);
                int gumEmptySurrounding = 0;
                if(x > 0 && inLayer.getCell(x-1, y) == null)
                    gumEmptySurrounding++;
                if(x < inLayer.getWidth()-1 && inLayer.getCell(x+1, y) == null)
                    gumEmptySurrounding++;
                int numSolidSorounding = 0;
                if(y > 0 && inLayer.getCell(x, y-1) != null)
                    numSolidSorounding++;
                if(y < inLayer.getHeight()-1 && inLayer.getCell(x, y+1) != null)
                    numSolidSorounding++;
                if(gumEmptySurrounding > 0  && numSolidSorounding > 0 && inLayer.getCell(x, y) != null) {
                    wallLayer.setCell(x, y, cell);
                    System.out.print(x);
                    System.out.print(", ");
                    System.out.println(y);
                }
            }
        }

        for(MapObject mapObject : tiledMap.getLayers().get("enemies").getObjects()) {
            if(mapObject.getProperties().get("type", String.class).equals("clown")) {
                RectangleMapObject rectangleMapObject = (RectangleMapObject)mapObject;
                Vector2 center = new Vector2();
                enemies.add(new Enemy(
                        "clown",
                        rectangleMapObject.getRectangle().getCenter(center),
                        rectangleMapObject.getRectangle().height,
                        new EnemyAnimations("clown.atlas"),
                        this,
                        collisionDetection
                ));
            }
        }

    }

    public String getLayerName(TileId t) {
        if(t == TileId.Visible) return "base";
        if(t == TileId.Floor) return "floor";
        else if(t == TileId.Wall) return "wall";
        else return "";
    }

    public int getLayerIndex(TileId t) {
        return getMap().getLayers().getIndex(getLayerName(t));
    }

    public int getTileId(TileId layer, int x, int y) {
        TiledMapTileLayer mapLayer = (TiledMapTileLayer)getMap().getLayers().get(getLayerName(layer));
        TiledMapTileLayer.Cell cell = mapLayer.getCell(x, y);
        int id = 0;
        if(cell != null)
            id = cell.getTile().getId();

        return id;
    }

    public int getTileIdFromWorldCoordinate(TileId layer, Vector2 pos) {
        TiledMapTileLayer mapLayer = (TiledMapTileLayer)getMap().getLayers().get(getLayerName(layer));
        int x = (int)(pos.x/mapLayer.getTileWidth());
        int y = (int)(pos.y/mapLayer.getTileHeight());
        return getTileId(layer, x, y);
    }

    public int getNumTilesX() {
        if(getMap().getLayers() == null || getMap().getLayers().getCount() <= 0)
            return 0;
        TiledMapTileLayer layer = (TiledMapTileLayer)getMap().getLayers().get("base");
        return layer.getWidth();
    }

    public int getNumTilesY() {
        if(getMap().getLayers() == null || getMap().getLayers().getCount() <= 0) return 0;
        TiledMapTileLayer layer = (TiledMapTileLayer)getMap().getLayers().get("base");
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
        if(getMap().getLayers() == null || getMap().getLayers().getCount() <= 0) return 0;
        TiledMapTileLayer layer = (TiledMapTileLayer)getMap().getLayers().get("base");

        return getNumTilesY()*layer.getTileHeight();
    }
}
