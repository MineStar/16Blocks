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
        blockData.add(new ColorBlock(Material.WOOL.getId(), (byte) 0, new Color(232, 232, 232)));
        blockData.add(new ColorBlock(Material.WOOL.getId(), (byte) 1, Color.ORANGE));
        blockData.add(new ColorBlock(Material.WOOL.getId(), (byte) 2, Color.MAGENTA));
        blockData.add(new ColorBlock(Material.WOOL.getId(), (byte) 3, new Color(120, 150, 210)));
        blockData.add(new ColorBlock(Material.WOOL.getId(), (byte) 4, Color.YELLOW));
        blockData.add(new ColorBlock(Material.WOOL.getId(), (byte) 5, new Color(0, 255, 0)));
        blockData.add(new ColorBlock(Material.WOOL.getId(), (byte) 6, Color.PINK));
        blockData.add(new ColorBlock(Material.WOOL.getId(), (byte) 7, Color.DARK_GRAY));
        blockData.add(new ColorBlock(Material.WOOL.getId(), (byte) 8, Color.GRAY));
        blockData.add(new ColorBlock(Material.WOOL.getId(), (byte) 8, new Color(153, 153, 153)));
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
        blockData.add(new ColorBlock(Material.OBSIDIAN.getId(), new Color(43, 23, 71)));
        blockData.add(new ColorBlock(Material.OBSIDIAN.getId(), new Color(16, 16, 24)));
        blockData.add(new ColorBlock(Material.STONE.getId(), Color.GRAY));
        blockData.add(new ColorBlock(Material.ICE.getId(), new Color(170, 200, 255)));
        blockData.add(new ColorBlock(Material.GOLD_BLOCK.getId(), new Color(255, 255, 128)));
        blockData.add(new ColorBlock(Material.CLAY.getId(), new Color(159, 163, 174)));
        blockData.add(new ColorBlock(Material.CLAY.getId(), new Color(204, 204, 204)));
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

        this.buildLegs(world, x, y, z);
        this.buildChest(world, x, y + 12, z);
        this.buildArms(world, x - 4, y + 12, z);

