package com.sa.game.entities;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.controllers.Controller;
import com.sa.game.StaticEnvironment;
import com.sa.game.collision.CollisionDetection;
import com.sa.game.gfx.Sprites;

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

    public void clear() {
        players.clear();
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

    public void update(float dt, AssetManager assetManager, StaticEnvironment staticEnvironment, CollisionDetection collisionDetection, PlayerStunProjectiles playerStunProjectiles, PlayerWeapons weapons, Enemies enemies) {
        //Move the player to the top if it falls under zero
        for (Player player : players) {
            if(player.position.y < 0) {
                player.warpToTop(staticEnvironment);
            }
        }

        for (Player player : players) {
            player.update(dt, assetManager, collisionDetection, staticEnvironment, playerStunProjectiles, weapons, enemies);
        }
    }

    public void render(float dt, Sprites sprites) {
        for (Player player : players) {
            player.render(dt, sprites);
        }
    }
}
