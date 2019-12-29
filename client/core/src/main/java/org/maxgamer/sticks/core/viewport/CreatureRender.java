package org.maxgamer.sticks.core.viewport;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import org.maxgamer.sticks.core.FloatingPosition;
import org.maxgamer.sticks.core.Game;
import org.maxgamer.sticks.core.world.entity.CreatureImpl;

import java.util.Map;

public class CreatureRender implements Render {
    private Map<Integer, CreatureImpl> creatures;
    private SpriteBatch batch = new SpriteBatch();
    private SpriteBatch debug = new SpriteBatch();

    public CreatureRender(Map<Integer, CreatureImpl> creatures) {
        this.creatures = creatures;
    }

    @Override
    public void render(float delta, Viewport viewport) {
        // Draw creatures
        batch.setProjectionMatrix(viewport.worldMatrix());
        batch.begin();
        for (CreatureImpl creature : creatures.values()) {
            creature.render(delta, batch);
        }
        batch.end();

        if (Game.DEBUG) {
            // Draw debug info
            debug.begin();
            BitmapFont font = new BitmapFont();
            for (CreatureImpl creature : creatures.values()) {
                FloatingPosition position = creature.getPosition();
                Vector3 screenCoords = viewport.getCamera().project(new Vector3(position.getX() + 0.5f, position.getY() + 1.1f, 0));
                String text = "(" + creature.getId() + ")";
                BitmapFont.TextBounds bounds = font.getBounds(text);
                font.draw(debug, text, screenCoords.x - bounds.width / 2, screenCoords.y - bounds.height / 2);
            }
            debug.end();
            font.dispose();
        }
    }
}
