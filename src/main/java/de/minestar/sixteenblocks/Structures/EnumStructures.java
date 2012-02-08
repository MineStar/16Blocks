package de.minestar.sixteenblocks.Structures;

public enum EnumStructures {
    ZONE_STREETS_AND_SOCKET("STREETS_AND_SOCKET"),

    ZONE_STEVE("STEVE"),

    ZONE_STREETS_BACK("STREETS_BACK");

    private final String name;

    private EnumStructures(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public EnumStructures fromName(String name) {
        for (EnumStructures thisStructure : EnumStructures.values()) {
            if (thisStructure.getName().contains(name))
                return thisStructure;
        }
        return null;
    }
}
