package de.minestar.sixteenblocks.structures;

import de.minestar.sixteenblocks.units.Structure;

public class StructureSocket extends Structure {
    public StructureSocket() {
        this.addLayer1(0);
        this.addLayer2(1);
    }

    private void addLayer1(int y) {
        // BASE - BOTTOM
        this.addBlock(10, y, 12, 43, (byte) 6);
        this.addBlock(11, y, 12, 98);
        this.addBlock(12, y, 12, 43, (byte) 6);
        this.addBlock(13, y, 12, 43, (byte) 6);
        this.addBlock(14, y, 12, 98);
        this.addBlock(15, y, 12, 43, (byte) 6);
        this.addBlock(16, y, 12, 43, (byte) 6);
        this.addBlock(17, y, 12, 98);
        this.addBlock(18, y, 12, 43, (byte) 6);
        this.addBlock(19, y, 12, 43, (byte) 6);
        this.addBlock(20, y, 12, 98);
        this.addBlock(21, y, 12, 43, (byte) 6);

        // BASE - TOP
        this.addBlock(10, y, 19, 43, (byte) 6);
        this.addBlock(11, y, 19, 98);
        this.addBlock(12, y, 19, 43, (byte) 6);
        this.addBlock(13, y, 19, 43, (byte) 6);
        this.addBlock(14, y, 19, 98);
        this.addBlock(15, y, 19, 43, (byte) 6);
        this.addBlock(16, y, 19, 43, (byte) 6);
        this.addBlock(17, y, 19, 98);
        this.addBlock(18, y, 19, 43, (byte) 6);
        this.addBlock(19, y, 19, 43, (byte) 6);
        this.addBlock(20, y, 19, 98);
        this.addBlock(21, y, 19, 43, (byte) 6);

        // BASE - RIGHT
        this.addBlock(10, y, 13, 98);
        this.addBlock(10, y, 14, 43, (byte) 6);
        this.addBlock(10, y, 15, 98);
        this.addBlock(10, y, 16, 98);
        this.addBlock(10, y, 17, 43, (byte) 6);
        this.addBlock(10, y, 18, 98);

        // BASE - LEFT
        this.addBlock(21, y, 13, 98);
        this.addBlock(21, y, 14, 43, (byte) 6);
        this.addBlock(21, y, 15, 98);
        this.addBlock(21, y, 16, 98);
        this.addBlock(21, y, 17, 43, (byte) 6);
        this.addBlock(21, y, 18, 98);
    }

    private void addLayer2(int y) {
        // OVERLAY- BOTTOM
        this.addBlock(11, y, 13, 43, (byte) 6);
        this.addBlock(12, y, 13, 98);
        this.addBlock(13, y, 13, 98);
        this.addBlock(14, y, 13, 43, (byte) 6);
        this.addBlock(15, y, 13, 98);
        this.addBlock(16, y, 13, 98);
        this.addBlock(17, y, 13, 43, (byte) 6);
        this.addBlock(18, y, 13, 98);
        this.addBlock(19, y, 13, 98);
        this.addBlock(20, y, 13, 43, (byte) 6);

        // OVERLAY - TOP
        this.addBlock(11, y, 18, 43, (byte) 6);
        this.addBlock(12, y, 18, 98);
        this.addBlock(13, y, 18, 98);
        this.addBlock(14, y, 18, 43, (byte) 6);
        this.addBlock(15, y, 18, 98);
        this.addBlock(16, y, 18, 98);
        this.addBlock(17, y, 18, 43, (byte) 6);
        this.addBlock(18, y, 18, 98);
        this.addBlock(19, y, 18, 98);
        this.addBlock(20, y, 18, 43, (byte) 6);

        // OVERLAY - RIGHT
        this.addBlock(11, y, 14, 98);
        this.addBlock(11, y, 15, 43, (byte) 6);
        this.addBlock(11, y, 16, 43, (byte) 6);
        this.addBlock(11, y, 17, 98);

        // OVERLAY - LEFT
        this.addBlock(20, y, 14, 98);
        this.addBlock(20, y, 15, 43, (byte) 6);
        this.addBlock(20, y, 16, 43, (byte) 6);
        this.addBlock(20, y, 17, 98);

        // OVERLAY - MIDDLE
        this.addBlock(12, y, 14, 1);
        this.addBlock(13, y, 14, 1);
        this.addBlock(14, y, 14, 1);
        this.addBlock(15, y, 14, 1);
        this.addBlock(16, y, 14, 1);
        this.addBlock(17, y, 14, 1);
        this.addBlock(18, y, 14, 1);
        this.addBlock(19, y, 14, 1);

        this.addBlock(12, y, 15, 1);
        this.addBlock(13, y, 15, 1);
        this.addBlock(14, y, 15, 1);
        this.addBlock(15, y, 15, 1);
        this.addBlock(16, y, 15, 1);
        this.addBlock(17, y, 15, 1);
        this.addBlock(18, y, 15, 1);
        this.addBlock(19, y, 15, 1);

        this.addBlock(12, y, 16, 1);
        this.addBlock(13, y, 16, 1);
        this.addBlock(14, y, 16, 1);
        this.addBlock(15, y, 16, 1);
        this.addBlock(16, y, 16, 1);
        this.addBlock(17, y, 16, 1);
        this.addBlock(18, y, 16, 1);
        this.addBlock(19, y, 16, 1);

        this.addBlock(12, y, 17, 1);
        this.addBlock(13, y, 17, 1);
        this.addBlock(14, y, 17, 1);
        this.addBlock(15, y, 17, 1);
        this.addBlock(16, y, 17, 1);
        this.addBlock(17, y, 17, 1);
        this.addBlock(18, y, 17, 1);
        this.addBlock(19, y, 17, 1);
    }
}
