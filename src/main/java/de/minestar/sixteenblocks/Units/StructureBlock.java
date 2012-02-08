package de.minestar.sixteenblocks.Units;

public class StructureBlock {
    private int x, y, z;
    private final int TypeID;
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

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public int getTypeID() {
        return TypeID;
    }

    public byte getSubID() {
        return SubID;
    }
}
