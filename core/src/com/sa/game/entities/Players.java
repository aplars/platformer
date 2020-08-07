package com.sa.game.entities;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.sa.game.StaticEnvironment;
import com.sa.game.collision.CollisionDetection;
import com.sa.game.collision.FloorCollisionData;
import com.sa.game.collision.IntersectionTests;
import com.sa.game.collision.WallCollisionData;

import java.util.ArrayList;
import java.util.Iterator;

public class Players implements Iterable<Player> {
    ArrayList<Player> players = new ArrayList<>();

    public Iterator<Player> iterator() { return players.iterator(); }

    public void add(Player player) {
        players.add(player);
    }

    public void remove(Player player, CollisionDetection collisionDetection) {
        players.remove(player);
        collisionDetection.remove(player.collisionEntity);
    }

    public void handleInput(float dt, Controller controller) {
        for (Player player : players) {
            player.handleInput(dt, controller);
        }
    }

    public void preUpdate(float dt) {
        for (Player player : players) {
            player.preUpdate(dt);
        }
    }

    public void update(float dt, StaticEnvironment staticEnvironment, CollisionDetection collisionDetection, PlayerProjectiles playerProjectiles, Enemies enemies) {
        for (Player player : players) {
            FloorCollisionData groundCollisionData = IntersectionTests.rectangleGround(player.collisionRectangle, player.velocity, staticEnvironment);
            WallCollisionData wallsCollisionData = IntersectionTests.rectangleWalls(player.collisionRectangle, player.velocity, staticEnvironment);

            player.update(dt, collisionDetection, groundCollisionData, wallsCollisionData, staticEnvironment, playerProjectiles, enemies);
        }
    }

    public void render(float dt, OrthographicCamera camera) {
        for (Player player : players) {
            player.render(dt, camera);
        }
    }
}
