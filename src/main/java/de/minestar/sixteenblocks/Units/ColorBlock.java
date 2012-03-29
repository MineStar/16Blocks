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

package de.minestar.sixteenblocks.Units;

import java.awt.Color;

public class ColorBlock {

    private final int TypeID;
    private final byte SubID;
    private final Color realColor;

    public ColorBlock(int TypeID, Color realColor) {
        this(TypeID, (byte) 0, realColor);
    }

    public ColorBlock(int TypeID, byte SubID, Color realColor) {
        this.TypeID = TypeID;
        this.SubID = SubID;
        this.realColor = realColor;
    }

    public boolean isCloser(Color pixelColor, int delta) {
        return (this.getDelta(pixelColor) < delta);
    }

    public int getDelta(Color pixelColor) {
        int deltaR = Math.abs(pixelColor.getRed() - realColor.getRed());
        int deltaG = Math.abs(pixelColor.getGreen() - realColor.getGreen());
        int deltaB = Math.abs(pixelColor.getBlue() - realColor.getBlue());
        return deltaR + deltaG + deltaB;
    }

    public int getTypeID() {
        return this.TypeID;
    }

    public byte getSubID() {
        return this.SubID;
    }
}
