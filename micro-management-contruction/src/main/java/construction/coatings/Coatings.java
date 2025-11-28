package construction.coatings;

import construction.coatings.entity_external.CoatingsExecutedService;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "coatings")
public class Coatings extends PanacheEntityBase {
    
    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String contractor;

    // Relacionamento com os serviços executados
    @OneToMany(mappedBy = "coatings", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CoatingsExecutedService> executedServices;

    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContractor() { return contractor; }
    public void setContractor(String contractor) { this.contractor = contractor; }

    public List<CoatingsExecutedService> getExecutedServices() { return executedServices; }
    public void setExecutedServices(List<CoatingsExecutedService> executedServices) { this.executedServices = executedServices; }
}