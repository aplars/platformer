package com.sa.game.components;

import com.badlogic.ashley.core.ComponentMapper;

public class ComponentMappers {
    public static ComponentMapper<Player1Component> player1 = ComponentMapper.getFor(Player1Component.class);
    public static ComponentMapper<PositionComponent> position = ComponentMapper.getFor(PositionComponent.class);
    public static ComponentMapper<AIComponent> ai = ComponentMapper.getFor(AIComponent.class);
    public static ComponentMapper<ControlComponent> control = ComponentMapper.getFor(ControlComponent.class);
    public static ComponentMapper<CollisionComponent> collision = ComponentMapper.getFor(CollisionComponent.class);
    public static ComponentMapper<PhysicsComponent> physics = ComponentMapper.getFor(PhysicsComponent.class);
    public static ComponentMapper<DamageComponent> damage = ComponentMapper.getFor(DamageComponent.class);
    public static ComponentMapper<HealthComponent> health = ComponentMapper.getFor(HealthComponent.class);
    public static ComponentMapper<MoveToEntityComponent> moveToEntity = ComponentMapper.getFor(MoveToEntityComponent.class);
    public static ComponentMapper<PickUpEntityComponent> pickUp = ComponentMapper.getFor(PickUpEntityComponent.class);
    public static ComponentMapper<RenderDebugInfoComponent> renderDebugInfo = ComponentMapper.getFor(RenderDebugInfoComponent.class);
    public static ComponentMapper<RenderComponent> render = ComponentMapper.getFor(RenderComponent.class);
    public static ComponentMapper<SensorComponent> sensor = ComponentMapper.getFor(SensorComponent.class);
    public static ComponentMapper<WorldConstantsComponent> worldConstats = ComponentMapper
            .getFor(WorldConstantsComponent.class);
}
