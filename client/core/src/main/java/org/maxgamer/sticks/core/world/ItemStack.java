package org.maxgamer.sticks.core.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.maxgamer.sticks.core.prototype.ItemPrototype;

public class ItemStack {
    private ItemPrototype prototype;
    private TextureRegion texture;

    public ItemStack(ItemPrototype prototype) {
        this.prototype = prototype;

        Texture texture = new Texture(Gdx.files.internal(prototype.texture));
        this.texture = new TextureRegion(texture, prototype.column * prototype.width, prototype.row * prototype.height, prototype.width, prototype.height);
    }

    public ItemPrototype getPrototype() {
        return prototype;
    }

    public TextureRegion getTexture() {
        return texture;
    }
}
