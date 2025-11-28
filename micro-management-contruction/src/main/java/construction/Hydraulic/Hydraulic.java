package construction.hydraulic;

import construction.hydraulic.entity_external.HydraulicExecutedService;
import construction.hydraulic.entity_external.HydraulicMachinery;
import construction.hydraulic.entity_external.HydraulicMaterial;
import construction.hydraulic.entity_external.HydraulicPhotoRecord;
import construction.hydraulic.entity_external.HydraulicTeamMember;
import construction.hydraulic.entity_external.HydraulicTool;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "hydraulics")
public class Hydraulic extends PanacheEntityBase {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(name = "contractor") // Usando name = "contractor" como na classe Structure
    private String contractor;

    // Relacionamentos ajustados para serem IGUAIS à classe Structure:
    // 1. Descomentados.
    // 2. Inicializados com `new ArrayList<>()`.
    // 3. Usando `FetchType.EAGER` (carregamento imediato dos dados relacionados).
    // 4. Removido `orphanRemoval = true` (para total conformidade com a Structure, que não o usa).

    @OneToMany(mappedBy = "hydraulic", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<HydraulicMaterial> materials = new ArrayList<>();

    @OneToMany(mappedBy = "hydraulic", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<HydraulicTool> tools = new ArrayList<>();

    @OneToMany(mappedBy = "hydraulic", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<HydraulicMachinery> machineries = new ArrayList<>(); // Note: Em Structure é 'machinery' (singular)

    @OneToMany(mappedBy = "hydraulic", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<HydraulicTeamMember> teamMembers = new ArrayList<>();

    @OneToMany(mappedBy = "hydraulic", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<HydraulicExecutedService> executedServices = new ArrayList<>(); // Note: Em Structure é 'services'

    @OneToMany(mappedBy = "hydraulic", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<HydraulicPhotoRecord> photoRecords = new ArrayList<>();

    /**
     * Construtor para garantir a geração de ID, seguindo a lógica da classe Structure.
     */
    public Hydraulic() {
        // Garante que o ID seja gerado se não existir
        this.id = UUID.randomUUID().toString();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContractor() {
        return contractor;
    }

    public void setContractor(String contractor) {
        this.contractor = contractor;
    }

    public List<HydraulicMaterial> getMaterials() {
        return materials;
    }

    public void setMaterials(List<HydraulicMaterial> materials) {
        this.materials = materials;
    }

    public List<HydraulicTool> getTools() {
        return tools;
    }

    public void setTools(List<HydraulicTool> tools) {
        this.tools = tools;
    }

    public List<HydraulicMachinery> getMachineries() {
        return machineries;
    }

    public void setMachineries(List<HydraulicMachinery> machineries) {
        this.machineries = machineries;
    }

    public List<HydraulicTeamMember> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(List<HydraulicTeamMember> teamMembers) {
        this.teamMembers = teamMembers;
    }

    public List<HydraulicExecutedService> getExecutedServices() {
        return executedServices;
    }

    public void setExecutedServices(List<HydraulicExecutedService> executedServices) {
        this.executedServices = executedServices;
    }

    public List<HydraulicPhotoRecord> getPhotoRecords() {
        return photoRecords;
    }

    public void setPhotoRecords(List<HydraulicPhotoRecord> photoRecords) {
        this.photoRecords = photoRecords;
    }
}