package letscode.gdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ScreenUtils;
import letscode.gdx.emitter.Emitter;
import letscode.gdx.emitter.Particle;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Starter extends ApplicationAdapter {
	SpriteBatch batch;
	private final KeyBoardAdapter inputProcessor;
	private String meID;
	private ObjectMap<String, Panzer> panzers = new ObjectMap<>();
//	private final List<Panzer> enemies = new ArrayList<>();
	private Integer count = 1;
	private MessageSender messageSender;

	private Texture bulletTexture;

	public Starter(InputState inputState) {
		this.inputProcessor = new KeyBoardAdapter(inputState);
	}

	@Override
	public void create () {
		Gdx.input.setInputProcessor(inputProcessor);
		batch = new SpriteBatch();
		Panzer player = new Panzer("player.png", 200, 300);
		panzers.put(meID, player);
		bulletTexture = new Texture("bullet.png");
//
//		enemies.addAll(IntStream.range(0, 15).mapToObj(num -> {
//			int x = MathUtils.random(Gdx.graphics.getWidth());
//			int y = MathUtils.random(Gdx.graphics.getHeight());
//			return new Panzer("enemy.png", x, y);
//		}).collect(Collectors.toList())
//		);
	}

	@Override
	public void render () {

		ScreenUtils.clear(1, 1, 1, 1);
		batch.begin();

		for (String key: panzers.keys()) {
			Panzer panzer = panzers.get(key);
			InputState inputState = inputProcessor.getInputState();

			Emitter emitter = panzer.emitter;
			emitter.setAngle(inputState.getAngle() + 90);
			emitter.getPosition().set(panzer.getOrigin());
			float deltaTime = Gdx.graphics.getDeltaTime();

			if(inputState.isFirePressed()){
				emitter.start(deltaTime);
			}

			emitter.act(deltaTime);
			for (Particle particle: emitter.getParticles()){
				Vector2 position = particle.getPosition();
				batch.draw(bulletTexture, position.x - 8, position.y - 8);
			}

			panzer.render(batch);
		}
//		enemies.forEach(enemy -> {
//			enemy.render(batch);
//			enemy.rotateTo(player.getPosition());
//		});
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		for (Panzer value: panzers.values()) {
			value.dispose();
		}
	}

	public void setMessageSender(MessageSender messageSender) {
		this.messageSender = messageSender;
	}

	public void handleTimer() {
		if(inputProcessor != null && !panzers.isEmpty()){
			Panzer player = panzers.get(meID);
			InputState playerState = inputProcessor.updateAndGetInputState(player.getOrigin());
			messageSender.sendMessage(playerState);
		}
	}

    public void setMeId(String meId) {
		this.meID = meId;
    }

	public void evict(String idToEvict) {
		panzers.remove(idToEvict);
	}

	public void updatePanzer(String id, float x, float y, float angle) {
		if(panzers.isEmpty()) return;
		Panzer panzer = panzers.get(id);
		if (panzer == null) {
			panzer = new Panzer("enemy.png", x, y);
			panzers.put(id, panzer);
		} else {
			panzer.moveTo(x, y);
			panzer.rotateTo(angle);
		}
	}
}
