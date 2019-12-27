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
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import org.maxgamer.sticks.core.controller.CreatureController;
import org.maxgamer.sticks.core.network.NetworkController;
import org.maxgamer.sticks.core.prototype.CreaturePrototype;
import org.maxgamer.sticks.core.tick.Clock;
import org.maxgamer.sticks.core.world.Zone;
import org.maxgamer.sticks.core.world.entity.CreatureImpl;
import org.maxgamer.sticks.core.world.view.Viewport;

import java.io.IOException;
import java.io.UncheckedIOException;

public class Launcher implements ApplicationListener {
    private static final boolean DEBUG = true;

    private static final int ASPECT_WIDTH = 16;
    private static final int ASPECT_HEIGHT = 9;

    private static final float VIEWPORT_MULTIPLYER = 1.25f;
    private static final float VIEWPORT_WIDTH = (int) (ASPECT_WIDTH * VIEWPORT_MULTIPLYER);
    private static final float VIEWPORT_HEIGHT = (int) (ASPECT_HEIGHT * VIEWPORT_MULTIPLYER);

    private static final int DEFAULT_RES_WIDTH = ASPECT_WIDTH * 100;
    private static final int DEFAULT_RES_HEIGHT = ASPECT_HEIGHT * 100;

    private Clock clock;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private CreatureController movement;
    private OrthographicCamera camera;
    private CreatureImpl player;
    private Viewport viewport;
    private SpriteBatch entityBatch;
    private long lastRender = System.nanoTime();
    private TiledMap collisionMap;
    private Zone zone;
    private NetworkController network;

    public Launcher() {

    }

    @Override
    public void create() {
        Gdx.graphics.setDisplayMode(DEFAULT_RES_WIDTH, DEFAULT_RES_HEIGHT, false);
        map = new TmxMapLoader().load("land.tmx");
        collisionMap = new TmxMapLoader().load("land_collision_rd.tmx");
        zone = new Zone(map, collisionMap);

        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / 32f);
        camera = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        camera.position.set(50, 50, 0);
        mapRenderer.setView(camera);

        entityBatch = new SpriteBatch();
        clock = new Clock();

        viewport = new Viewport(entityBatch, new FloatingPosition(50, 50), 12, 12);

        CreaturePrototype playerProto = new CreaturePrototype();
        playerProto.setAnimations("trainer.png");
        playerProto.setFootstep(new String[]{
                "footstep/0.wav",
                "footstep/1.wav",
                "footstep/2.wav",
                "footstep/3.wav",
                "footstep/4.wav",
                "footstep/5.wav",
                "footstep/6.wav",
                "footstep/7.wav",
                "footstep/8.wav",
                "footstep/9.wav",
        });

        Music music = Gdx.audio.newMusic(Gdx.files.internal("sound/music/Breeze.ogg"));
        music.setVolume(0.55f);
        music.setLooping(true);
        music.play();

        player = new CreatureImpl(playerProto);
        player.teleport(new Position(50, 50));

        network = new NetworkController();
        try {
            network.connect("localhost", 2713);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        movement = new CreatureController(network, player, zone);
        clock.subscribe(player);
        clock.start();
        Gdx.input.setInputProcessor(movement);
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

        movement.apply();
        camera.update();

        FloatingPosition center = player.getPosition();
        camera.position.set(center.getX(), center.getY(), 0);
        camera.update();

        entityBatch.setProjectionMatrix(camera.combined);

        mapRenderer.setView(camera);
        mapRenderer.render();

        entityBatch.begin();
        player.render(deltaSeconds, viewport);

        entityBatch.end();

        lastRender = start;

        if (DEBUG) {
            BitmapFont font = new BitmapFont();
            SpriteBatch debug = new SpriteBatch();

            debug.begin();
            font.draw(debug, "Player: " + player.getPosition().toString(), 10f, 20f);
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
}
