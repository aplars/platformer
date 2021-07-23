package com.sa.game.components;

import com.badlogic.ashley.core.ComponentMapper;
import com.sa.game.components.groups.EnemyGroupComponent;
import com.sa.game.components.groups.KeyGroupComponent;
import com.sa.game.components.groups.BoxingGloveGroupComponent;
import com.sa.game.components.groups.CoinGroupComponent;
import com.sa.game.components.groups.DoorGroupComponent;

public class ComponentMappers {
    public static ComponentMapper<Player1Component> player1 = ComponentMapper.getFor(Player1Component.class);
    public static ComponentMapper<PositionComponent> position = ComponentMapper.getFor(PositionComponent.class);
    public static ComponentMapper<DelayControlComponent> delayAi = ComponentMapper.getFor(DelayControlComponent.class);
    public static ComponentMapper<AIComponent> ai = ComponentMapper.getFor(AIComponent.class);
    public static ComponentMapper<AnimationComponent> animation = ComponentMapper.getFor(AnimationComponent.class);
    public static ComponentMapper<ControlComponent> control = ComponentMapper.getFor(ControlComponent.class);
    public static ComponentMapper<CollisionComponent> collision = ComponentMapper.getFor(CollisionComponent.class);
    public static ComponentMapper<PhysicsComponent> physics = ComponentMapper.getFor(PhysicsComponent.class);
    public static ComponentMapper<DamageComponent> damage = ComponentMapper.getFor(DamageComponent.class);
    public static ComponentMapper<HealthComponent> health = ComponentMapper.getFor(HealthComponent.class);
    public static ComponentMapper<MoveToEntityComponent> moveToEntity = ComponentMapper.getFor(MoveToEntityComponent.class);
    public static ComponentMapper<PickUpEntityComponent> pickUp = ComponentMapper.getFor(PickUpEntityComponent.class);
    public static ComponentMapper<RenderDebugInfoComponent> renderDebugInfo = ComponentMapper.getFor(RenderDebugInfoComponent.class);
    public static ComponentMapper<RenderComponent> render = ComponentMapper.getFor(RenderComponent.class);
    public static ComponentMapper<RenderSpriteInWhiteColorComponent> renderSpriteInWhiteColor = ComponentMapper.getFor(RenderSpriteInWhiteColorComponent.class);
    public static ComponentMapper<SensorComponent> sensor = ComponentMapper.getFor(SensorComponent.class);
    public static ComponentMapper<WorldConstantsComponent> worldConstats = ComponentMapper.getFor(WorldConstantsComponent.class);
    public static ComponentMapper<RenderStarsComponent> renderStars = ComponentMapper.getFor(RenderStarsComponent.class);
    public static ComponentMapper<PunchComponent> punch = ComponentMapper.getFor(PunchComponent.class);
    public static ComponentMapper<ParticleEffectComponent> particleEffect = ComponentMapper.getFor(ParticleEffectComponent.class);
    public static ComponentMapper<ThrownComponent> thrown = ComponentMapper.getFor(ThrownComponent.class);
    public static ComponentMapper<ExplodeOnContactComponent> explodeOnContact = ComponentMapper.getFor(ExplodeOnContactComponent.class);
    public static ComponentMapper<CoinGroupComponent> coinGroup = ComponentMapper.getFor(CoinGroupComponent.class);
    public static ComponentMapper<EnemyGroupComponent> enemyGroup = ComponentMapper.getFor(EnemyGroupComponent.class);
    public static ComponentMapper<BoxingGloveGroupComponent> boxingGloveGroup = ComponentMapper.getFor(BoxingGloveGroupComponent.class);
    public static ComponentMapper<KeyGroupComponent> keyGroup = ComponentMapper.getFor(KeyGroupComponent.class);
    public static ComponentMapper<DoorGroupComponent> doorGroup = ComponentMapper.getFor(DoorGroupComponent.class);
}
