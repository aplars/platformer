package com.sa.game.collision;


import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sa.game.StaticEnvironment;

public class IntersectionTests {
    public static FloorCollisionData rectangleGround(final float dt, final Rectangle rectangle, final Vector2 velocity, final StaticEnvironment staticEnvironment) {
        Rectangle destRectangle = new Rectangle();
        destRectangle.set(rectangle);
        Vector2 center = new Vector2();
        rectangle.getCenter(center);
        center.add(velocity.x*dt, velocity.y*dt);
        destRectangle.setCenter(center);

        int minx = Math.max(0, (int)Math.floor(destRectangle.x/staticEnvironment.tileSizeInPixels));
        int miny = Math.max(0, (int)Math.floor(destRectangle.y/staticEnvironment.tileSizeInPixels));
        int maxx = Math.min(staticEnvironment.getNumTilesX(), (int)Math.ceil((destRectangle.x+destRectangle.width)/staticEnvironment.tileSizeInPixels));
        int maxy = Math.min(staticEnvironment.getNumTilesY(), (int)Math.ceil((destRectangle.y+destRectangle.height)/staticEnvironment.tileSizeInPixels));

        boolean didCollide = false;
        float move = 0.0f;
        boolean breakLoop = false;
        for(int y = miny; y < maxy; y++) {
            if(breakLoop)
                break;
            for(int x = minx; x < maxx; x++) {
                if(staticEnvironment.getTileId(StaticEnvironment.TileId.Floor, x, y) != 0) {
                    //We did collide with the tile.
                    //Narrow to just the upper part of the tile.
                    if(destRectangle.y <= ((y+1) * staticEnvironment.tileSizeInPixels) &&
                            rectangle.y >= ((y+1) * staticEnvironment.tileSizeInPixels))
                    {
                        didCollide = true;
                        move = ((y+1) * staticEnvironment.tileSizeInPixels)-(rectangle.y);
                        breakLoop = true;
                        break;
                    }
                }
            }
        }

        //If the src rect is in a tile then we do not collide
        breakLoop = false;
        if(didCollide) {
            minx = Math.max(0, (int) Math.floor(rectangle.x / staticEnvironment.tileSizeInPixels));
            miny = Math.max(0, (int) Math.floor(rectangle.y / staticEnvironment.tileSizeInPixels));
            maxx = Math.min(staticEnvironment.getNumTilesX(), (int) Math.ceil((rectangle.x + rectangle.width) / staticEnvironment.tileSizeInPixels));
            maxy = Math.min(staticEnvironment.getNumTilesY(), (int) Math.ceil((rectangle.y + rectangle.height) / staticEnvironment.tileSizeInPixels));
            for (int y = miny; y < maxy; y++) {
                if (breakLoop)
                    break;
                for (int x = minx; x < maxx; x++) {
                    if(staticEnvironment.getTileId(StaticEnvironment.TileId.Wall, x, y) != 0) {
                        didCollide = false;
                        breakLoop = true;
                        break;
                    }
                }
            }
        }
        return new FloorCollisionData(didCollide, new Vector2(0f, move));
    }

    public static WallCollisionData rectangleWalls(final float dt, final Rectangle rectangle, final Vector2 velocity, final StaticEnvironment staticEnvironment) {
        Rectangle destRectangle = new Rectangle();
        destRectangle.set(rectangle);
        Vector2 center = new Vector2();
        rectangle.getCenter(center);
        center.add(velocity.x*dt, velocity.y*dt);
        destRectangle.setCenter(center);

        int minx = Math.max(0, (int)Math.floor(destRectangle.x/(float) staticEnvironment.tileSizeInPixels));
        int miny = Math.max(0, (int)Math.floor(destRectangle.y/(float)staticEnvironment.tileSizeInPixels));
        int maxx = Math.min(staticEnvironment.getNumTilesX(), (int)Math.ceil((destRectangle.x+destRectangle.width)/(float)staticEnvironment.tileSizeInPixels));
        int maxy = Math.min(staticEnvironment.getNumTilesY(), (int)Math.ceil((destRectangle.y+destRectangle.height)/(float)staticEnvironment.tileSizeInPixels));
        boolean didCollide = false;
        float move = 0.0f;

        for(int y = miny; y < maxy; y++) {
            if(didCollide)
                break;
            for(int x = minx; x < maxx; x++) {
                if(staticEnvironment.getTileId(StaticEnvironment.TileId.Wall, x, y) != 0) {
                    //We did collide with the tile.
                    //Narrow down the test to only collide if source is on the left or on the right side of the tile.
                    if (rectangle.x <= x * staticEnvironment.tileSizeInPixels && (rectangle.x + rectangle.width) <= x * staticEnvironment.tileSizeInPixels) {
                        //src is on the left side
                        //Only collide if there is no floor to the left.
                        boolean floorToTheLeft = false;
                        if(x > 0 && staticEnvironment.getTileId(StaticEnvironment.TileId.Floor, x-1, y) != 0) {
                            floorToTheLeft = true;
                        }
                        if(!floorToTheLeft) {
                            didCollide = true;
                            move = (rectangle.x + rectangle.width) - x * staticEnvironment.tileSizeInPixels;
                            break;
                        }
                    }
                    else if (rectangle.x >= (staticEnvironment.tileSizeInPixels + x * staticEnvironment.tileSizeInPixels) && (rectangle.x + rectangle.width) >= (staticEnvironment.tileSizeInPixels + x * staticEnvironment.tileSizeInPixels)) {
                        //src is on the right side
                        //Only collide if there is no floor to the left.
                        boolean floorToTheRight = false;
                        if(staticEnvironment.getTileId(StaticEnvironment.TileId.Floor, x+1, y) != 0) {
                            floorToTheRight = true;
                        }
                        if(!floorToTheRight) {
                            didCollide = true;
                            move = rectangle.x - (staticEnvironment.tileSizeInPixels + x * staticEnvironment.tileSizeInPixels);
                            break;
                        }
                    }
                    else {

                    }
                }
            }
        }
        return new WallCollisionData(didCollide, new Vector2(move, 0f));
    }

    public static RectangleCollisionData rectangleRectangle(Rectangle recta, Vector2 va, Rectangle rectb) {
        RectangleCollisionData rectangleCollisionData = new RectangleCollisionData();

        //Extend the static rectangle with the extends of the moving one.
        Rectangle rect = new Rectangle();
        rect.set(recta);
        rect.width += rectb.width/2f;
        rect.height += rectb.height/2f;
        Vector2 centerb = new Vector2();
        rect.setCenter(rectb.getCenter(centerb));

        //Do a line segment vs rectangle test.
        Vector2 center = new Vector2();
        center = recta.getCenter(center);

        rectangleCollisionData.didCollide = Intersector.intersectSegmentRectangle(center, center.add(va), rect);

        return rectangleCollisionData;
    }

    public static RectangleCollisionData rectangleRectangle(Rectangle ra, Vector2 va, Rectangle rb, Vector2 vb) {
        Vector2 v = new Vector2();
        v.set(va);
        v = v.sub(vb);
        return rectangleRectangle(ra, v, rb);
    }
}