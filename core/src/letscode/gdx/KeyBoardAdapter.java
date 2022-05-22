package letscode.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;

public class KeyBoardAdapter extends InputAdapter {

    private final Vector2 direction = new Vector2();
    private final Vector2 mousePoss = new Vector2();
    private final Vector2 angle = new Vector2();

    private InputState inputState;

    public KeyBoardAdapter(InputState inputState) {
        this.inputState = inputState;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        inputState.setFirePressed(true);
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        inputState.setFirePressed(false);
        return super.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.W) inputState.setUpPressed(true);
        if(keycode == Input.Keys.S) inputState.setDownPressed(true);
        if(keycode == Input.Keys.A) inputState.setLeftPressed(true);
        if(keycode == Input.Keys.D) inputState.setRightPressed(true);

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.W) inputState.setUpPressed(false);
        if(keycode == Input.Keys.S) inputState.setDownPressed(false);
        if(keycode == Input.Keys.A) inputState.setLeftPressed(false);
        if(keycode == Input.Keys.D) inputState.setRightPressed(false);

        return false;
    }

    public void updateMousePos(){
        int x = Gdx.input.getX();
        int y = Gdx.graphics.getHeight() - Gdx.input.getY();
        mousePoss.set(x, y);
    }

    public Vector2 getDirection(){
        direction.set(0, 0);

        if(inputState.isUpPressed()) direction.add(0, 5);
        if(inputState.isDownPressed()) direction.add(0, -5);
        if(inputState.isLeftPressed()) direction.add(-5, 0);
        if(inputState.isRightPressed()) direction.add(5, 0);

        return direction;
    }

    public Vector2 getMousePoss() {
        updateMousePos();
        return mousePoss;
    }

    public InputState updateAndGetInputState(Vector2 playerOrigin){
        updateMousePos();
        angle.set(mousePoss).sub(playerOrigin);
        inputState.setAngle(angle.angleDeg() - 90);
        return inputState;
    }

    public InputState getInputState() {
        return inputState;
    }
}
