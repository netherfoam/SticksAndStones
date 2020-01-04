package org.maxgamer.sticks.core;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import org.maxgamer.sticks.common.clock.Clock;
import org.maxgamer.sticks.core.controller.CreatureController;
import org.maxgamer.sticks.core.network.NetworkController;
import org.maxgamer.sticks.core.network.NetworkListener;
import org.maxgamer.sticks.core.prototype.CreaturePrototype;
import org.maxgamer.sticks.core.prototype.ItemPrototype;
import org.maxgamer.sticks.core.prototype.PrototypeFactory;
import org.maxgamer.sticks.core.viewport.Viewport;
import org.maxgamer.sticks.core.world.ItemStack;
import org.maxgamer.sticks.core.world.NetworkHandler;
import org.maxgamer.sticks.core.world.World;
import org.maxgamer.sticks.core.world.Zone;
import org.maxgamer.sticks.core.world.entity.CreatureImpl;
import org.maxgamer.sticks.common.world.EntityList;

import java.io.IOException;
import java.io.UncheckedIOException;

public class Game implements ApplicationListener {
    public static final boolean DEBUG = true;

    private Clock clock;
    private CreatureController input;
    private CreatureImpl player;
    private Viewport viewport;
    private NetworkController networkController;
    private NetworkListener networkListener;
    private PrototypeFactory prototypeFactory;
    private World world;

    @Override
    public void create() {
        Gdx.graphics.setTitle("Sticks And Stones");
        Gdx.graphics.setDisplayMode(Viewport.DEFAULT_RES_WIDTH, Viewport.DEFAULT_RES_HEIGHT, false);
        world = new World(new Zone(new TmxMapLoader()));
        viewport = new Viewport(12, 12, world);

        prototypeFactory = new PrototypeFactory();
        clock = new Clock(Settings.TICKS_PER_SECOND);

        networkController = new NetworkController();
        try {
            networkController.connect("localhost", 2713);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        NetworkHandler networkHandler = new NetworkHandler(this, clock, prototypeFactory, world);
        networkListener = new NetworkListener(networkHandler, (r) -> Gdx.app.postRunnable(r));
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
            // See Clock#tick() method is synchronized on the clock too
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

    public CreatureImpl init(int playerId, int proto, World world) {
        CreaturePrototype playerProto = prototypeFactory.get(CreaturePrototype.class, proto);
        player = new CreatureImpl(playerId, playerProto);
        player.teleport(new Position(50, 50));

        Zone zone = world.getZone();
        input = new CreatureController(networkController, player, zone);

        viewport.setFocus(player);
        viewport.setZone(zone);
        viewport.setInput(input);

        Gdx.input.setInputProcessor(input);

        clock.subscribe(player);

        EntityList<CreatureImpl> creatures = world.getCreatures();
        creatures.add(playerId, player);

        input.subscribe((k) -> {
            int protoId = k - Input.Keys.NUM_0;
            ItemPrototype itemProto = prototypeFactory.get(ItemPrototype.class, protoId);

            ItemStack item = new ItemStack(itemProto);
            viewport.getInventory().add(item);

        }, true, Input.Keys.NUM_1, Input.Keys.NUM_2, Input.Keys.NUM_3, Input.Keys.NUM_4);

        return player;
    }
}
