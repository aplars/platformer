package com.sa.game.statemachines;

import com.sa.game.StaticEnvironment;

public interface GameStateCommon{
    void setDt(float dt);

    void setGroundCollision(boolean groundCollision);

    void setWallCollision(boolean wallCollision);

    void setStaticEnvironment(StaticEnvironment staticEnvironment);
}
