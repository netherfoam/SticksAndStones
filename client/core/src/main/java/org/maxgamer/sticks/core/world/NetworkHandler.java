package org.maxgamer.sticks.core.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import org.maxgamer.sticks.common.clock.Clock;
import org.maxgamer.sticks.common.network.frame.IdentityFrame;
import org.maxgamer.sticks.common.network.frame.TickFrame;
import org.maxgamer.sticks.common.world.Direction;
import org.maxgamer.sticks.core.Game;
import org.maxgamer.sticks.core.Position;
import org.maxgamer.sticks.core.prototype.CreaturePrototype;
import org.maxgamer.sticks.core.prototype.PrototypeFactory;
import org.maxgamer.sticks.core.world.entity.CreatureImpl;
import org.maxgamer.sticks.common.world.EntityList;

public class NetworkHandler {
    private Game game;
    private Clock clock;
    private PrototypeFactory prototypeFactory;
    private World world;

    private CreatureImpl player;

    public NetworkHandler(
            Game game,
            Clock clock,
            PrototypeFactory prototypeFactory,
            World world
    ) {
        this.game = game;
        this.clock = clock;
        this.prototypeFactory = prototypeFactory;
        this.world = world;
    }

    public void handle(TickFrame tick) {
        EntityList<CreatureImpl> creatures = world.getCreatures();

        for (TickFrame.AddedEntity added : tick.getAdded()) {
            CreatureImpl creature = creatures.get(added.id);
            if (creature == null) {
                CreaturePrototype proto = prototypeFactory.get(CreaturePrototype.class, added.proto);
                creature = new CreatureImpl(added.id, proto);
                clock.subscribe(creature);
                creatures.add(added.id, creature);
            }

            creature.teleport(new Position(added.x, added.y));
        }

        for (TickFrame.MovedEntity moved : tick.getMoved()) {
            if (moved.id == player.getId()) {
                // Server can't force us to move
                continue;
            }

            CreatureImpl creature = creatures.get(moved.id);
            if (creature == null) {
                System.out.println("Missing: " + moved.id);
                continue;
            }

            Direction direction = Direction.decode(moved.code);

            creature.move(direction);
        }

        for (TickFrame.RemovedEntity removed : tick.getRemoved()) {
            CreatureImpl creature = creatures.get(removed.id);
            if (creature == null) {
                System.out.println("Missing: " + removed.id);
                continue;
            }

            creatures.remove(removed.id);
            clock.unsubscribe(creature);
        }
    }

    public void handle(IdentityFrame identity) {
        this.player = game.init(identity.getIdentity(), identity.getProto(), world);

        Music music = Gdx.audio.newMusic(Gdx.files.internal("sound/music/Breeze.ogg"));
        music.setVolume(0.55f);
        music.setLooping(true);
        music.play();
    }
}
