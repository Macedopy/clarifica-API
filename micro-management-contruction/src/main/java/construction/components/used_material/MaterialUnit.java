package construction.components.used_material;

public enum MaterialUnit {
    BAG("saco"),
    KG("kg"),
    CUBIC_METER("m³"),
    UNIT("unidade"),
    LITER("litro");

    private final String symbol;

    MaterialUnit(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public static MaterialUnit fromSymbol(String symbol) {
        for (MaterialUnit unit : values()) {
            if (unit.symbol.equalsIgnoreCase(symbol)) {
                return unit;
            }
        }
        return UNIT; // fallback
    }
}
