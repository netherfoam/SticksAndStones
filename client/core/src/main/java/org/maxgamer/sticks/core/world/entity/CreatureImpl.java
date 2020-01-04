package org.maxgamer.sticks.core.world.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.maxgamer.sticks.common.clock.Tickable;
import org.maxgamer.sticks.common.world.Direction;
import org.maxgamer.sticks.core.Settings;
import org.maxgamer.sticks.core.prototype.CreaturePrototype;
import org.maxgamer.sticks.core.sound.SoundKit;
import org.maxgamer.sticks.core.sound.SoundLoop;
import org.maxgamer.sticks.core.sound.SoundType;
import org.maxgamer.sticks.core.world.Momentum;

public class CreatureImpl extends EntityImpl implements Tickable {
    private int id;
    private CreaturePrototype prototype;
    private Animator animator;
    private Momentum momentum = new Momentum();
    private float frameTime = 0f;
    private SoundKit sounds;
    private SoundLoop walking;

    public CreatureImpl(int id, CreaturePrototype prototype) {
        this.id = id;
        this.prototype = prototype;
        this.animator = prototype.animations.load();
        this.sounds = new SoundKit();

        for (String footstep : prototype.footstep) {
            this.sounds.load(SoundType.FOOTSTEP, Gdx.files.internal("sound/" + footstep));
        }
        walking = new SoundLoop(this.sounds.get(SoundType.FOOTSTEP), (int) ((float) Momentum.DURATION_IN_TICKS / Settings.TICKS_PER_SECOND * 1000), 0.5f);
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
        return !momentum.isDone();
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

        Animation animation = animator.getAnimation(direction);

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
