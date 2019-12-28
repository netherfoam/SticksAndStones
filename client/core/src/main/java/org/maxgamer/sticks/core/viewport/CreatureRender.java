package org.maxgamer.sticks.core.viewport;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.maxgamer.sticks.core.world.entity.CreatureImpl;

import java.util.ArrayList;
import java.util.List;

public class CreatureRender implements Render {
    private List<CreatureImpl> creatures = new ArrayList<>();
    private SpriteBatch batch = new SpriteBatch();

    public void add(CreatureImpl creature) {
        this.creatures.add(creature);
    }

    public void remove(CreatureImpl creature) {
        this.creatures.remove(creature);
    }

    @Override
    public void render(float delta, Viewport viewport) {
        batch.setProjectionMatrix(viewport.worldMatrix());
        batch.begin();

        for (CreatureImpl creature : creatures) {
            creature.render(delta, batch);
        }

        batch.flush();
        batch.end();
    }
}
