package com.sa.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Vector2;

public class ParticleEffectComponent implements Component {
    public ParticleEffect particleEffect;
    public Vector2 position = new Vector2();

    public ParticleEffectComponent(String effectPath) {
        particleEffect = new ParticleEffect();
        particleEffect.load(Gdx.files.internal("particlesystems.party"),Gdx.files.internal(""));
        //particleEffect.getEmitters().first().setPosition(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
        particleEffect.getEmitters().first().setContinuous(false);

        particleEffect.start();

    }
}

