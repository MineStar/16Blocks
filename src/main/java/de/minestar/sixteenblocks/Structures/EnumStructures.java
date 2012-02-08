package de.minestar.sixteenblocks.Structures;

public enum EnumStructures {
    ZONE_STREETS_AND_SOCKET("STREETS_AND_SOCKET"),

    ZONE_STEVE("STEVE"),

    ZONE_STREETS_BACK("STREETS_BACK"),

    STREETS_CORNER("STREETS_CORNER"),

    STREETS_SIDE_1("STREETS_SIDE_1"),

    STREETS_SIDE_2("STREETS_SIDE_2");

    private final String name;

    private EnumStructures(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static EnumStructures fromName(String name) {
        for (EnumStructures thisStructure : EnumStructures.values()) {
            if (thisStructure.getName().toLowerCase().contains(name.toLowerCase()))
                return thisStructure;
        }
        return null;
    }
}
