package construction.hydraulic.entity_external;

import construction.components.team_present.MemberStatus;
import construction.components.team_present.TeamMemberDetails;
import construction.hydraulic.Hydraulic;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore; // novo

@Entity
@Table(name = "hydraulic_team_members")
public class HydraulicTeamMember extends PanacheEntityBase {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hydraulic_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore // ESTA LINHA IMPEDE O LOOP DE SERIALIZAÇÃO
    private Hydraulic hydraulic;

    @Transient
    private String phaseId;

    @Embedded
    private TeamMemberDetails details;

    @Column(name = "hours_worked", nullable = false)
    private double hoursWorked = 0.0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberStatus status;

    @Column(length = 500)
    private String notes;

    public HydraulicTeamMember() {}

    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Hydraulic getHydraulic() { return hydraulic; }
    public void setHydraulic(Hydraulic hydraulic) { this.hydraulic = hydraulic; }

    public String getPhaseId() { return phaseId; }
    public void setPhaseId(String phaseId) { this.phaseId = phaseId; }

    public TeamMemberDetails getDetails() { return details; }
    public void setDetails(TeamMemberDetails details) { this.details = details; }

    public double getHoursWorked() { return hoursWorked; }
    public void setHoursWorked(double hoursWorked) { this.hoursWorked = hoursWorked; }

    public MemberStatus getStatus() { return status; }
    public void setStatus(MemberStatus status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
