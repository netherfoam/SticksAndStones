package org.maxgamer.sticks.core.tick;

import org.maxgamer.sticks.core.world.entity.Tickable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public class Clock {
    public static final int TICKS_PER_SECOND = 60;

    private Logger logger = Logger.getLogger("Clock");

    private boolean running = false;
    private Thread thread;

    private List<Tickable> subscribers = new ArrayList<>(200);

    public Clock() {
        this.thread = new Thread("Clock") {
            @Override
            public void run() {
                running = true;
                int tickDuration = 1000 / TICKS_PER_SECOND;

                while (running) {
                    long start = System.currentTimeMillis();
                    tick();
                    long end = System.currentTimeMillis();
                    long delta = end - start;

                    if (delta < tickDuration) {
                        try {
                            Thread.sleep(tickDuration - delta);
                        } catch (InterruptedException e) {
                            running = false;
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                }
            }
        };
    }

    public void start() {
        thread.start();
    }

    private synchronized void tick() {
        Iterator<Tickable> it = subscribers.iterator();
        while (it.hasNext()) {
            Tickable tickable = it.next();
            try {
                boolean ended = tickable.tick();

                if (ended) {
                    it.remove();
                }
            } catch (Exception e) {
                logger.warning(() -> tickable.toString() + " threw " + e.getClass().getName() + ": " + e.getMessage() + ", trace: " + e.toString());
                e.printStackTrace();

                it.remove();
                continue;
            }
        }
    }

    public synchronized void subscribe(Tickable t) {
        if (subscribers.contains(t)) {
            return;
        }

        subscribers.add(t);
    }

    public synchronized boolean unsubscribe(Tickable t) {
        return subscribers.remove(t);
    }

    public void stop() throws InterruptedException {
        running = false;
        this.thread.join();
    }
}
