package com.sa.game.collision;


import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sa.game.StaticEnvironment;

public class IntersectionTests {
    public static void rectangleGround(final float dt, final Rectangle rectangle, final Vector2 velocity, final StaticEnvironment staticEnvironment, FloorCollisionData floorCollisionData) {
        Rectangle destRectangle = new Rectangle();
        destRectangle.set(rectangle);
        Vector2 center = new Vector2();
        rectangle.getCenter(center);
        center.add(0f, velocity.y*dt);
        destRectangle.setCenter(center);

        int minx = Math.max(0, (int)Math.floor(destRectangle.x/staticEnvironment.tileSizeInPixels));
        int miny = Math.max(0, (int)Math.floor(destRectangle.y/staticEnvironment.tileSizeInPixels));
        int maxx = Math.min(staticEnvironment.getNumTilesX(), (int)Math.ceil((destRectangle.x+destRectangle.width)/staticEnvironment.tileSizeInPixels));
        int maxy = Math.min(staticEnvironment.getNumTilesY(), (int)Math.ceil((destRectangle.y+destRectangle.height)/staticEnvironment.tileSizeInPixels));

        boolean didCollide = false;
        float moveY = 0.0f;
        float moveX = 0.0f;
        boolean breakLoop = false;
        for(int y = miny; y < maxy; y++) {
            if(breakLoop)
                break;
            for(int x = minx; x < maxx; x++) {
                if(staticEnvironment.getTileId(StaticEnvironment.LayerId.Floor, x, y) != 0) {
                    //We did collide with the tile.
                    //Narrow to just the upper part of the tile.
                    if(destRectangle.y <= ((y+1) * staticEnvironment.tileSizeInPixels) &&
                       rectangle.y >= ((y+1) * staticEnvironment.tileSizeInPixels))
                    {
                        didCollide = true;
                        moveY = ((y+1) * staticEnvironment.tileSizeInPixels+0.001f)-(rectangle.y);
                        breakLoop = true;
                        break;
                    }
                }
            }
        }

        //If the rect at the collision point is in a tile then we do not collide
        breakLoop = false;
        if(didCollide) {
            Rectangle dst = new Rectangle();
            dst.set(rectangle);
            dst.setY(dst.getY() + moveY);

            minx = Math.max(0, (int) Math.floor(dst.x / staticEnvironment.tileSizeInPixels));
            miny = Math.max(0, (int) Math.floor(dst.y / staticEnvironment.tileSizeInPixels));
            maxx = Math.min(staticEnvironment.getNumTilesX(), (int) Math.ceil((dst.x + dst.width) / staticEnvironment.tileSizeInPixels));
            maxy = Math.min(staticEnvironment.getNumTilesY(), (int) Math.ceil((dst.y + dst.height) / staticEnvironment.tileSizeInPixels));
            for (int y = miny; y < maxy; y++) {
                if (breakLoop)
                    break;
                for (int x = minx; x < maxx; x++) {
                    if(staticEnvironment.getTileId(StaticEnvironment.LayerId.Wall, x, y) != 0) {
                         didCollide = false;
                        breakLoop = true;
                        break;
                    }
                }
            }
        }
        floorCollisionData.set(didCollide, moveX, moveY);
    }

    public static WallCollisionData rectangleWalls(final float dt, final Rectangle rectangle, final Vector2 velocity, final StaticEnvironment staticEnvironment, WallCollisionData wallCollisionData) {
        Rectangle destRectangle = new Rectangle();
        destRectangle.set(rectangle);
        Vector2 center = new Vector2();
        rectangle.getCenter(center);
        //center.add(velocity.x*dt, velocity.y*dt);
        center.add(velocity.x*dt, 0f);
        destRectangle.setCenter(center);

        int minx = Math.max(0, (int)Math.floor(destRectangle.x/(float) staticEnvironment.tileSizeInPixels));
        int miny = Math.max(0, (int)Math.floor(destRectangle.y/(float)staticEnvironment.tileSizeInPixels));
        int maxx = Math.min(staticEnvironment.getNumTilesX(), (int)Math.ceil((destRectangle.x+destRectangle.width)/(float)staticEnvironment.tileSizeInPixels));
        int maxy = Math.min(staticEnvironment.getNumTilesY(), (int)Math.ceil((destRectangle.y+destRectangle.height)/(float)staticEnvironment.tileSizeInPixels));
        wallCollision(rectangle, staticEnvironment, StaticEnvironment.LayerId.Wall, true, true, minx, miny, maxx, maxy, wallCollisionData);
        if(!wallCollisionData.didCollide)
            wallCollision(rectangle, staticEnvironment, StaticEnvironment.LayerId.LeftWall, true, false, minx, miny, maxx, maxy, wallCollisionData);
        if(!wallCollisionData.didCollide)
            wallCollision(rectangle, staticEnvironment, StaticEnvironment.LayerId.RightWall, false, true, minx, miny, maxx, maxy, wallCollisionData);

        return wallCollisionData;
    }

    private static void wallCollision(Rectangle rectangle, StaticEnvironment staticEnvironment, StaticEnvironment.LayerId tileId, boolean left, boolean right, int minx, int miny, int maxx, int maxy, WallCollisionData wallCollisionData) {
        boolean didCollide = false;
        wallCollisionData.didCollide = false;
        for(int y = miny; y < maxy; y++) {
            if(didCollide)
                break;
            for(int x = minx; x < maxx; x++) {
                if(staticEnvironment.getTileId(tileId, x, y) != 0) {
                    //We did collide with the tile.
                    //Narrow down the test to only collide if source is on the left or on the right side of the tile.
                    if (right && rectangle.x <= x * staticEnvironment.tileSizeInPixels && (rectangle.x + rectangle.width) <= x * staticEnvironment.tileSizeInPixels) {
                        //src is on the left side
                        didCollide = true;
                        //wallCollisionData.set(didCollide, new Vector2((rectangle.x + rectangle.width) - x * staticEnvironment.tileSizeInPixels, 0f));
                        wallCollisionData.set(didCollide, new Vector2(0f, 0f));
                        break;
                    }
                    else if (left && rectangle.x >= (staticEnvironment.tileSizeInPixels + x * staticEnvironment.tileSizeInPixels) && (rectangle.x + rectangle.width) >= (staticEnvironment.tileSizeInPixels + x * staticEnvironment.tileSizeInPixels)) {
                        //src is on the right side
                        didCollide = true;
                        //wallCollisionData.set(didCollide, new Vector2(rectangle.x - (staticEnvironment.tileSizeInPixels + x * staticEnvironment.tileSizeInPixels), 0f));
                        wallCollisionData.set(didCollide, new Vector2(0f, 0f));
                        break;
                    }
                    else {

                    }
                }
            }
        }
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
