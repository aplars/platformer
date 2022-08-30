package com.sa.game.systems.sound;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.sa.game.components.CollisionComponent;
import com.sa.game.components.ComponentMappers;
import com.sa.game.components.ControlComponent;
import com.sa.game.components.PhysicsComponent;
import com.sa.game.components.PickUpEntityComponent;
import com.sa.game.components.Player1Component;
import com.sa.game.components.PunchComponent;
import com.sa.game.models.SoundSettingsModel;

public class PlayerSoundSystem extends IteratingSystem {
    private final ComponentMapper<ControlComponent> controlMap = ComponentMapper.getFor(ControlComponent.class);
    private final ComponentMapper<CollisionComponent> collisionMap = ComponentMapper.getFor(CollisionComponent.class);

    Sound jumpSound;
    Sound fireSound;
    SoundSettingsModel soundSettingsModel;

    public  PlayerSoundSystem(AssetManager assetManager, SoundSettingsModel soundSettingsModel) {
        super(Family.all(Player1Component.class,
                         ControlComponent.class,
                         PhysicsComponent.class,
                         PickUpEntityComponent.class,
                         PunchComponent.class).get());

        //assetManager.load("sounds/jump.wav", Sound.class);
        //assetManager.finishLoadingAsset("sounds/jump.wav");
        this.soundSettingsModel = soundSettingsModel;
        jumpSound = Gdx.audio.newSound(Gdx.files.internal("sounds/jump.mp3"));
        fireSound = Gdx.audio.newSound(Gdx.files.internal("sounds/laserShoot.mp3"));
    }

    @Override
    protected void processEntity(final Entity entity, final float deltaTime) {
        final ControlComponent control = controlMap.get(entity);
        final CollisionComponent collision = collisionMap.get(entity);
        final PickUpEntityComponent pickUpEntityComponent = ComponentMappers.pickUp.get(entity);
        final PunchComponent punchComponent = ComponentMappers.punch.get(entity);

        if(control.buttonA && collision.entity.groundCollisionData.didCollide) {
            jumpSound.play(soundSettingsModel.soundVolume/100.0f);
        }
        if(punchComponent.didFire && control.buttonB && pickUpEntityComponent.entity == null) {
            fireSound.play(soundSettingsModel.soundVolume/100.0f);
        }

    }
}
