package construction.components.used_material;

public enum MaterialCategory {
    CEMENT("Cimento"),
    SAND("Areia"),
    STEEL("Ferro"),
    BRICK("Tijolo"),
    WOOD("Madeira"),
    ELECTRICAL("Elétrica"),
    PLUMBING("Hidráulica"),
    OTHER("Outros");

    private final String displayName;

    MaterialCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    // Permite buscar pelo nome em português (ex: "Cimento")
    public static MaterialCategory fromDisplayName(String name) {
        for (MaterialCategory cat : values()) {
            if (cat.displayName.equalsIgnoreCase(name)) {
                return cat;
            }
        }
        return OTHER;
    }
}