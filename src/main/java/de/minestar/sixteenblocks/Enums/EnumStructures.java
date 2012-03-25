package de.minestar.sixteenblocks.Enums;

public enum EnumStructures {
    ZONE_STREETS_AND_SOCKET("STREETS_AND_SOCKET"),

    ZONE_STEVE("STEVE"),

    ZONE_STREETS_BACK("STREETS_BACK"),

    STREETS_CORNER("STREETS_CORNER"),

    STREETS_SIDE_1("STREETS_SIDE_1"),

    STREETS_SIDE_2("STREETS_SIDE_2"),

    INFO_WALL_1("INFO_WALL_1"),

    INFO_WALL_2("INFO_WALL_2"),

    NOTCH_1("NOTCH_1"),

    NOTCH_2("NOTCH_2"),

    NOTCH_3("NOTCH_3"),

    NOTCH_4("NOTCH_4");

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
