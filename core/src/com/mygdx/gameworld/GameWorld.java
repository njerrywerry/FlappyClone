package com.mygdx.gameworld;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.gameobjects.Bird;
import com.mygdx.gameobjects.ScrollHandler;
import com.mygdx.zbHelpers.AssetLoader;

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

    public enum GameState {
        READY, RUNNING, GAMEOVER, HIGHSCORE
    }

    public GameWorld (int midPointY) {
        bird = new Bird(33, midPointY -5, 17, 12);

        scroller = new ScrollHandler(this, midPointY + 66);

        ground = new Rectangle(0, midPointY + 66, 136, 11);

        currentState = GameState.READY;
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
        //do nothing for now
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

}


