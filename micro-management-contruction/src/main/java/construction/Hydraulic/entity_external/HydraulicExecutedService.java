package construction.hydraulic.entity_external;

import construction.components.executed_services.ExecutedServiceStatus;
import construction.hydraulic.Hydraulic;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import com.fasterxml.jackson.annotation.JsonIgnore; // novo

@Entity
@Table(name = "hydraulic_executed_services")
public class HydraulicExecutedService extends PanacheEntityBase {

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

    @Column(nullable = false)
    private String team;

    @Min(1)
    @Column(name = "planned_hours", nullable = false)
    private double plannedHours;

    @Min(0)
    @Column(name = "executed_hours", nullable = false)
    private double executedHours = 0.0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExecutedServiceStatus status;

    @Min(0)
    @Column(nullable = false)
    private int progress = 0;

    @Column(length = 1000)
    private String notes;

    @PostLoad
    @PostPersist
    @PostUpdate
    private void updateProgress() {
        if (plannedHours > 0) {
            this.progress = (int) Math.round((executedHours / plannedHours) * 100);
            this.progress = Math.min(100, Math.max(0, this.progress));
        } else {
            this.progress = 0;
        }
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

    public String getTeam() { return team; }
    public void setTeam(String team) { this.team = team; }

    public double getPlannedHours() { return plannedHours; }
    public void setPlannedHours(double plannedHours) { this.plannedHours = plannedHours; updateProgress(); }

    public double getExecutedHours() { return executedHours; }
    public void setExecutedHours(double executedHours) { this.executedHours = executedHours; updateProgress(); }

    public ExecutedServiceStatus getStatus() { return status; }
    public void setStatus(ExecutedServiceStatus status) { this.status = status; }

    public int getProgress() { return progress; }
    public void setProgress(int progress) { this.progress = progress; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
