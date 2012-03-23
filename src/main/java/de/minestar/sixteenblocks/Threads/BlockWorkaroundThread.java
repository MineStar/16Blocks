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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Location;

public class BlockWorkaroundThread implements Runnable {

    private final List<Location> blockList = Collections.synchronizedList(new ArrayList<Location>());

    public void addBlock(Location blockLocation) {
        synchronized (this.blockList) {
            this.blockList.add(blockLocation);
        }
    }

    @Override
    public void run() {
        if (this.blockList.size() < 1)
            return;

        synchronized (this.blockList) {
            for (Location thisLocation : this.blockList) {
                thisLocation.getBlock().setTypeId(0, false);
            }
            this.blockList.clear();
        }
    }
}
