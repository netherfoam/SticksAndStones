package org.maxgamer.sticks.core.hud;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.maxgamer.sticks.core.viewport.Render;
import org.maxgamer.sticks.core.viewport.Viewport;
import org.maxgamer.sticks.core.world.ItemStack;

import java.util.Objects;

public class Inventory implements Render {
    private boolean open = false;

    private int width;
    private int height;
    private ItemStack[] items;

    private SpriteBatch batch = new SpriteBatch();

    public Inventory(int width, int height) {
        if (width < 2 || height < 2) {
            throw new IllegalArgumentException("W: " + width + " H: " + height);
        }

        this.width = width;
        this.height = height;
        this.items = new ItemStack[width * height];

        setOpen(true);
    }

    public void add(ItemStack item) {
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null) continue;

            items[i] = item;
            return;
        }
    }

    public void remove(ItemStack item) {
        for (int i = 0; i < items.length; i++) {
            if (Objects.equals(items[i], item)) {
                items[i] = null;
                return;
            }
        }
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        if (open == this.open) {
            return;
        }

        if (this.open) {
            this.open = false;
            this.batch.dispose();
            this.batch = null;
        } else {
            this.open = true;
            this.batch = new SpriteBatch();
            this.batch.getProjectionMatrix().setToScaling(1 / 10f, 1 / 10f, 1);
            this.batch.getProjectionMatrix().translate(5, -9, 0);
        }
    }

    @Override
    public void render(float delta, Viewport viewport) {
        if (!open) return;

        batch.begin();

        // Now draw items
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                ItemStack item = items[i * height + j];
                if (item == null) continue;

                TextureRegion texture = item.getTexture();
                batch.draw(texture, i + 0.1f, j + 0.1f, 0.8f, 0.8f);
            }
        }

        batch.end();
    }
}
