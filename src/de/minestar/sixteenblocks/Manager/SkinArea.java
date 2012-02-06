package de.minestar.sixteenblocks.Manager;

public class SkinArea {
    private static int areaSizeX = 32, areaSizeZ = 32;
    private static int minimumY = 5;

    private int areaX = 0, areaZ = 0;
    private int realX = areaX * areaSizeX, realZ = areaZ * areaSizeZ, realEndX = realX + areaSizeX, realEndZ = realZ + areaSizeZ;

    public SkinArea(int areaX, int areaZ) {
        this.areaX = areaX;
        this.areaZ = areaZ;
        this.realX = this.areaX * areaSizeX;
        this.realZ = this.areaZ * areaSizeZ;
        this.realEndX = this.realX + areaSizeX - 1;
        this.realEndZ = this.realZ + areaSizeZ - 1;
    }

    public boolean isInArea(int x, int y, int z) {
        if (y < minimumY || x < this.realX || x > this.realEndX || z < this.realZ || z > this.realEndZ)
            return false;
        return true;
    }
}
