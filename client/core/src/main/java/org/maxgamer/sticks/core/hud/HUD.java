package org.maxgamer.sticks.core.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.maxgamer.sticks.core.viewport.Render;
import org.maxgamer.sticks.core.viewport.Viewport;

public class HUD implements Render {
    private Texture header;
    private Texture contents;
    private Texture right;
    private Texture bottom;

    private SpriteBatch batch;

    private Inventory inventory;
    private SkillHUD skills;

    public HUD(Inventory inventory) {
        this.inventory = inventory;
        this.skills = new SkillHUD();

        header = new Texture(Gdx.files.internal("hud/rs/S1036I0.png"));
        right = new Texture(Gdx.files.internal("hud/rs/S1035I0.png"));
        contents = new Texture(Gdx.files.internal("hud/rs/S1031I0.png"));
        bottom = new Texture(Gdx.files.internal("hud/rs/S1032I0.png"));

        batch = new SpriteBatch();
        batch.getProjectionMatrix().setToScaling(1 / 10f, 1 / 10f, 1);
        batch.getProjectionMatrix().translate(5, -9, 0);
    }

    @Override
    public void render(float delta, Viewport viewport) {
        batch.begin();

        if (inventory.isOpen()) {
            batch.draw(header, -1, 7, 6, 1);
            batch.draw(right, 4, 0, 1, 7);
            batch.draw(right, 0, 0, -1, 7);
            batch.draw(bottom, -1, -1, 6, 1);
            batch.draw(contents, 0, 0, 4, 7);
        }

        batch.end();

        this.skills.render(delta, viewport);
        this.inventory.render(delta, viewport);
    }
}
