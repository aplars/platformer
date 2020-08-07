package com.sa.game.entities;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.sa.game.StaticEnvironment;
import com.sa.game.collision.FloorCollisionData;
import com.sa.game.collision.IntersectionTests;
import com.sa.game.collision.WallCollisionData;

public class Enemies {
    ArrayList<Enemy> enemies = new ArrayList<>();

    public void add(Enemy enemy) {
        this.enemies.add(enemy);
    }

    public void remove(Enemy enemy) {
        enemies.remove(enemy);
    }

    public Iterator<Enemy> iterator() {
        return enemies.iterator();
    }

    public void preUpdate(float dt) {
        for(Enemy enemy : enemies) {
            enemy.preUpdate(dt);
        }
    }

    public void update(float dt, StaticEnvironment staticEnvironment) {
        for(Enemy enemy : enemies) {
            FloorCollisionData enemyGroundCollisionData = IntersectionTests.rectangleGround(enemy.collisionRectangle, enemy.velocity, staticEnvironment);
            WallCollisionData enemyWallsCollisionData = IntersectionTests.rectangleWalls(enemy.collisionRectangle, enemy.velocity, staticEnvironment);
            enemy.update(dt, enemyGroundCollisionData, enemyWallsCollisionData, staticEnvironment);
        }
    }

    public void render(float dt, OrthographicCamera camera) {
        for(Enemy enemy : enemies) {
            enemy.render(dt, camera);
        }
    }
}
