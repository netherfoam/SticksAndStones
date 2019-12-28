package org.maxgamer.sticks.core;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import org.maxgamer.sticks.common.network.FrameFactory;
import org.maxgamer.sticks.common.network.frame.IdentityFrame;
import org.maxgamer.sticks.common.network.frame.TickFrame;
import org.maxgamer.sticks.core.controller.CreatureController;
import org.maxgamer.sticks.core.network.NetworkController;
import org.maxgamer.sticks.core.network.NetworkListener;
import org.maxgamer.sticks.core.prototype.CreaturePrototype;
import org.maxgamer.sticks.core.prototype.PrototypeFactory;
import org.maxgamer.sticks.core.tick.Clock;
import org.maxgamer.sticks.core.world.Momentum;
import org.maxgamer.sticks.core.world.Zone;
import org.maxgamer.sticks.core.world.entity.CreatureImpl;
import org.maxgamer.sticks.core.viewport.Viewport;
import org.maxgamer.sticks.core.world.entity.Tickable;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;

public class Game implements ApplicationListener {
    private static final boolean DEBUG = true;

    private static final int ASPECT_WIDTH = 16;
    private static final int ASPECT_HEIGHT = 9;

    private static final float VIEWPORT_MULTIPLIER = 1.25f;
    public static final float VIEWPORT_WIDTH = (int) (ASPECT_WIDTH * VIEWPORT_MULTIPLIER);
    public static final float VIEWPORT_HEIGHT = (int) (ASPECT_HEIGHT * VIEWPORT_MULTIPLIER);

    private static final int DEFAULT_RES_WIDTH = ASPECT_WIDTH * 100;
    private static final int DEFAULT_RES_HEIGHT = ASPECT_HEIGHT * 100;

    private Clock clock;
    private TiledMap map;
    private CreatureController input;
    private OrthographicCamera camera;
    private CreatureImpl player;
    private Viewport viewport;
    private long lastRender = System.nanoTime();
    private TiledMap collisionMap;
    private Zone zone;
    private NetworkController networkController;
    private NetworkListener networkListener;
    private PrototypeFactory prototypeFactory;
    private Map<Integer, CreatureImpl> creatures = new HashMap<>();
    private FrameFactory frameFactory;

    public Game() {

    }

    @Override
    public void create() {
        Gdx.graphics.setDisplayMode(DEFAULT_RES_WIDTH, DEFAULT_RES_HEIGHT, false);
        zone = new Zone(new TmxMapLoader());

        camera = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        camera.position.set(50, 50, 0);
        camera.update();

        prototypeFactory = new PrototypeFactory();
        clock = new Clock();

        Music music = Gdx.audio.newMusic(Gdx.files.internal("sound/music/Breeze.ogg"));
        music.setVolume(0.55f);
        music.setLooping(true);
        music.play();

        networkController = new NetworkController();
        try {
            networkController.connect("localhost", 2713);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        frameFactory = new FrameFactory();
        frameFactory.register(TickFrame.OPCODE, TickFrame::new);
        frameFactory.register(IdentityFrame.OPCODE, IdentityFrame::new);

        networkListener = new NetworkListener(this, (r) -> Gdx.app.postRunnable(r), frameFactory);
        networkListener.listen(networkController.input());

        clock.start();
        Gdx.graphics.setVSync(true);
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        long start = System.nanoTime();
        long delta = start - lastRender;
        float deltaSeconds = (float) (delta / 1000000000d);

        //float deltaSeconds = Gdx.graphics.getRawDeltaTime();

        if (input != null) {
            input.apply();
        }

        synchronized (clock) {
            // Synchronize here because we want to avoid rendering while the tick is still running
            // See Clocko#tick() method is synchronized on the clock too
            if (viewport != null) {
                viewport.render(deltaSeconds);
            }
        }

        lastRender = start;

        if (DEBUG) {
            BitmapFont font = new BitmapFont();
            SpriteBatch debug = new SpriteBatch();

            debug.begin();
            font.draw(debug, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10f, 40f);
            if (player != null) {
                font.draw(debug, "Player: " + player.getPosition().toString(), 10f, 20f);
            }
            debug.end();
        }
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        try {
            clock.stop();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void handle(TickFrame tick) {
        for (TickFrame.AddedEntity added : tick.getAdded()) {
            CreaturePrototype proto = prototypeFactory.get(CreaturePrototype.class, added.proto);

            CreatureImpl creature = new CreatureImpl(proto);
            creature.teleport(new Position(added.x, added.y));
            viewport.getCreatures().add(creature);
            clock.subscribe(creature);

            creatures.put(added.id, creature);
        }

        // TODO: bug.. somewhere.. where movements are dropped?
        for (TickFrame.MovedEntity moved : tick.getMoved()) {
            CreatureImpl creature = creatures.get(moved.id);
            if (creature == null) {
                System.out.println("Missing: " + moved.id);
                continue;
            }

            creature.move(new Momentum(moved.dx, moved.dy));
        }

        for (TickFrame.RemovedEntity removed : tick.getRemoved()) {
            CreatureImpl creature = creatures.get(removed.id);
            if (creature == null) {
                System.out.println("Missing: " + removed.id);
                continue;
            }

            creatures.remove(removed.id, creature);
            viewport.getCreatures().remove(creature);
            clock.unsubscribe(creature);
        }
    }

    public void handle(IdentityFrame identity) {
        CreaturePrototype playerProto = prototypeFactory.get(CreaturePrototype.class, 1);
        player = new CreatureImpl(playerProto);
        player.teleport(new Position(50, 50));

        viewport = new Viewport(player, 12, 12, camera);
        viewport.setZone(zone);

        input = new CreatureController(networkController, player, zone);
        Gdx.input.setInputProcessor(input);

        clock.subscribe(player);
        viewport.getCreatures().add(player);

        creatures.put(identity.getIdentity(), player);
    }
}
