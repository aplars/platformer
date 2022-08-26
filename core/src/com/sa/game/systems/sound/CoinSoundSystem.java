package com.sa.game.systems.sound;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.sa.game.components.ComponentMappers;
import com.sa.game.components.groups.CoinGroupComponent;

public class CoinSoundSystem extends IteratingSystem {
    Sound pickupSound;

    public  CoinSoundSystem(AssetManager assetManager) {
        super(Family.all(CoinGroupComponent.class).get());

        pickupSound = Gdx.audio.newSound(Gdx.files.internal("sounds/pickupCoin.mp3"));
    }

    @Override
    protected void processEntity(final Entity entity, final float deltaTime) {
        final CoinGroupComponent coinGroupComponent = ComponentMappers.coinGroup.get(entity);

        if(coinGroupComponent.thief != null && coinGroupComponent.timeSinceTaken <= 0.0) {
            pickupSound.play();
        }

    }
}
