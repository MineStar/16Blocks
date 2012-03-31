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

package de.minestar.sixteenblocks.Manager;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.World;

import de.minestar.sixteenblocks.Number.NumberUnit;

public class NumberManager {
    private HashMap<Integer, NumberUnit> numberMap = new HashMap<Integer, NumberUnit>();

    public NumberManager() {
        // INIT NUMBERS
        for (int i = 0; i <= 9; i++) {
            this.numberMap.put(i, new NumberUnit(i));
        }
    }

    public void print(int baseX, int baseZ, int number) {
        String text = String.valueOf(number);
        int thisX = 0;
        World world = Bukkit.getWorlds().get(0);
        for (int i = 0; i < text.length(); i++) {
            int num = Integer.valueOf(String.valueOf(text.charAt(i)));
            numberMap.get(num).print(world, baseX - thisX, baseZ);
            if (i < (text.length() - 1)) {
                thisX += (numberMap.get(num).getWidth() - 1);
            } else {
                thisX += (numberMap.get(num).getWidth());
            }
        }
    }
}
