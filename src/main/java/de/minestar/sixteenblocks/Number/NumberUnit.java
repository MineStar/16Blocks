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

package de.minestar.sixteenblocks.Number;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.bukkit.World;

import de.minestar.sixteenblocks.Core.Core;
import de.minestar.sixteenblocks.Units.StructureBlock;

public class NumberUnit {
    private int number = -1;
    private static int Y = 3;
    private int width = 0;
    private ArrayList<StructureBlock> blockList = new ArrayList<StructureBlock>();

    public NumberUnit(int number) {
        this.number = number;
        if (number >= 0 && number <= 9) {
            this.loadFromBitmap();
        }
    }

    private void loadFromBitmap() {
        // LOAD IMAGEDATA
        try {
            BufferedImage img = ImageIO.read(new File(Core.getInstance().getDataFolder(), "numbers.png"));
            int startX = this.number * 7;
            Color thisColor;
            int thisWidth = 0;

            for (int y = 0; y < img.getHeight(); y++) {
                int nowX = 0;
                for (int x = startX; x < startX + 7; x++) {
                    thisColor = new Color(img.getRGB(x, y));
                    if (y == 0) {
                        if (thisColor.getRed() != 255) {
                            thisWidth++;
                        }
                    }

                    if (thisColor.getGreen() == 255) {
                        this.addBlock(-nowX, img.getHeight() - y - 1, 35, (byte) 15);
                    } else if (thisColor.getBlue() == 255) {
                        this.addBlock(-nowX, img.getHeight() - y - 1, 35, (byte) 8);
                    }
                    nowX++;
                    if (thisColor.getRed() == 255) {
                        nowX--;
                    }
                }
            }
            this.setWidth(thisWidth);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean represents(int number) {
        return this.number == number;
    }

    public void addBlock(int x, int z, int TypeID, byte SubID) {
        this.blockList.add(new StructureBlock(x, Y, z, TypeID, SubID));
    }

    public void print(World world, int baseX, int baseZ) {
        for (StructureBlock block : this.blockList) {
            world.getBlockAt(baseX + block.getX(), block.getY(), baseZ + block.getZ()).setTypeIdAndData(block.getTypeID(), block.getSubID(), false);
        }
    }

    protected void setWidth(int width) {
        this.width = width;
    }

    public int getWidth() {
        return this.width;
    }
}
