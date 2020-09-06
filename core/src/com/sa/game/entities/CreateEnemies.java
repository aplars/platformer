package com.sa.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.sa.game.StaticEnvironment;
import com.sa.game.collision.CollisionDetection;
import com.sa.game.gfx.EnemyAnimations;
import com.sa.game.statemachines.ClownEnemyBrain;

public class CreateEnemies {
    public static Enemy clown(AssetManager assetManager, Vector2 center, float height, StaticEnvironment staticEnvironment, CollisionDetection collisionDetection) {
        assetManager.load("clown.atlas", TextureAtlas.class);
        assetManager.finishLoadingAsset("clown.atlas");
        TextureAtlas atlas = assetManager.get("clown.atlas", TextureAtlas.class);
        Enemy enemy = new Enemy(
                "clown",
                center,
                height,
                new EnemyAnimations(
                        atlas,
                        new Animation<TextureRegion>(1 / 60f * 6f, atlas.findRegions("idle"), Animation.PlayMode.LOOP),
                        new Animation<TextureRegion>(1 / 60f * 6f, atlas.findRegions("walk"), Animation.PlayMode.LOOP),
                        new Animation<TextureRegion>(1 / 60f * 6f, atlas.findRegions("stunned"), Animation.PlayMode.NORMAL)),
                ClownEnemyBrain.RESTING,
                staticEnvironment,
                collisionDetection
        );
        return enemy;
    }

}
