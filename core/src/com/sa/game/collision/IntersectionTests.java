package com.sa.game.collision;


import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sa.game.StaticEnvironment;

public class IntersectionTests {
        public static void rectangleRoof(final float dt, final Rectangle rectangle, final Vector2 velocity, final StaticEnvironment staticEnvironment, RoofCollisionData roofCollisionData) {
        Rectangle destRectangle = new Rectangle();
        destRectangle.set(rectangle);
        Vector2 center = new Vector2();
        rectangle.getCenter(center);
        center.add(0f, velocity.y*dt);
        destRectangle.setCenter(center);

        int minx = (int)(destRectangle.x/staticEnvironment.tileSizeInPixels);
        int miny = (int)(destRectangle.y/staticEnvironment.tileSizeInPixels);
        int maxx = (int)((destRectangle.x+destRectangle.width)/staticEnvironment.tileSizeInPixels);
        int maxy = (int)((destRectangle.y+destRectangle.height)/staticEnvironment.tileSizeInPixels);

        int srcMaxY = (int)((rectangle.y+rectangle.height)/staticEnvironment.tileSizeInPixels);

        boolean didCollide = false;
        float moveY = 0.0f;
        float moveX = 0.0f;
        boolean breakLoop = false;
        for(int y = miny; y <= maxy; y++) {
            if(breakLoop)
                break;
            for(int x = minx; x <= maxx; x++) {
                if(staticEnvironment.getTileId(StaticEnvironment.LayerId.Roof, x, y) != 0) {
                    if(maxy != srcMaxY && srcMaxY < y){
                        didCollide = true;
                        moveY = ((y-1) * staticEnvironment.tileSizeInPixels+0.001f)-(rectangle.y);
                        breakLoop = true;
                        break;
                    }
                }
            }
        }

        roofCollisionData.set(didCollide, moveX, moveY);
    }


    public static void rectangleGround(final float dt, final Rectangle rectangle, final Vector2 velocity, final StaticEnvironment staticEnvironment, FloorCollisionData floorCollisionData) {
        Rectangle destRectangle = new Rectangle();
        destRectangle.set(rectangle);
        Vector2 center = new Vector2();
        rectangle.getCenter(center);
        center.add(0f, velocity.y*dt);
        destRectangle.setCenter(center);

        int minx = (int)(destRectangle.x/staticEnvironment.tileSizeInPixels);
        int miny = (int)(destRectangle.y/staticEnvironment.tileSizeInPixels);
        int maxx = (int)((destRectangle.x+destRectangle.width)/staticEnvironment.tileSizeInPixels);
        int maxy = (int)((destRectangle.y+destRectangle.height)/staticEnvironment.tileSizeInPixels);

        int srcMinY = (int)(rectangle.y/staticEnvironment.tileSizeInPixels);

        boolean didCollide = false;
        float moveY = 0.0f;
        float moveX = 0.0f;
        boolean breakLoop = false;
        for(int y = miny; y <= maxy; y++) {
            if(breakLoop)
                break;
            for(int x = minx; x <= maxx; x++) {
                if(staticEnvironment.getTileId(StaticEnvironment.LayerId.Floor, x, y) != 0) {
                    if(miny != srcMinY && srcMinY > y){
                        didCollide = true;
                        moveY = ((y+1) * staticEnvironment.tileSizeInPixels+0.001f)-(rectangle.y);
                        breakLoop = true;
                        break;
                    }
                }
            }
        }

        floorCollisionData.set(didCollide, moveX, moveY);
    }

    public static WallCollisionData rectangleWalls(final float dt, final Rectangle rectangle, final Vector2 velocity, final StaticEnvironment staticEnvironment, WallCollisionData wallCollisionData) {
        wallCollision(dt, rectangle, velocity, staticEnvironment, StaticEnvironment.LayerId.Wall, true, true, wallCollisionData);
        if(!wallCollisionData.didCollide)
            wallCollision(dt, rectangle, velocity, staticEnvironment, StaticEnvironment.LayerId.LeftWall, true, false, wallCollisionData);
        if(!wallCollisionData.didCollide)
            wallCollision(dt, rectangle, velocity, staticEnvironment, StaticEnvironment.LayerId.RightWall, false, true, wallCollisionData);

        return wallCollisionData;
    }

    private static void wallCollision(final float dt, Rectangle rectangle, Vector2 velocity, StaticEnvironment staticEnvironment, StaticEnvironment.LayerId tileId, boolean left, boolean right, WallCollisionData wallCollisionData) {
        Rectangle destRectangle = new Rectangle();
        destRectangle.set(rectangle);
        Vector2 center = new Vector2();
        center = rectangle.getCenter(center);
        center.add(velocity.x*dt, velocity.y*dt);
        destRectangle.setCenter(center);

        int dstMinx = (int)(destRectangle.x/(float) staticEnvironment.tileSizeInPixels);
        int dstMiny = (int)(destRectangle.y/(float)staticEnvironment.tileSizeInPixels);
        int dstMaxx = (int)((destRectangle.x+destRectangle.width)/(float)staticEnvironment.tileSizeInPixels);
        int dstMaxy = (int)((destRectangle.y+destRectangle.height)/(float)staticEnvironment.tileSizeInPixels);

        int srcMinx = (int)(rectangle.x/(float) staticEnvironment.tileSizeInPixels);
        int srcMiny = (int)(rectangle.y/(float)staticEnvironment.tileSizeInPixels);
        int srcMaxx = (int)((rectangle.x+rectangle.width)/(float)staticEnvironment.tileSizeInPixels);
        int srcMaxy = (int)((rectangle.y+rectangle.height)/(float)staticEnvironment.tileSizeInPixels);

        int fromX;
        int toX;

        if(velocity.x < 0) {
            fromX = dstMinx;
            toX = srcMinx;
        }
        else {
            fromX = srcMaxx;
            toX = dstMaxx;
        }
        boolean didCollide = false;
        wallCollisionData.didCollide = false;
        for(int y = Math.min(srcMiny, dstMiny); y <= Math.max(srcMaxy, dstMaxy); y++) {
            for(int x = fromX; x <= toX; x++) {
                if(staticEnvironment.getTileId(tileId, x, y) != 0) {
                    //Only collide if src is outside
                    if((velocity.x > 0 && srcMaxx < x) || (velocity.x < 0 && srcMinx > x)) {
                        //We did collide with the tile.
                        if (tileId == StaticEnvironment.LayerId.Wall) {
                            wallCollisionData.set(true, new Vector2(0f, 0f));
                            return;
                        }
                        if (tileId == StaticEnvironment.LayerId.RightWall && velocity.x > 0f && srcMaxx < x) {
                            wallCollisionData.set(true, new Vector2(0f, 0f));
                            return;
                        }
                        if (tileId == StaticEnvironment.LayerId.LeftWall && velocity.x < 0f && srcMinx > x) {
                            wallCollisionData.set(true, new Vector2(0f, 0f));
                            return;
                        }
                    }
                    // return;
                }
            }
        }
    }

    public static RectangleCollisionData rectangleRectangle(Rectangle recta, Vector2 va, Rectangle rectb) {
        RectangleCollisionData rectangleCollisionData = new RectangleCollisionData();

        //Extend the static rectangle with the extends of the moving one.
        Rectangle rect = new Rectangle();
        rect.set(recta);
        rect.width += rectb.width;
        rect.height += rectb.height;
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
