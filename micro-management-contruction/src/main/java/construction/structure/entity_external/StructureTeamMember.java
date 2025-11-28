package construction.structure.entity_external;

import com.fasterxml.jackson.annotation.JsonIgnore;

import construction.components.team_present.MemberStatus;
import construction.components.team_present.TeamMemberDetails;
import construction.structure.Structure;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Entity
@Table(name = "structure_team_members")
public class StructureTeamMember extends PanacheEntityBase {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "structure_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private Structure structure;

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

    public StructureTeamMember() {}
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Structure getStructure() { return structure; }
    public void setStructure(Structure structure) { this.structure = structure; }
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