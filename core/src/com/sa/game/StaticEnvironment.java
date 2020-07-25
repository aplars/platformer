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
        TiledMapTileLayer wallLayer = new TiledMapTileLayer(mapWidth, mapHeight, tileSizeInPixels, tileSizeInPixels);
        map.getLayers().add(wallLayer);
        TiledMapTileLayer visibleLayer = new TiledMapTileLayer(mapWidth, mapHeight, tileSizeInPixels, tileSizeInPixels);
        map.getLayers().add(visibleLayer);
    }

    public void setLevel(String levelDataInverted[]) {
        String levelData[] = new String[levelDataInverted.length];
        for(int i = 0; i < levelDataInverted.length; i++) {
            levelData[levelDataInverted.length - 1- i] = levelDataInverted[i];
        }

        TiledMapTileLayer floorLayer = (TiledMapTileLayer)map.getLayers().get(0);
        TiledMapTileLayer wallLayer = (TiledMapTileLayer)map.getLayers().get(1);
        TiledMapTileLayer visibleLayer = (TiledMapTileLayer)map.getLayers().get(2);

        for(int y = 0; y < levelData.length; y++) {
            for(int x = 0; x < levelData[y].length(); x++) {
                int tileId = Character.getNumericValue(levelData[y].charAt(x));
                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                cell.setTile(tileSet.getTile(tileId));
                cell.getTile().setId(tileId);
                if(y < levelData.length-1 && Character.getNumericValue(levelData[y].charAt(x)) != 0 && Character.getNumericValue(levelData[y+1].charAt(x)) == 0)
                    floorLayer.setCell(x, y, cell);
                int numEmptySorounding = 0;
                if(x > 0 && Character.getNumericValue(levelData[y].charAt(x-1)) == 0)
                    numEmptySorounding++;
                if(x < levelData[y].length()-1 && Character.getNumericValue(levelData[y].charAt(x+1)) == 0)
                    numEmptySorounding++;
                int numSolidSorounding = 0;
                if(y > 0 && Character.getNumericValue(levelData[y-1].charAt(x)) != 0)
                    numSolidSorounding++;
                if(y < levelData.length-1 && Character.getNumericValue(levelData[y+1].charAt(x)) != 0)
                    numSolidSorounding++;
                if(numEmptySorounding > 0  && numSolidSorounding > 0 && Character.getNumericValue(levelData[y].charAt(x)) != 0) {
                    wallLayer.setCell(x, y, cell);
                    System.out.print(x);
                    System.out.print(", ");
                    System.out.println(y);
                }

                visibleLayer.setCell(x, y, cell);
            }
        }
    }

    public int getTileId(int layer, int x, int y) {
        TiledMapTileLayer mapLayer = (TiledMapTileLayer)map.getLayers().get(layer);
        TiledMapTileLayer.Cell cell = mapLayer.getCell(x, y);
        int id = 0;
        if(cell != null)
            id = cell.getTile().getId();
        return id;
    }

    public int getMapWidth() {
        if(map.getLayers() == null || map.getLayers().getCount() <= 0)
            return 0;
        TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get(0);
        return layer.getWidth();
    }

    public int getMapHeight() {
        if(map.getLayers() == null || map.getLayers().getCount() <= 0) return 0;
        TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get(0);
        return layer.getHeight();
    }
}
