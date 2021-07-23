package com.sa.game.screens;

import com.sa.game.systems.control.ControllerMapping;
import com.sa.game.systems.control.KeyboardMapping;

interface IJoystickConfigurationWindowCloseEvent {

    void onWindowClose(ControllerMapping keyboardMapping);

    void onWindowCLose();
}

interface IKeyboardConfigurationWindowCloseEvent {

    void onWindowClose(KeyboardMapping keyboardMapping);

    void onWindowCLose();
}
