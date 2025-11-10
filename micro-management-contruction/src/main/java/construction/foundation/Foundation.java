package construction.foundation;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

import java.sql.Date;

import construction.components.used_material.Material;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
@Table(name = "users")
public class Foundation extends PanacheEntityBase {
    @Id
    private String id;

    @Column(name = "first_name", nullable = false)
    private String name;

    @Column(name = "last_name", nullable = false)
    private Date date;

    @Email(message = "Invalid email format")
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private int percentageCompleted;

    @Column(nullable = false)
    private Material usedMaterials;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    
}