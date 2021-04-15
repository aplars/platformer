package com.sa.game.components;

import com.badlogic.ashley.core.Component;
import com.sa.game.entities.EntityType;

public class TypeComponent implements Component {
    public EntityType entityType = EntityType.None;
}
