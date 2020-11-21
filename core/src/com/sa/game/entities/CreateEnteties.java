package com.sa.game.entities;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Vector2;
import com.sa.game.StaticEnvironment;
import com.sa.game.collision.CollisionDetection;
import com.sa.game.gfx.EnemyAnimations;
import com.sa.game.gfx.PlayerAnimations;
import com.sa.game.gfx.PlayerStunProjectileAnimations;
import com.sa.game.gfx.PlayerWeaponAnimations;
import com.sa.game.statemachines.ClownEnemyBrain;

public class CreateEnteties {
    public static Enemy clown(AssetManager assetManager, Vector2 center, float height, StaticEnvironment staticEnvironment, CollisionDetection collisionDetection) {
        assetManager.load("enteties/clown/clown.atlas", TextureAtlas.class);
        assetManager.finishLoadingAsset("enteties/clown/clown.atlas");
        TextureAtlas atlas = assetManager.get("enteties/clown/clown.atlas", TextureAtlas.class);


        Enemy enemy = new Enemy(
                "clown",
                center,
                height,
                new EnemyAnimations(
                        new Animation<TextureRegion>(1 / 60f * 6f, atlas.findRegions("idle"), Animation.PlayMode.LOOP),
                        new Animation<TextureRegion>(1 / 60f * 6f, atlas.findRegions("walk"), Animation.PlayMode.LOOP),
                        new Animation<TextureRegion>(1 / 60f * 6f, atlas.findRegions("stunned"), Animation.PlayMode.NORMAL)),
                staticEnvironment,
                collisionDetection
        );
        State<Enemy> test = ClownEnemyBrain.RESTING;
        enemy.stateMachine = new DefaultStateMachine<>(enemy, test);
        return enemy;
    }

    public static Player player(AssetManager assetManager, Vector2 pos, Vector2 siz, StaticEnvironment staticEnvironment, CollisionDetection collisionDetection, Engine preUpdateEngine, Engine updateEngine) {
        //TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("player.atlas"));
        assetManager.load("enteties/player/player.atlas", TextureAtlas.class);
        assetManager.finishLoadingAsset("enteties/player/player.atlas");
        TextureAtlas atlas = assetManager.get("enteties/player/player.atlas", TextureAtlas.class);
        return new Player(
                          pos,
                          new Vector2(),
                          siz,
                          new Animation<TextureRegion>(1 / 60f * 6f, atlas.findRegions("walk"), PlayMode.LOOP),
                          new Animation<TextureRegion>(1 / 60f * 6f, atlas.findRegions("walk"), PlayMode.LOOP),
                          staticEnvironment,
                          collisionDetection,
                          preUpdateEngine,
                          updateEngine);
    }

    public  static  PlayerStunProjectile playerStunProjectile(AssetManager assetManager, Vector2 position, Vector2 velocity, int tileSizeInPixels, CollisionDetection collisionDetection, Engine preUpdateEngine, Engine updateEngine) {
        assetManager.load("enteties/player_stun_projectile/player_stun_projectile.atlas", TextureAtlas.class);
        assetManager.finishLoadingAsset("enteties/player_stun_projectile/player_stun_projectile.atlas");
        TextureAtlas atlas = assetManager.get("enteties/player_stun_projectile/player_stun_projectile.atlas", TextureAtlas.class);

        return  new PlayerStunProjectile(
                                         position,
                                         velocity,
                                         new Animation<TextureRegion>(1 / 60f * 6f, atlas.findRegions("projectile"), PlayMode.LOOP),
                                         new Animation<TextureRegion>(1 / 60f * 6f, atlas.findRegions("projectile"), PlayMode.NORMAL),
                                         tileSizeInPixels,
                                         collisionDetection,
                                         preUpdateEngine,
                                         updateEngine);
    }

    public static PickedUpEntity playerWeapon(PlayerWeaponAnimations playerWeaponAnimations, Vector2 position, Vector2 velocity, float size, CollisionDetection collisionDetection) {

        return new PickedUpEntity(position, velocity, size, playerWeaponAnimations, collisionDetection);
    }
}
