package com.sa.game.components;

import com.badlogic.ashley.core.ComponentMapper;

public class ComponentMappers {
    public static ComponentMapper<PositionComponent> position = ComponentMapper.getFor(PositionComponent.class);
    public static ComponentMapper<AIComponent> ai = ComponentMapper.getFor(AIComponent.class);
    public static ComponentMapper<ControlComponent> control = ComponentMapper.getFor(ControlComponent.class);
    public static ComponentMapper<CollisionComponent> collision = ComponentMapper.getFor(CollisionComponent.class);

}
