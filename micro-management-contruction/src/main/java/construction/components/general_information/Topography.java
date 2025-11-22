package construction.components.general_information;

public enum Topography {
    FLAT("plana"),
    SLOPED("inclinada"),
    HILLY("colinoso"),
    MOUNTAINOUS("montanhoso");

    private final String displayName;

    Topography(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Topography fromDisplayName(String name) {
        for (Topography t : values()) {
            if (t.displayName.equalsIgnoreCase(name)) {
                return t;
            }
        }
        return FLAT;
    }
}
