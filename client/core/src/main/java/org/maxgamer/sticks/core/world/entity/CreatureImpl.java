package org.maxgamer.sticks.core.world.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.maxgamer.sticks.core.prototype.CreaturePrototype;
import org.maxgamer.sticks.core.tick.Clock;
import org.maxgamer.sticks.core.world.Momentum;
import org.maxgamer.sticks.core.world.view.Viewport;

public class CreatureImpl extends EntityImpl implements Tickable {
    private CreaturePrototype prototype;
    private Texture texture;
    private TextureRegion[][] textures;
    private Animation[] animations;
    private Momentum momentum;
    private float frameTime = 0f;

    public CreatureImpl(CreaturePrototype prototype) {
        this.prototype = prototype;
        this.texture = new Texture(Gdx.files.internal(prototype.getAnimations()));
        this.textures = TextureRegion.split(this.texture, 64, 64);

        this.animations = new Animation[4];
        for (int i = 0; i < 4; i++) {
            this.animations[i] = new Animation((float) Momentum.DURATION_IN_TICKS / Clock.TICKS_PER_SECOND / textures[i].length * 2, textures[i]);
        }
    }

    @Override
    public void tick() {
        if (momentum != null && !momentum.isDone()) {
            position = momentum.apply(position);

            if (momentum.isDone()) {
                momentum = null;
            }
        }
    }

    public boolean isMoving() {
        return momentum != null && !momentum.isDone();
    }

    public boolean move(Momentum momentum) {
        if (this.momentum != null && !this.momentum.isDone()) {
            return false;
        }

        this.momentum = momentum;
        if (momentum != null) {
            this.direction = momentum.getDirection();
        }

        return true;
    }

    public void render(float delta, Viewport viewport) {
        int progress = 0;

        if (momentum != null && !momentum.isDone()) {
            progress = momentum.getProgress();
        }

        /*TextureRegion sprite = animations[direction.ordinal()][progress % 4];
        viewport.getSpriteBatch().draw(sprite, position.getX(), position.getY(), width, height);*/

        frameTime += delta;

        Animation animation = animations[direction.ordinal()];

        TextureRegion region;
        if (isMoving()) {
            region = animation.getKeyFrame(frameTime, true);
        } else {
            region = animation.getKeyFrame(0, true);
        }

        viewport.getSpriteBatch().draw(region, position.getX(), position.getY(), width, height);
    }
}
