package com.sa.game;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;

public class StaticEnvironment {
    public TiledMap map = new TiledMap();
    public int tileSizeInPixels;
    TiledMapTileSet tileSet = new TiledMapTileSet();

    public StaticEnvironment(String tileSetPath, int tileSizeInPixels, int mapWidth, int mapHeight) {
        this.tileSizeInPixels = tileSizeInPixels;
        Texture texture = new Texture(tileSetPath);
        TextureRegion[][] textureRegion = TextureRegion.split(texture, tileSizeInPixels, tileSizeInPixels);
        int tileSetWidth = texture.getWidth()/tileSizeInPixels;
        int tileSetHeight = texture.getHeight()/tileSizeInPixels;
        int tileId  = 0;
        for(int col = 0; col < tileSetHeight; col++) {
            for(int row = 0; row < tileSetWidth; row++) {
                final StaticTiledMapTile tile = new StaticTiledMapTile(textureRegion[col][row]);

                tileSet.putTile(tileId++, tile);
            }
        }

        TiledMapTileLayer floorLayer = new TiledMapTileLayer(mapWidth, mapHeight, tileSizeInPixels, tileSizeInPixels);
        map.getLayers().add(floorLayer);
        for(int x = 0; x < mapWidth; x++) {
            for(int y = 0; y < mapHeight; y++) {
                //TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                //cell.setTile(tileSet.getTile(0));
                //floorLayer.setCell(x, y, cell);
            }
        }
        TiledMapTileLayer wallLayer = new TiledMapTileLayer(mapWidth, mapHeight, tileSizeInPixels, tileSizeInPixels);
        map.getLayers().add(wallLayer);
        for(int x = 0; x < mapWidth; x++) {
            for(int y = 0; y < mapHeight; y++) {
                //TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                //cell.setTile(tileSet.getTile(0));
                //wallLayer.setCell(x, y, cell);
            }
        }
    }

    void setLevel(String levelData[]) {
        TiledMapTileLayer floorLayer = (TiledMapTileLayer)map.getLayers().get(0);
        TiledMapTileLayer wallLayer = (TiledMapTileLayer)map.getLayers().get(1);
        for(int y = 0; y < levelData.length; y++) {
            for(int x = 0; x < levelData[y].length(); x++) {
                int tileId = Character.getNumericValue(levelData[y].charAt(x));
                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                cell.setTile(tileSet.getTile(tileId));
                cell.getTile().setId(tileId);
                if(y > 0 && Character.getNumericValue(levelData[y].charAt(x)) != 0 && Character.getNumericValue(levelData[y-1].charAt(x)) == 0)
                    floorLayer.setCell(x, y, cell);
                int numSorounding = 0;
                if(x > 0 && Character.getNumericValue(levelData[y].charAt(x-1)) == 0)
                    numSorounding++;
                if(x < levelData[y].length()-1 && Character.getNumericValue(levelData[y].charAt(x+1)) == 0)
                    numSorounding++;
                if(numSorounding > 0 && Character.getNumericValue(levelData[y].charAt(x)) != 0)
                    wallLayer.setCell(x, y, cell);
            }
        }
    }

    int getTileId(int layer, int x, int y) {
        TiledMapTileLayer mapLayer = (TiledMapTileLayer)map.getLayers().get(layer);
        TiledMapTileLayer.Cell cell = mapLayer.getCell(x, y);
        int id = 0;
        if(cell != null)
            id = cell.getTile().getId();
        return id;
    }

    int getMapWidth() {
        if(map.getLayers() == null || map.getLayers().getCount() <= 0)
            return 0;
        TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get(0);
        return layer.getWidth();
    }

    int getMapHeight() {
        if(map.getLayers() == null || map.getLayers().getCount() <= 0) return 0;
        TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get(0);
        return layer.getHeight();
    }
}
