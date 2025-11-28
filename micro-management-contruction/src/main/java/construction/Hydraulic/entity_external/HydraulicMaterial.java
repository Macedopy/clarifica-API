package construction.hydraulic.entity_external;

import construction.components.used_material.MaterialCategory;
import construction.components.used_material.MaterialUnit;
import construction.hydraulic.Hydraulic;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import com.fasterxml.jackson.annotation.JsonIgnore; // novo

@Entity
@Table(name = "hydraulic_materials")
public class HydraulicMaterial extends PanacheEntityBase {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hydraulic_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore // ESTA LINHA IMPEDE O LOOP DE SERIALIZAÇÃO
    private Hydraulic hydraulic;

    @Transient
    private String phaseId;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MaterialCategory category;

    @Column(name = "consumed_quantity", nullable = false)
    @Min(value = 0, message = "Consumed quantity cannot be negative")
    private double consumedQuantity = 0.0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MaterialUnit unit;

    @Column(name = "current_stock", nullable = false)
    @Min(value = 0, message = "Current stock cannot be negative")
    private double currentStock = 0.0;

    @Column(name = "minimum_stock", nullable = false)
    @Min(value = 0, message = "Minimum stock cannot be negative")
    private double minimumStock = 10.0;

    @Column(name = "needs_restock", nullable = false)
    private boolean needsRestock = false;

    @Column(name = "urgency")
    private String urgency;

    // Atualiza automaticamente o status de reposição
    @PostLoad
    @PostPersist
    @PostUpdate
    public void updateRestockStatus() {
        this.needsRestock = this.currentStock <= this.minimumStock;
    }

    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Hydraulic getHydraulic() { return hydraulic; }
    public void setHydraulic(Hydraulic hydraulic) { this.hydraulic = hydraulic; }

    public String getPhaseId() { return phaseId; }
    public void setPhaseId(String phaseId) { this.phaseId = phaseId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public MaterialCategory getCategory() { return category; }
    public void setCategory(MaterialCategory category) { this.category = category; }

    public double getConsumedQuantity() { return consumedQuantity; }
    public void setConsumedQuantity(double consumedQuantity) { this.consumedQuantity = consumedQuantity; }

    public MaterialUnit getUnit() { return unit; }
    public void setUnit(MaterialUnit unit) { this.unit = unit; }

    public double getCurrentStock() { return currentStock; }
    public void setCurrentStock(double currentStock) { this.currentStock = currentStock; }

    public double getMinimumStock() { return minimumStock; }
    public void setMinimumStock(double minimumStock) { this.minimumStock = minimumStock; }

    public boolean isNeedsRestock() { return needsRestock; }
    public void setNeedsRestock(boolean needsRestock) { this.needsRestock = needsRestock; }

    public String getUrgency() { return urgency; }
    public void setUrgency(String urgency) { this.urgency = urgency; }
}
