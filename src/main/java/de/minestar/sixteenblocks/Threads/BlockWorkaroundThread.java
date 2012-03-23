/*
 * Copyright (C) 2012 MineStar.de 
 * 
 * This file is part of SixteenBlocks.
 * 
 * SixteenBlocks is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * 
 * SixteenBlocks is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with SixteenBlocks.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.minestar.sixteenblocks.Threads;

import net.minecraft.server.World;

import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftWorld;

public class BlockWorkaroundThread implements Runnable {

    private final Location blockLocation;
    private final World nativeWorld;

    public BlockWorkaroundThread(CraftWorld cWorld, Location blockLocation) {
        this.blockLocation = blockLocation;
        this.nativeWorld = cWorld.getHandle();
    }

    @Override
    public void run() {
        this.nativeWorld.setTypeId(this.blockLocation.getBlockX(), this.blockLocation.getBlockY() - 1, this.blockLocation.getBlockZ(), 0);
    }

}
