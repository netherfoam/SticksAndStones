package org.maxgamer.sticks.core;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import org.maxgamer.sticks.common.network.frame.IdentityFrame;
import org.maxgamer.sticks.common.network.frame.TickFrame;
import org.maxgamer.sticks.common.world.Direction;
import org.maxgamer.sticks.core.controller.CreatureController;
import org.maxgamer.sticks.core.network.NetworkController;
import org.maxgamer.sticks.core.network.NetworkListener;
import org.maxgamer.sticks.core.prototype.CreaturePrototype;
import org.maxgamer.sticks.core.prototype.PrototypeFactory;
import org.maxgamer.sticks.core.tick.Clock;
import org.maxgamer.sticks.core.viewport.Viewport;
import org.maxgamer.sticks.core.world.Zone;
import org.maxgamer.sticks.core.world.entity.CreatureImpl;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;

public class Game implements ApplicationListener {
    public static final boolean DEBUG = true;

    private Clock clock;
    private CreatureController input;
    private CreatureImpl player;
    private Viewport viewport;
    private Zone zone;
    private NetworkController networkController;
    private NetworkListener networkListener;
    private PrototypeFactory prototypeFactory;
    private Map<Integer, CreatureImpl> creatures = new HashMap<>();

    @Override
    public void create() {
        Gdx.graphics.setDisplayMode(Viewport.DEFAULT_RES_WIDTH, Viewport.DEFAULT_RES_HEIGHT, false);
        zone = new Zone(new TmxMapLoader());
        viewport = new Viewport(12, 12, creatures);

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

        networkListener = new NetworkListener(this, (r) -> Gdx.app.postRunnable(r));
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

        float deltaSeconds = Gdx.graphics.getRawDeltaTime();

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

        if (DEBUG) {
            BitmapFont font = new BitmapFont();
            SpriteBatch debug = new SpriteBatch();

            debug.begin();
            font.draw(debug, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10f, 40f);
            if (player != null) {
                font.draw(debug, "Player: " + player.getPosition().toString(), 10f, 20f);
            }
            debug.end();

            font.dispose();
            debug.dispose();
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
            networkListener.stop();
            networkController.stop();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    public void handle(TickFrame tick) {
        for (TickFrame.AddedEntity added : tick.getAdded()) {
            CreatureImpl creature = creatures.get(added.id);
            if (creature == null) {
                CreaturePrototype proto = prototypeFactory.get(CreaturePrototype.class, added.proto);
                creature = new CreatureImpl(added.id, proto);
                clock.subscribe(creature);
                creatures.put(added.id, creature);
            }

            creature.teleport(new Position(added.x, added.y));
        }

        for (TickFrame.MovedEntity moved : tick.getMoved()) {
            if (moved.id == player.getId()) {
                // Server can't force us to move
                continue;
            }

            CreatureImpl creature = creatures.get(moved.id);
            if (creature == null) {
                System.out.println("Missing: " + moved.id);
                continue;
            }

            Direction direction = Direction.decode(moved.code);

            creature.move(direction);
        }

        for (TickFrame.RemovedEntity removed : tick.getRemoved()) {
            CreatureImpl creature = creatures.get(removed.id);
            if (creature == null) {
                System.out.println("Missing: " + removed.id);
                continue;
            }

            creatures.remove(removed.id, creature);
            clock.unsubscribe(creature);
        }
    }

    public void handle(IdentityFrame identity) {
        CreaturePrototype playerProto = prototypeFactory.get(CreaturePrototype.class, 1);
        player = new CreatureImpl(identity.getIdentity(), playerProto);
        player.teleport(new Position(50, 50));

        viewport.setFocus(player);
        viewport.setZone(zone);

        input = new CreatureController(networkController, player, zone);
        Gdx.input.setInputProcessor(input);

        clock.subscribe(player);
        creatures.put(identity.getIdentity(), player);
    }
}
