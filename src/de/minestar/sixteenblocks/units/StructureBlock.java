package de.minestar.sixteenblocks.units;

public class StructureBlock {
    private final int x, y, z, TypeID;
    private final byte SubID;

    public StructureBlock(int x, int y, int z, int TypeID, byte SubID) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.TypeID = TypeID;
        this.SubID = SubID;
    }

    public StructureBlock(int x, int y, int z, int TypeID) {
        this(x, y, z, TypeID, (byte) 0);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public int getTypeID() {
        return TypeID;
    }

    public byte getSubID() {
        return SubID;
    }
}
