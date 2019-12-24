package org.maxgamer;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.Creature;
import de.gurkenlabs.litiengine.environment.Environment;
import de.gurkenlabs.litiengine.environment.tilemap.IMap;
import de.gurkenlabs.litiengine.graphics.FreeFlightCamera;
import de.gurkenlabs.litiengine.gui.screens.GameScreen;
import de.gurkenlabs.litiengine.gui.screens.Resolution;
import de.gurkenlabs.litiengine.input.Input;
import de.gurkenlabs.litiengine.resources.Resources;

import java.awt.event.KeyEvent;

public class Launcher {
    public static void main(String[] args) {
        Game.init(args);
        Game.window().setResolution(Resolution.Ratio16x9.RES_1920x1080);
        Resources.load("../game.litidata");
        IMap map = Resources.maps().get("land");
        Game.world().loadEnvironment(map);

        Game.screens().add(new GameScreen());
        Game.graphics().setBaseRenderScale(1f);
        Game.world().setCamera(new FreeFlightCamera(100, 100));

        Game.config().client().setUpdaterate(60);
        Game.config().client().setMaxFps(144);
        Creature creature = new Creature();
        creature.setSpritePrefix();
        Game.world().environment().add(creature);

        Input.keyboard().onKeyTyped(KeyEvent.VK_ESCAPE, (key) -> System.exit(0));
        Game.start();
    }
}
