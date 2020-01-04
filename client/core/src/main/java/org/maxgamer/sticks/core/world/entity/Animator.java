package org.maxgamer.sticks.core.world.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.maxgamer.sticks.common.world.Direction;
import org.maxgamer.sticks.core.Settings;
import org.maxgamer.sticks.core.prototype.AnimatorPrototype;
import org.maxgamer.sticks.core.world.Momentum;

public abstract class Animator {
    public static final float FRAME_DURATION = (float) Momentum.DURATION_IN_TICKS / Settings.TICKS_PER_SECOND / 4 * 2;

    public abstract Animation getAnimation(Direction direction);

    public static class SheetAnimator extends Animator {
        private Animation[] animations;

        public SheetAnimator(AnimatorPrototype.SheetAnimatorPrototype proto) {
            Texture texture = new Texture(Gdx.files.internal(proto.sheet));
            TextureRegion[][] textures = TextureRegion.split(texture, proto.width, proto.height);

            animations = new Animation[4];

            for (int i = 0; i < 4; i++) {
                this.animations[i] = new Animation(FRAME_DURATION, textures[i]);
            }
        }

        @Override
        public Animation getAnimation(Direction direction) {
            return animations[direction.ordinal()];
        }
    }

    public static class OrdinalAnimator extends Animator {
        private Animation[] animations;

        public OrdinalAnimator(AnimatorPrototype.OrdinalAnimatorPrototype proto) {
            animations = new Animation[4];
            for (Direction direction : Direction.values()) {
                AnimatorPrototype.OrdinalAnimatorPrototype.Ordinal ordinal = proto.get(direction);
                Texture texture = new Texture(ordinal.sheet);

                TextureRegion[][] frames = TextureRegion.split(texture, ordinal.width, ordinal.height);
                animations[direction.ordinal()] = new Animation(FRAME_DURATION, frames[0]);
            }
        }

        @Override
        public Animation getAnimation(Direction direction) {
            return animations[direction.ordinal()];
        }
    }
}
