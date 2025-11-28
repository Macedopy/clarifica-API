package construction.hydraulic.entity_external;

import construction.components.photo.PhotoCategory;
import construction.hydraulic.Hydraulic;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore; // novo

@Entity
@Table(name = "hydraulic_photo_records")
public class HydraulicPhotoRecord extends PanacheEntityBase {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hydraulic_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore // ESTA LINHA IMPEDE O LOOP DE SERIALIZAÇÃO
    private Hydraulic hydraulic;

    @Transient
    private String phaseId;

    @Column(name = "file_path", nullable = false, length = 1000)
    private String filePath;

    @Column(length = 500)
    private String caption;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PhotoCategory category;

    @Column(name = "uploaded_at", nullable = false)
    private java.time.LocalDateTime uploadedAt;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Hydraulic getHydraulic() { return hydraulic; }
    public void setHydraulic(Hydraulic hydraulic) { this.hydraulic = hydraulic; }

    public String getPhaseId() { return phaseId; }
    public void setPhaseId(String phaseId) { this.phaseId = phaseId; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getCaption() { return caption; }
    public void setCaption(String caption) { this.caption = caption; }

    public PhotoCategory getCategory() { return category; }
    public void setCategory(PhotoCategory category) { this.category = category; }

    public java.time.LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(java.time.LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }
}
