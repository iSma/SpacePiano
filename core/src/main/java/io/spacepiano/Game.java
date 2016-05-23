package io.spacepiano;

public class Game extends com.badlogic.gdx.Game {
    @Override
    public void create() {
        setScreen(new FirstScreen());
    }
}
