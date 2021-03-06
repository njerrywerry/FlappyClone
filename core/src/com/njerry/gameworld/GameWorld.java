package com.njerry.gameworld;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.njerry.gameobjects.Bird;
import com.njerry.gameobjects.ScrollHandler;
import com.njerry.zbHelpers.AssetLoader;

/**
 * Created by njerry on 7/27/15.
 */
public class GameWorld {
    private Bird bird;
    private ScrollHandler scroller;

    private Rectangle ground;

    private int score = 0;

    private GameState currentState;

    public int midPointY;

    private float runTime = 0;

    public enum GameState {
        MENU, READY, RUNNING, GAMEOVER, HIGHSCORE
    }

    public GameWorld (int midPointY) {
        bird = new Bird(33, midPointY -5, 17, 12);

        scroller = new ScrollHandler(this, midPointY + 66);

        ground = new Rectangle(0, midPointY + 66, 136, 11);

        currentState = GameState.MENU;
        this.midPointY = midPointY;
    }

    public void updateRunning (float delta) {
        if (delta > .15f) {
            delta = .15f;
        }
        bird.update(delta);
        scroller.update(delta);

        // Clean up on game over
        if (scroller.collides(bird) && bird.isAlive) {
            scroller.stop();
            bird.die();
            AssetLoader.dead.play();
        }

        if (Intersector.overlaps(bird.getBoundingCircle(), ground)) {
            scroller.stop();
            bird.die();
            bird.decelerate();
            currentState = GameState.GAMEOVER;

            if (score > AssetLoader.getHighScore()) {
                AssetLoader.setHighScore(score);
                currentState = GameState.HIGHSCORE;
            }
        }
    }

    public void update(float delta) {
        runTime += delta;

        switch (currentState) {
            case READY:
                updateReady(delta);
                break;
            case RUNNING:
                updateRunning(delta);
                break;
            default:
                break;
        }
    }

    private void updateReady(float delta) {
        bird.updateReady(runTime);
        scroller.updateReady(delta);
    }

    public void restart() {
        currentState = GameState.READY;
        score = 0;
        bird.onRestart(midPointY - 5);
        scroller.onRestart();
        currentState = GameState.READY;
    }

    public Bird getBird() {
        return bird;
    }

    public ScrollHandler getScroller() {
        return scroller;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int increment) {
        score += increment;
    }

    public boolean isReady() {
        return currentState == GameState.READY;
    }

    public void start() {
        currentState = GameState.RUNNING;
    }

    public boolean isGameOver() {
        return currentState == GameState.GAMEOVER;
    }

    public boolean isHighScore () {
        return currentState == GameState.HIGHSCORE;
    }

    public int getMidPointY() {
        return midPointY;
    }

    public void ready() {
        currentState = GameState.READY;
    }

    public boolean isMenu() {
        return currentState == GameState.MENU;
    }

    public boolean isRunning() {
        return currentState == GameState.RUNNING;
    }
}


