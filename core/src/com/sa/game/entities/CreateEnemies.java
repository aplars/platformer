package com.sa.game.entities;

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
    public static Enemy clown(Vector2 center, float height, FileHandle clownAtlasFileHandle, StaticEnvironment staticEnvironment, CollisionDetection collisionDetection) {
        final TextureAtlas atlas = new TextureAtlas(clownAtlasFileHandle);
        Animation<TextureRegion> idleAnimation = new Animation<TextureRegion>(1 / 60f * 6f, atlas.findRegions("idle"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> walkAnimation = new Animation<TextureRegion>(1 / 60f * 6f, atlas.findRegions("walk"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> stunnedAnimation = new Animation<TextureRegion>(1 / 60f * 6f, atlas.findRegions("stunned"), Animation.PlayMode.NORMAL);

        Enemy enemy = new Enemy(
                "clown",
                center,
                height,
                new EnemyAnimations(idleAnimation, walkAnimation, stunnedAnimation),
                ClownEnemyBrain.RESTING,
                staticEnvironment,
                collisionDetection
        );
        return enemy;
    }

}
