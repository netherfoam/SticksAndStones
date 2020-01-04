package org.maxgamer.sticks.core.prototype;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.maxgamer.sticks.common.world.Direction;
import org.maxgamer.sticks.core.world.entity.Animator;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(name = "ORDINAL", value = AnimatorPrototype.OrdinalAnimatorPrototype.class),
        @JsonSubTypes.Type(name = "SHEET", value = AnimatorPrototype.SheetAnimatorPrototype.class)
})
public abstract class AnimatorPrototype {
    public abstract String getType();
    public abstract Animator load();

    public static class SheetAnimatorPrototype extends AnimatorPrototype {
        public String sheet;
        public int width;
        public int height;

        @Override
        public String getType() {
            return "SHEET";
        }

        @Override
        public Animator load() {
            return new Animator.SheetAnimator(this);
        }
    }

    public static class OrdinalAnimatorPrototype extends AnimatorPrototype {
        public Ordinal north;
        public Ordinal south;
        public Ordinal east;
        public Ordinal west;

        public Ordinal get(Direction direction) {
            if (direction == Direction.NORTH) {
                return north;
            }

            if (direction == Direction.SOUTH) {
                return south;
            }

            if (direction == Direction.EAST) {
                return east;
            }

            if (direction == Direction.WEST) {
                return west;
            }

            return null;
        }

        @Override
        public String getType() {
            return "ORDINAL";
        }

        @Override
        public Animator load() {
            return new Animator.OrdinalAnimator(this);
        }

        public static class Ordinal {
            public int width;
            public int height;
            public String sheet;
        }
    }
}
