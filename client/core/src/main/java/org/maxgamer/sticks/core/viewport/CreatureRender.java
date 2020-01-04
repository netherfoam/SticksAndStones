package org.maxgamer.sticks.core.viewport;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import org.maxgamer.sticks.core.FloatingPosition;
import org.maxgamer.sticks.core.Game;
import org.maxgamer.sticks.core.world.entity.CreatureImpl;
import org.maxgamer.sticks.common.world.EntityList;

public class CreatureRender implements Render {
    private EntityList<CreatureImpl> creatures;
    private SpriteBatch batch = new SpriteBatch();
    private SpriteBatch debug = new SpriteBatch();

    public CreatureRender(EntityList<CreatureImpl> creatures) {
        this.creatures = creatures;
    }

    @Override
    public void render(float delta, Viewport viewport) {
        // Draw creatures
        batch.setProjectionMatrix(viewport.worldMatrix());
        batch.begin();
        for (CreatureImpl creature : creatures) {
            creature.render(delta, batch);
        }
        batch.end();

        if (Game.DEBUG) {
            // Draw debug info
            debug.begin();
            debug.getTransformMatrix().setToScaling((float) Viewport.DEFAULT_RES_WIDTH / Gdx.graphics.getWidth() , (float) Viewport.DEFAULT_RES_HEIGHT / Gdx.graphics.getHeight(), 1);
            BitmapFont font = new BitmapFont();
            for (CreatureImpl creature : creatures) {
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
