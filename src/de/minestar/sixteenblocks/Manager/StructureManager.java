package de.minestar.sixteenblocks.Manager;

import java.util.HashMap;

import de.minestar.sixteenblocks.structures.EnumStructures;
import de.minestar.sixteenblocks.units.Structure;

public class StructureManager {
    private HashMap<EnumStructures, Structure> structureList = new HashMap<EnumStructures, Structure>();
    private AreaManager areaManager;

    public StructureManager(AreaManager areaManager) {
        this.areaManager = areaManager;
        this.initStructures();
    }

    public Structure getStructure(EnumStructures structure) {
        return structureList.get(structure);
    }

    private void initStructures() {
        this.addStructure(EnumStructures.ZONE_STREETS_AND_SOCKET);
        this.addStructure(EnumStructures.ZONE_STREETS_BACK);
        this.addStructure(EnumStructures.ZONE_STEVE);
    }

    private void addStructure(EnumStructures structure) {
        this.structureList.put(structure, new Structure(this.areaManager, structure.getName()));
    }
}