//        // CREATE SKIN
        // TODO: BUILD A REAL SKIN
        for (int thisX = 0; thisX < this.width; thisX++) {
            for (int thisY = 0; thisY < this.height; thisY++) {
                // world.getBlockAt(x + thisX, y + thisY, z +
                // 2).setTypeIdAndData(this.blockTypes[thisX][thisY].getTypeID(),
                // this.blockTypes[thisX][thisY].getSubID(), false);
            }
        }
    }

    private void buildArms(World world, int x, int y, int z) {
        // RIGHT ARM - FRONT / BACK
        int pixelOffSetX = 44;
        int pixelOffSetY = 0;
        for (int thisX = 0; thisX < 4; thisX++) {
            for (int thisY = 0; thisY < 12; thisY++) {
                world.getBlockAt(x + thisX, y + thisY, z).setTypeIdAndData(this.blockTypes[thisX + pixelOffSetX][thisY].getTypeID(), this.blockTypes[thisX + pixelOffSetX][thisY].getSubID(), false);
                world.getBlockAt(x + thisX, y + thisY, z + 3).setTypeIdAndData(this.blockTypes[thisX + pixelOffSetX + 7][thisY].getTypeID(), this.blockTypes[thisX + pixelOffSetX + 7][thisY].getSubID(), false);
            }
        }

        // RIGHT ARM - SIDE
        pixelOffSetX = 40;
        for (int thisX = 1; thisX < 3; thisX++) {
            for (int thisY = 0; thisY < 12; thisY++) {
                world.getBlockAt(x, y + thisY, z + thisX).setTypeIdAndData(this.blockTypes[thisX + pixelOffSetX][thisY].getTypeID(), this.blockTypes[thisX + pixelOffSetX][thisY].getSubID(), false);
            }
        }

        // RIGHT ARM - TOP
        pixelOffSetX = 44;
        pixelOffSetY = 12;
        for (int thisX = 1; thisX < 4; thisX++) {
            for (int thisY = 1; thisY < 3; thisY++) {
                world.getBlockAt(x + thisX, y + 11, z + thisY).setTypeIdAndData(this.blockTypes[thisX + pixelOffSetX][thisY + pixelOffSetY].getTypeID(), this.blockTypes[thisX + pixelOffSetX][thisY + pixelOffSetY].getSubID(), false);
            }
        }

        // RIGHT ARM - BOTTOM
        pixelOffSetX = 48;
        pixelOffSetY = 12;
        for (int thisX = 1; thisX < 4; thisX++) {
            for (int thisY = 1; thisY < 3; thisY++) {
                world.getBlockAt(x + thisX, y, z + thisY).setTypeIdAndData(this.blockTypes[thisX + pixelOffSetX][thisY + pixelOffSetY].getTypeID(), this.blockTypes[thisX + pixelOffSetX][thisY + pixelOffSetY].getSubID(), false);
            }
        }

        // LEFT ARM - FRONT / BACK
        pixelOffSetX = 48;
        for (int thisX = 0; thisX < 4; thisX++) {
            for (int thisY = 0; thisY < 12; thisY++) {
                world.getBlockAt(x + 3 - thisX + 12, y + thisY, z).setTypeIdAndData(this.blockTypes[pixelOffSetX - thisX][thisY].getTypeID(), this.blockTypes[pixelOffSetX - thisX][thisY].getSubID(), false);
                world.getBlockAt(x + 3 - thisX + 12, y + thisY, z + 3).setTypeIdAndData(this.blockTypes[pixelOffSetX - thisX + 7][thisY].getTypeID(), this.blockTypes[pixelOffSetX - thisX + 7][thisY].getSubID(), false);
            }
        }

        // LEFT ARM - SIDE
        pixelOffSetX = 40;
        for (int thisX = 1; thisX < 3; thisX++) {
            for (int thisY = 0; thisY < 12; thisY++) {
                world.getBlockAt(x + 15, y + thisY, z + thisX).setTypeIdAndData(this.blockTypes[thisX + pixelOffSetX][thisY].getTypeID(), this.blockTypes[thisX + pixelOffSetX][thisY].getSubID(), false);
            }
        }

        // LEFT ARM - TOP
        pixelOffSetX = 47;
        pixelOffSetY = 12;
        for (int thisX = 1; thisX < 4; thisX++) {
            for (int thisY = 1; thisY < 3; thisY++) {
                world.getBlockAt(x + thisX + 11, y + 11, z + thisY).setTypeIdAndData(this.blockTypes[pixelOffSetX - thisX][thisY + pixelOffSetY].getTypeID(), this.blockTypes[pixelOffSetX - thisX][thisY + pixelOffSetY].getSubID(), false);
            }
        }

        // LEFT ARM - BOTTOM
        pixelOffSetX = 52;
        pixelOffSetY = 12;
        for (int thisX = 1; thisX < 4; thisX++) {
            for (int thisY = 1; thisY < 3; thisY++) {
                world.getBlockAt(x + thisX + 11, y, z + thisY).setTypeIdAndData(this.blockTypes[pixelOffSetX - thisX][thisY + pixelOffSetY].getTypeID(), this.blockTypes[pixelOffSetX - thisX][thisY + pixelOffSetY].getSubID(), false);
            }
        }
    }

    private void buildChest(World world, int x, int y, int z) {
        // FRONT
        int pixelOffSetX = 27;
        for (int thisX = 0; thisX < 8; thisX++) {
            for (int thisY = 0; thisY < 12; thisY++) {
                world.getBlockAt(x + thisX, y + thisY, z).setTypeIdAndData(this.blockTypes[pixelOffSetX - thisX][thisY].getTypeID(), this.blockTypes[pixelOffSetX - thisX][thisY].getSubID(), false);
            }
        }

        // BACK
        pixelOffSetX = 32;
        for (int thisX = 0; thisX < 8; thisX++) {
            for (int thisY = 0; thisY < 12; thisY++) {
                world.getBlockAt(x + thisX, y + thisY, z + 3).setTypeIdAndData(this.blockTypes[thisX + pixelOffSetX][thisY].getTypeID(), this.blockTypes[thisX + pixelOffSetX][thisY].getSubID(), false);
            }
        }
    }

    private void buildLegs(World world, int x, int y, int z) {
        // FRONT
        int pixelOffSetX = 4;
        int offSetX = 4;
        for (int thisX = 0; thisX < 4; thisX++) {
            for (int thisY = 0; thisY < 12; thisY++) {
                world.getBlockAt(x + thisX, y + thisY, z).setTypeIdAndData(this.blockTypes[thisX + pixelOffSetX][thisY].getTypeID(), this.blockTypes[thisX + pixelOffSetX][thisY].getSubID(), false);
                world.getBlockAt(x + (offSetX - 1) + (offSetX - thisX), y + thisY, z).setTypeIdAndData(this.blockTypes[thisX + pixelOffSetX][thisY].getTypeID(), this.blockTypes[thisX + pixelOffSetX][thisY].getSubID(), false);
            }
        }

        // BACK
        pixelOffSetX = 15;
        for (int thisX = 0; thisX < 4; thisX++) {
            for (int thisY = 0; thisY < 12; thisY++) {
                world.getBlockAt(x + thisX, y + thisY, z + 3).setTypeIdAndData(this.blockTypes[pixelOffSetX - thisX][thisY].getTypeID(), this.blockTypes[pixelOffSetX - thisX][thisY].getSubID(), false);
                world.getBlockAt(x + (offSetX - 1) + (offSetX - thisX), y + thisY, z + 3).setTypeIdAndData(this.blockTypes[pixelOffSetX - thisX][thisY].getTypeID(), this.blockTypes[pixelOffSetX - thisX][thisY].getSubID(), false);
            }
        }

        // SIDE (WHEN STANDING IN FRONT)
        pixelOffSetX = 3;
        for (int thisX = 1; thisX < 3; thisX++) {
            for (int thisY = 0; thisY < 12; thisY++) {
                // RIGHT SIDE
                world.getBlockAt(x, y + thisY, z + thisX).setTypeIdAndData(this.blockTypes[pixelOffSetX - thisX][thisY].getTypeID(), this.blockTypes[pixelOffSetX - thisX][thisY].getSubID(), false);
                // LEFT SIDE
                world.getBlockAt(x + 7, y + thisY, z + thisX).setTypeIdAndData(this.blockTypes[pixelOffSetX - thisX][thisY].getTypeID(), this.blockTypes[pixelOffSetX - thisX][thisY].getSubID(), false);
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
