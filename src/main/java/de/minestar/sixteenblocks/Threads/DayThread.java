package de.minestar.sixteenblocks.Threads;

import org.bukkit.World;

public class DayThread implements Runnable {

    private final World world;
    private final long time;

    public DayThread(World world, long time) {
        this.world = world;
        if (time > 23999)
            time = 23999;
        else if (time < 0)
            time = 0;
        this.time = time;
    }

    @Override
    public void run() {
        this.world.setTime(this.time);
    }
}
