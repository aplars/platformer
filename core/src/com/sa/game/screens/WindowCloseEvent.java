package com.sa.game.screens;

import com.sa.game.systems.control.ControllerMapping;
import com.sa.game.systems.control.KeyboardMapping;

interface IWindowCloseEvent {
    void onWindowClose(KeyboardMapping keyboardMapping);
    void onWindowClose(ControllerMapping keyboardMapping);
    void onWindowCLose();
}
