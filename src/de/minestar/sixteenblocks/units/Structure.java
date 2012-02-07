package de.minestar.sixteenblocks.units;

import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.World;

import de.minestar.sixteenblocks.Manager.AreaManager;
import de.minestar.sixteenblocks.core.TextUtils;

public class Structure {
    private HashSet<StructureBlock> BlockSet = null;

    public Structure(AreaManager areaManager, String structureName) {
        HashSet<StructureBlock> loadedBlocks = areaManager.loadStructure(structureName);
        if (loadedBlocks != null) {
            this.BlockSet = loadedBlocks;
        } else {
            TextUtils.logWarning("Structure '" + structureName + "' could not be initialized!");
        }
    }

    public void createStructure(World world, int baseX, int baseY, int baseZ) {
        if (this.BlockSet == null)
            return;
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
