package org.maxgamer.sticks.core.world.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.maxgamer.sticks.core.prototype.CreaturePrototype;
import org.maxgamer.sticks.core.sound.SoundKit;
import org.maxgamer.sticks.core.sound.SoundLoop;
import org.maxgamer.sticks.core.sound.SoundType;
import org.maxgamer.sticks.core.tick.Clock;
import org.maxgamer.sticks.common.world.Direction;
import org.maxgamer.sticks.core.world.Momentum;

public class CreatureImpl extends EntityImpl implements Tickable {
    private int id;
    private CreaturePrototype prototype;
    private Texture texture;
    private TextureRegion[][] textures;
    private Animation[] animations;
    private Momentum momentum = new Momentum();
    private float frameTime = 0f;
    private SoundKit sounds;
    private SoundLoop walking;

    public CreatureImpl(int id, CreaturePrototype prototype) {
        this.id = id;
        this.prototype = prototype;
        this.texture = new Texture(Gdx.files.internal(prototype.getAnimations()));
        this.textures = TextureRegion.split(this.texture, 64, 64);
        this.sounds = new SoundKit();

        for (String footstep : prototype.getFootstep()) {
            this.sounds.load(SoundType.FOOTSTEP, Gdx.files.internal("sound/" + footstep));
        }
        walking = new SoundLoop(this.sounds.get(SoundType.FOOTSTEP), (int) ((float) Momentum.DURATION_IN_TICKS / Clock.TICKS_PER_SECOND / textures[0].length * 4 * 1000), 0.5f);

        this.animations = new Animation[4];
        for (int i = 0; i < 4; i++) {
            this.animations[i] = new Animation((float) Momentum.DURATION_IN_TICKS / Clock.TICKS_PER_SECOND / textures[i].length * 2, textures[i]);
        }
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean tick() {
        if (!momentum.isDone()) {
            position = momentum.apply(position);
            setDirection(momentum.getDirection());
        }

        return false;
    }

    public boolean isMoving() {
        return momentum != null && !momentum.isDone();
    }

    public boolean move(Direction direction) {
        this.momentum.append(direction);
        this.direction = this.momentum.getDirection();

        return true;
    }

    public void setDirection(Direction direction) {
        if (direction == null) {
            // Can't face null, keep old direction
            return;
        }

        this.direction = direction;
    }

    public void render(float delta, SpriteBatch batch) {
        frameTime += delta;

        Animation animation = animations[direction.ordinal()];

        TextureRegion region;
        if (isMoving()) {
            region = animation.getKeyFrame(frameTime, true);
            walking.play();
        } else {
            region = animation.getKeyFrame(0, true);
        }

        batch.draw(region, position.getX(), position.getY(), width, height);
    }
}
