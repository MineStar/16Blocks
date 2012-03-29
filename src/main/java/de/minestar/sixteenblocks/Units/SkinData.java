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
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public class SkinData {

    private static List<ColorBlock> blockData;

    private final File file;
    private boolean loaded = false;
    private boolean updated = false;
    private Color[][] pixelData = null;
    private ColorBlock[][] blockTypes = null;
    private int width, height;

    private static void initBlockData() {
        // INIT LIST
        blockData = new ArrayList<ColorBlock>();

        // WOOL
        blockData.add(new ColorBlock(Material.WOOL.getId(), (byte) 0, new Color(218, 218, 218)));
        blockData.add(new ColorBlock(Material.WOOL.getId(), (byte) 1, Color.ORANGE));
        blockData.add(new ColorBlock(Material.WOOL.getId(), (byte) 2, Color.MAGENTA));
        blockData.add(new ColorBlock(Material.WOOL.getId(), (byte) 3, new Color(120, 150, 210)));
        blockData.add(new ColorBlock(Material.WOOL.getId(), (byte) 4, Color.YELLOW));
        blockData.add(new ColorBlock(Material.WOOL.getId(), (byte) 5, new Color(0, 255, 0)));
        blockData.add(new ColorBlock(Material.WOOL.getId(), (byte) 6, Color.PINK));
        blockData.add(new ColorBlock(Material.WOOL.getId(), (byte) 7, Color.DARK_GRAY));
        blockData.add(new ColorBlock(Material.WOOL.getId(), (byte) 8, Color.GRAY));
        blockData.add(new ColorBlock(Material.WOOL.getId(), (byte) 9, Color.CYAN));
        blockData.add(new ColorBlock(Material.WOOL.getId(), (byte) 10, new Color(106, 50, 154)));
        blockData.add(new ColorBlock(Material.WOOL.getId(), (byte) 11, Color.BLUE));
        blockData.add(new ColorBlock(Material.WOOL.getId(), (byte) 12, new Color(94, 55, 41)));
        blockData.add(new ColorBlock(Material.WOOL.getId(), (byte) 13, Color.GREEN));
        blockData.add(new ColorBlock(Material.WOOL.getId(), (byte) 14, Color.RED));
        blockData.add(new ColorBlock(Material.WOOL.getId(), (byte) 14, new Color(104, 0, 31)));
        blockData.add(new ColorBlock(Material.WOOL.getId(), (byte) 15, Color.BLACK));

        // WOOD
        blockData.add(new ColorBlock(Material.WOOD.getId(), (byte) 0, new Color(180, 144, 90)));
        blockData.add(new ColorBlock(Material.WOOD.getId(), (byte) 1, new Color(120, 88, 54)));
        blockData.add(new ColorBlock(Material.WOOD.getId(), (byte) 2, new Color(238, 180, 159)));
        blockData.add(new ColorBlock(Material.WOOD.getId(), (byte) 3, new Color(184, 135, 100)));

        // OTHER
        blockData.add(new ColorBlock(Material.OBSIDIAN.getId(), new Color(16, 16, 24)));
        blockData.add(new ColorBlock(Material.STONE.getId(), Color.GRAY));
        blockData.add(new ColorBlock(Material.ICE.getId(), new Color(170, 200, 255)));
        blockData.add(new ColorBlock(Material.GOLD_BLOCK.getId(), new Color(255, 255, 128)));
        blockData.add(new ColorBlock(Material.CLAY.getId(), new Color(159, 163, 174)));
        blockData.add(new ColorBlock(Material.NETHER_BRICK.getId(), new Color(70, 10, 20)));
        blockData.add(new ColorBlock(Material.SANDSTONE.getId(), (byte) 3, new Color(230, 223, 178)));
        blockData.add(new ColorBlock(Material.LAPIS_BLOCK.getId(), new Color(42, 79, 140)));
        blockData.add(new ColorBlock(Material.SNOW_BLOCK.getId(), Color.WHITE));
    }

    public SkinData(File file) {
        if (blockData == null) {
            initBlockData();
        }
        this.file = file;
        this.loadData();
        this.createColorTable();
    }

    public void createSkin(Location location) {
        this.createSkin(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public void createSkin(World world, int x, int y, int z) {
        // CHECK: BLOCKDATA CREATED
        if (!this.updated) {
            throw new RuntimeException("BlockData was not built!");
        }

        // CREATE SKIN
        // TODO: BUILD A REAL SKIN
        for (int thisX = 0; thisX < this.width; thisX++) {
            for (int thisY = 0; thisY < this.height; thisY++) {
                world.getBlockAt(x + thisX, y + thisY, z).setTypeIdAndData(this.blockTypes[thisX][thisY].getTypeID(), this.blockTypes[thisX][thisY].getSubID(), false);
            }
        }
    }

    private void createColorTable() {
        // CHECK: PIXELDATA EXISTS
        if (!this.loaded) {
            throw new RuntimeException("PixelData was not found!");
        }

        // CREATE TABLE
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                this.updateBlockData(x, y);
            }
        }

        this.updated = true;
    }

    private void updateBlockData(int x, int y) {
        ColorBlock newColor = blockData.get(0);
        int currentDelta = Integer.MAX_VALUE;
        for (ColorBlock thisColor : blockData) {
            if (thisColor.isCloser(this.pixelData[x][y], currentDelta)) {
                newColor = thisColor;
                currentDelta = thisColor.getDelta(this.pixelData[x][y]);
            }
        }
        this.blockTypes[x][y] = newColor;
    }

    private void loadData() {
        // CHECK: FILE EXISTS
        if (!this.file.exists()) {
            throw new RuntimeException("Skinfile '" + this.file.getName() + "' does not exist!");
        }

        // LOAD IMAGEDATA
        try {
            BufferedImage img = ImageIO.read(this.file);
            this.width = img.getWidth();
            this.height = img.getHeight();
            this.pixelData = new Color[this.width][this.height];
            this.blockTypes = new ColorBlock[this.width][this.height];
            for (int x = 0; x < this.width; x++) {
                for (int y = 0; y < this.height; y++) {
                    this.pixelData[x][this.height - y - 1] = new Color(img.getRGB(x, y));
                }
            }
            this.loaded = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
