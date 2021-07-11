package com.sa.game.screens;

import com.sa.game.systems.control.KeyboardMapping;

interface IWindowCloseEvent {
    void onWindowClose(KeyboardMapping keyboardMapping);
}
