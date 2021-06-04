package com.sa.game;

public interface ILoadNextLevel {
    void nextLevel(int player1Score, int player1Lives);

    void toGameOverScreen(int player1Score);
}
