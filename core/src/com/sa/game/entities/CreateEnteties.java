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

public class CreateEnteties {
    public static Entity enemy(AssetManager assetManager, Vector2 center, float height, StaticEnvironment staticEnvironment, CollisionDetection collisionDetection) {
        assetManager.load("enteties/game.atlas", TextureAtlas.class);
        assetManager.finishLoadingAsset("enteties/game.atlas");
        TextureAtlas atlas = assetManager.get("enteties/game.atlas", TextureAtlas.class);
        return Enemy.create(
                "clown",
                center,
                height,
                new Animation<TextureRegion>(1 / 60f * 6f, atlas.findRegions("devodevilstanding"), Animation.PlayMode.LOOP),
                new Animation<TextureRegion>(1 / 60f * 6f, atlas.findRegions("devodevilrunning"), Animation.PlayMode.LOOP),
                new Animation<TextureRegion>(1 / 60f * 6f, atlas.findRegions("devodevilstanding"), Animation.PlayMode.NORMAL),
                new Animation<TextureRegion>(1 / 60f * 12f, atlas.findRegions("stars"), Animation.PlayMode.LOOP),
                staticEnvironment,
                collisionDetection);
    }

    public static Entity player(AssetManager assetManager, Vector2 pos, Vector2 siz, StaticEnvironment staticEnvironment, CollisionDetection collisionDetection) {
        assetManager.load("enteties/game.atlas", TextureAtlas.class);
        assetManager.finishLoadingAsset("enteties/game.atlas");
        TextureAtlas atlas = assetManager.get("enteties/game.atlas", TextureAtlas.class);
        return Player.create(
                          pos,
                          new Vector2(),
                          siz,
                          new Animation<TextureRegion>(1 / 60f * 12f, atlas.findRegions("mrmochiidle"), PlayMode.LOOP),
                          new Animation<TextureRegion>(1 / 60f * 6f, atlas.findRegions("mrmochirunning"), PlayMode.LOOP),
                          new Animation<TextureRegion>(1 / 60f * 6f, atlas.findRegions("mrmochijumping"), PlayMode.LOOP),
                          new Animation<TextureRegion>(1 / 60f * 6f, atlas.findRegions("mrmochihurt"), PlayMode.LOOP),
                          staticEnvironment,
                          collisionDetection);
    }

    public static Entity boxingGlove(AssetManager assetManager, Vector2 pos, Vector2 vel, float size, Entity parent, StaticEnvironment staticEnvironment, CollisionDetection collisionDetection) {
        assetManager.load("enteties/game.atlas", TextureAtlas.class);
        assetManager.finishLoadingAsset("enteties/game.atlas");
        TextureAtlas atlas = assetManager.get("enteties/game.atlas", TextureAtlas.class);
        return BoxingGlove.create("glove",
                                  pos,
                                  vel,
                                  size,
                                  new Animation<TextureRegion>(1 / 60f * 6f, atlas.findRegions("boxingglove"), PlayMode.LOOP),
                                  parent,
                                  staticEnvironment,
                                  collisionDetection);
    }

    public static Entity key(AssetManager assetManager, Vector2 center, float height, StaticEnvironment staticEnvironment, CollisionDetection collisionDetection) {
        assetManager.load("enteties/game.atlas", TextureAtlas.class);
        assetManager.finishLoadingAsset("enteties/game.atlas");
        TextureAtlas atlas = assetManager.get("enteties/game.atlas", TextureAtlas.class);
        return Key.create(
                          "key",
                          center,
                          height,
                          new Animation<TextureRegion>(1 / 60f * 6f, atlas.findRegions("key"), Animation.PlayMode.NORMAL),
                          staticEnvironment,
                          collisionDetection);
    }
    public static Entity apple(AssetManager assetManager, Vector2 center, float height, StaticEnvironment staticEnvironment, CollisionDetection collisionDetection) {
        assetManager.load("enteties/game.atlas", TextureAtlas.class);
        assetManager.finishLoadingAsset("enteties/game.atlas");
        TextureAtlas atlas = assetManager.get("enteties/game.atlas", TextureAtlas.class);
        return Apple.create(
                          center,
                          height,
                          new Animation<TextureRegion>(1 / 60f * 6f, atlas.findRegions("apple32x32"), Animation.PlayMode.NORMAL),
                          staticEnvironment,
                          collisionDetection);
    }

    public static Entity explosion(Vector2 position) {
        return Explosion.create(position);
    }
}
