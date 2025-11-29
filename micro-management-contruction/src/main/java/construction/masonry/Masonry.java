package construction.masonry;

import construction.masonry.entity_external.MasonryExecutedService;
import construction.masonry.entity_external.MasonryMachinery;
import construction.masonry.entity_external.MasonryMaterial;
import construction.masonry.entity_external.MasonryPhotoRecord;
import construction.masonry.entity_external.MasonryTeamMember;
import construction.masonry.entity_external.MasonryTool;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "masonry")
public class Masonry extends PanacheEntityBase {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(name = "contractor")
    private String contractor;

    @OneToMany(mappedBy = "masonry", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<MasonryMaterial> materials = new ArrayList<>();

    @OneToMany(mappedBy = "masonry", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<MasonryTool> tools = new ArrayList<>();

    @OneToMany(mappedBy = "masonry", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<MasonryMachinery> machinery = new ArrayList<>();

    @OneToMany(mappedBy = "masonry", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<MasonryTeamMember> teamMembers = new ArrayList<>();

    @OneToMany(mappedBy = "masonry", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<MasonryExecutedService> services = new ArrayList<>();

    @OneToMany(mappedBy = "masonry", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<MasonryPhotoRecord> photoRecords = new ArrayList<>();

    public Masonry() {
        // Garante que o ID seja gerado se não existir, igual à Structure
        this.id = UUID.randomUUID().toString();
    }

    // Getters e Setters

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

    public List<MasonryMaterial> getMaterials() {
        return materials;
    }

    public void setMaterials(List<MasonryMaterial> materials) {
        this.materials = materials;
    }

    public List<MasonryTool> getTools() {
        return tools;
    }

    public void setTools(List<MasonryTool> tools) {
        this.tools = tools;
    }

    public List<MasonryMachinery> getMachinery() {
        return machinery;
    }

    public void setMachinery(List<MasonryMachinery> machinery) {
        this.machinery = machinery;
    }

    public List<MasonryTeamMember> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(List<MasonryTeamMember> teamMembers) {
        this.teamMembers = teamMembers;
    }

    public List<MasonryExecutedService> getServices() {
        return services;
    }

    public void setServices(List<MasonryExecutedService> services) {
        this.services = services;
    }

    public List<MasonryPhotoRecord> getPhotoRecords() {
        return photoRecords;
    }

    public void setPhotoRecords(List<MasonryPhotoRecord> photoRecords) {
        this.photoRecords = photoRecords;
    }
}