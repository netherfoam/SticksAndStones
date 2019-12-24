package org.maxgamer.sticks.core;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import org.maxgamer.sticks.core.controller.CreatureController;
import org.maxgamer.sticks.core.prototype.CreaturePrototype;
import org.maxgamer.sticks.core.tick.Clock;
import org.maxgamer.sticks.core.world.entity.CreatureImpl;
import org.maxgamer.sticks.core.world.view.Viewport;

public class Launcher implements ApplicationListener {
    private Clock clock;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private CreatureController movement;
    private OrthographicCamera camera;
    private CreatureImpl player;
    private Viewport viewport;
    private SpriteBatch spriteBatch;
    private long lastRender = System.nanoTime();

    public Launcher() {

    }

    @Override
    public void create() {
        map = new TmxMapLoader().load("land.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / 32f);
        camera = new OrthographicCamera(20, 20);
        camera.position.set(50, 50, 0);
        mapRenderer.setView(camera);

        spriteBatch = new SpriteBatch();
        clock = new Clock();

        viewport = new Viewport(spriteBatch, new FloatingPosition(50, 50), 12, 12);

        CreaturePrototype playerProto = new CreaturePrototype();
        playerProto.setAnimations("trainer.png");

        player = new CreatureImpl(playerProto);
        player.teleport(new Position(50, 50));

        movement = new CreatureController(player);
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

        spriteBatch.setProjectionMatrix(camera.combined);

        mapRenderer.setView(camera);
        mapRenderer.render();

        spriteBatch.begin();
        player.render(deltaSeconds, viewport);
        spriteBatch.end();

        lastRender = start;
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
