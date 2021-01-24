package com.sa.game.entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.sa.game.StaticEnvironment;
import com.sa.game.collision.CollisionDetection;
import com.sa.game.gfx.PlayerWeaponAnimations;

public class CreateEnteties {
    public static Entity clown(AssetManager assetManager, Vector2 center, float height, StaticEnvironment staticEnvironment, CollisionDetection collisionDetection) {
        assetManager.load("enteties/clown/clown.atlas", TextureAtlas.class);
        assetManager.finishLoadingAsset("enteties/clown/clown.atlas");
        TextureAtlas atlas = assetManager.get("enteties/clown/clown.atlas", TextureAtlas.class);


        return Enemy.create(
                "clown",
                center,
                height,
                new Animation<TextureRegion>(1 / 60f * 6f, atlas.findRegions("idle"), Animation.PlayMode.LOOP),
                new Animation<TextureRegion>(1 / 60f * 6f, atlas.findRegions("walk"), Animation.PlayMode.LOOP),
                new Animation<TextureRegion>(1 / 60f * 6f, atlas.findRegions("stunned"), Animation.PlayMode.NORMAL),
                staticEnvironment,
                collisionDetection);
    }

    public static Entity player(AssetManager assetManager, Vector2 pos, Vector2 siz, StaticEnvironment staticEnvironment, CollisionDetection collisionDetection) {
        //TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("player.atlas"));
        assetManager.load("enteties/game.atlas", TextureAtlas.class);
        assetManager.finishLoadingAsset("enteties/game.atlas");
        TextureAtlas atlas = assetManager.get("enteties/game.atlas", TextureAtlas.class);
        return Player.create(
                          pos,
                          new Vector2(),
                          siz,
                          new Animation<TextureRegion>(1 / 60f * 6f, atlas.findRegions("mrmochiidle"), PlayMode.LOOP),
                          new Animation<TextureRegion>(1 / 60f * 6f, atlas.findRegions("mrmochirunning"), PlayMode.LOOP),
                          new Animation<TextureRegion>(1 / 60f * 6f, atlas.findRegions("mrmochijumping"), PlayMode.LOOP),
                          new Animation<TextureRegion>(1 / 60f * 6f, atlas.findRegions("mrmochihurt"), PlayMode.LOOP),
                          staticEnvironment,
                          collisionDetection);
    }

    public static Entity playerStunProjectile(AssetManager assetManager, Vector2 position, Vector2 velocity, int tileSizeInPixels, CollisionDetection collisionDetection) {
        assetManager.load("enteties/player_stun_projectile/player_stun_projectile.atlas", TextureAtlas.class);
        assetManager.finishLoadingAsset("enteties/player_stun_projectile/player_stun_projectile.atlas");
        TextureAtlas atlas = assetManager.get("enteties/player_stun_projectile/player_stun_projectile.atlas", TextureAtlas.class);

        return  PlayerStunProjectile.create(
                                         position,
                                         velocity,
                                         new Animation<TextureRegion>(1 / 60f * 6f, atlas.findRegions("projectile"), PlayMode.LOOP),
                                         new Animation<TextureRegion>(1 / 60f * 6f, atlas.findRegions("projectile"), PlayMode.NORMAL),
                                         tileSizeInPixels,
                                         collisionDetection);
    }

    public static PickedUpEntity playerWeapon(PlayerWeaponAnimations playerWeaponAnimations, Vector2 position, Vector2 velocity, float size, CollisionDetection collisionDetection) {

        return new PickedUpEntity(position, velocity, size, playerWeaponAnimations, collisionDetection);
    }
}
