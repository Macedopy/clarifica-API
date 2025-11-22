package construction.components.photo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PhotoCategory {
    PROGRESS("progresso"),
    ISSUE("problema"),
    BEFORE("antes"),
    AFTER("depois"),
    SAFETY("segurança"),
    // Adicione estas linhas abaixo:
    DIARIO_OBRA("diario_obra"), 
    OUTROS("outros");

    private final String displayName;

    PhotoCategory(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static PhotoCategory fromString(String value) {
        if (value == null) return PROGRESS; // Padrão seguro se for nulo
        
        for (PhotoCategory c : values()) {
            // Verifica tanto o nome da constante (DIARIO_OBRA) quanto o displayName (diario_obra)
            if (c.name().equalsIgnoreCase(value) || c.displayName.equalsIgnoreCase(value)) {
                return c;
            }
        }
        // Se não encontrar, retorna OUTROS ou PROGRESS para não travar o sistema
        return OUTROS; 
    }
}
