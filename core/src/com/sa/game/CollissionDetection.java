package com.sa.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class CollissionDetection {
    static CollissionData collidesWithGround(Rectangle rectangle, Vector2 velocity, StaticEnvironment staticEnvironment) {
        Rectangle destRectangle = new Rectangle();
        destRectangle.set(rectangle);
        Vector2 center = new Vector2();
        rectangle.getCenter(center);
        center.add(velocity.x/(float)staticEnvironment.tileSizeInPixels, velocity.y/(float)staticEnvironment.tileSizeInPixels);
        destRectangle.setCenter(center);

        int minx = Math.max(0, (int)Math.floor(destRectangle.x/staticEnvironment.tileSizeInPixels));
        int miny = Math.max(0, (int)Math.floor(destRectangle.y/staticEnvironment.tileSizeInPixels));

        int maxx = Math.min(staticEnvironment.getMapWidth(), (int)Math.ceil((destRectangle.x+destRectangle.width)/staticEnvironment.tileSizeInPixels));
        int maxy = Math.min(staticEnvironment.getMapHeight(), (int)Math.floor((destRectangle.y+destRectangle.height)/staticEnvironment.tileSizeInPixels));
        boolean didCollide = false;
        float move = 0.0f;
        
        int y = maxy;
        {
            for(int x = minx; x < maxx; x++) {
                if(staticEnvironment.getTileId(0, x, y) != 0) {
                    //We did collide with the tile.
                    //Narrow to just the upper part of the tile.
                    if((rectangle.y + rectangle.height) <= (y * staticEnvironment.tileSizeInPixels))
                    {
                        didCollide = true;
                        move = (rectangle.y + rectangle.height) - (y * staticEnvironment.tileSizeInPixels);
                        break;
                    }
                }
            }
        }
        return new CollissionData(didCollide, new Vector2(0f, move));
    }

    static CollissionData colidesWithWalls(Rectangle rectangle, Vector2 velocity, StaticEnvironment staticEnvironment) {
        Rectangle destRectangle = new Rectangle();
        destRectangle.set(rectangle);
        Vector2 center = new Vector2();
        rectangle.getCenter(center);
        center.add(velocity.x/(float)staticEnvironment.tileSizeInPixels, velocity.y/(float)staticEnvironment.tileSizeInPixels);
        destRectangle.setCenter(center);

        int minx = Math.max(0, (int)Math.floor(destRectangle.x/staticEnvironment.tileSizeInPixels));
        int miny = Math.max(0, (int)Math.floor(destRectangle.y/staticEnvironment.tileSizeInPixels));

        int maxx = Math.min(staticEnvironment.getMapWidth(), (int)Math.ceil((destRectangle.x+destRectangle.width)/staticEnvironment.tileSizeInPixels));
        int maxy = Math.min(staticEnvironment.getMapHeight(), (int)Math.ceil((destRectangle.y+destRectangle.height)/staticEnvironment.tileSizeInPixels));
        boolean didCollide = false;
        float move = 0.0f;

        for(int y = miny; y < maxy; y++) {
            if(didCollide)
                break;
            for(int x = minx; x < maxx; x++) {
                if(staticEnvironment.getTileId(1, x, y) != 0) {
                    //We did collide with the tile.
                    //Narrow dow the test to only collide if source is on the left or on the right side of the tile.
                    if (rectangle.x <= x * staticEnvironment.tileSizeInPixels && (rectangle.x + rectangle.width) <= x * staticEnvironment.tileSizeInPixels) {
                        //src is on the left side
                        //Only collide if there is no floor to the left.
                        boolean floorToTheLeft = false;
                        if(x > 0 && staticEnvironment.getTileId(0, x-1, y) != 0) {
                            floorToTheLeft = true;
                        }
                        if(!floorToTheLeft) {
                            didCollide = true;
                            move = (rectangle.x + rectangle.width) - x * staticEnvironment.tileSizeInPixels;
                            break;
                        }
                    }
                    if (rectangle.x >= (staticEnvironment.tileSizeInPixels + x * staticEnvironment.tileSizeInPixels) && (rectangle.x + rectangle.width) >= (staticEnvironment.tileSizeInPixels + x * staticEnvironment.tileSizeInPixels)) {
                        //src is on the right side
                        //Only collide if there is no floor to the left.
                        boolean floorToTheRight = false;
                        if(staticEnvironment.getTileId(0, x+1, y) != 0) {
                            floorToTheRight = true;
                        }
                        if(!floorToTheRight) {
                            didCollide = true;
                            move = rectangle.x - (staticEnvironment.tileSizeInPixels + x * staticEnvironment.tileSizeInPixels);
                            break;
                        }
                    }
                }
            }
        }
        return new CollissionData(didCollide, new Vector2(move, 0f));
    }
}
