package letscode.gdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Starter extends ApplicationAdapter {
	SpriteBatch batch;
	private final KeyBoardAdapter inputProcessor;
	private Panzer player;
	private Integer count = 1;
	private final List<Panzer> enemies = new ArrayList<>();
	private MessageSender messageSender;

	public Starter(InputState inputState) {
		this.inputProcessor = new KeyBoardAdapter(inputState);
	}

	@Override
	public void create () {
		Gdx.input.setInputProcessor(inputProcessor);
		batch = new SpriteBatch();
		player = new Panzer("player.png", 200, 300);

		enemies.addAll(IntStream.range(0, 15).mapToObj(num -> {
			int x = MathUtils.random(Gdx.graphics.getWidth());
			int y = MathUtils.random(Gdx.graphics.getHeight());
			return new Panzer("enemy.png", x, y);
		}).collect(Collectors.toList())
		);
	}

	@Override
	public void render () {
		player.moveTo(inputProcessor.getDirection());
		player.rotateTo(inputProcessor.getMousePoss());
		ScreenUtils.clear(1, 1, 1, 1);
		batch.begin();
		player.render(batch);


		enemies.forEach(enemy -> {
			enemy.render(batch);
			enemy.rotateTo(player.getPosition());
		});
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		player.dispose();
	}

	public void setMessageSender(MessageSender messageSender) {
		this.messageSender = messageSender;
	}

	public void handleTimer() {
		if(inputProcessor != null){
			InputState playerState = inputProcessor.updateAndGetInputState(player.getOrigin());
			messageSender.sendMessage(playerState);
		}
	}
}
