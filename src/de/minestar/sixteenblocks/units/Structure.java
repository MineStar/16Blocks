package de.minestar.sixteenblocks.units;

import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.World;

public class Structure {
    private HashSet<StructureBlock> BlockSet = new HashSet<StructureBlock>();

    public void createStructure(World world, int baseX, int baseY, int baseZ) {
        Iterator<StructureBlock> iterator = BlockSet.iterator();
        StructureBlock thisBlock;
        while (iterator.hasNext()) {
            thisBlock = iterator.next();
            world.getBlockAt(baseX + thisBlock.getX(), baseY + thisBlock.getY(), baseZ + thisBlock.getZ()).setTypeIdAndData(thisBlock.getTypeID(), thisBlock.getSubID(), false);
        }
        thisBlock = null;
        iterator = null;
    }

    protected void addBlock(int x, int y, int z, int TypeID) {
        this.addBlock(new StructureBlock(x, y, z, TypeID));
    }

    protected void addBlock(int x, int y, int z, int TypeID, byte SubID) {
        this.addBlock(new StructureBlock(x, y, z, TypeID, SubID));
    }

    protected void addBlock(StructureBlock block) {
        this.BlockSet.add(block);
    }
}
