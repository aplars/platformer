package com.sa.game.components;

import com.badlogic.ashley.core.Component;

public class StateComponent<T>  implements Component
{
    public T state;

    public Class<StateComponent<T>> getMyClass(){
        return (Class<StateComponent<T>>)(Class<?>)StateComponent.class;
    }
}
