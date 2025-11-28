package construction.structure;

import java.util.Optional;

import construction.structure.entity_external.StructureExecutedServiceService;
import construction.structure.entity_external.StructureMachineryService;
import construction.structure.entity_external.StructureMaterialService;
import construction.structure.entity_external.StructurePhotoRecordService;
import construction.structure.entity_external.StructureTeamMemberService;
import construction.structure.entity_external.StructureToolService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class StructureService {

    @Inject StructureRepository structureRepository;
    @Inject StructureMaterialService structureMaterialService;
    @Inject StructureToolService structureToolService;
    @Inject StructureMachineryService structureMachineryService;
    @Inject StructureTeamMemberService structureTeamMemberService;
    @Inject StructureExecutedServiceService structureExecutedServiceService;
    @Inject StructurePhotoRecordService structurePhotoRecordService;

    @Transactional
    public void saveStructure(Structure structure){
        structureRepository.persist(structure);
    }

    @Transactional
    public void updateStructure(String id, Structure updatedStructure){
        Structure entity = structureRepository.findByIdOptional(id)
            .orElseThrow(() -> new NotFoundException("Fase Structure não encontrada com ID: " + id));
        
        entity.setName(updatedStructure.getName());
        entity.setContractor(updatedStructure.getContractor());
        
        structureRepository.persist(entity);
    }

    @Transactional
    public void saveAllPhaseDetails(String phaseId, StructureDTO detailsDTO){
        try{
            System.out.println("\n--- INICIANDO saveAllPhaseDetails para phaseId: " + phaseId);
            if (detailsDTO.getEquipe() != null && !detailsDTO.getEquipe().isEmpty()){
                System.out.println("  → Salvando " + detailsDTO.getEquipe().size() + " membros da equipe...");
                structureTeamMemberService.saveAll(detailsDTO.getEquipe(), phaseId);
                System.out.println("  ✓ Equipe salva!");
            }else{
                System.out.println("  ⊘ Equipe vazia ou null");
            }

            if (detailsDTO.getServicos() != null && !detailsDTO.getServicos().isEmpty()){
                System.out.println("  → Salvando " + detailsDTO.getServicos().size() + " serviços executados...");
                structureExecutedServiceService.saveAll(detailsDTO.getServicos(), phaseId);
                System.out.println("  ✓ Serviços salvos!");
            }

            if (detailsDTO.getMaquinarios() != null && !detailsDTO.getMaquinarios().isEmpty()){
                System.out.println("  → Salvando " + detailsDTO.getMaquinarios().size() + " maquinários...");
                structureMachineryService.saveAll(detailsDTO.getMaquinarios(), phaseId);
                System.out.println("  ✓ Maquinários salvos!");
            }

            if (detailsDTO.getMateriais() != null && !detailsDTO.getMateriais().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getMateriais().size() + " materiais...");
                structureMaterialService.saveAll(detailsDTO.getMateriais(), phaseId);
                System.out.println("  ✓ Materiais salvos!");
            }

            if (detailsDTO.getFerramentas() != null && !detailsDTO.getFerramentas().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getFerramentas().size() + " ferramentas...");
                structureToolService.saveAll(detailsDTO.getFerramentas(), phaseId);
                System.out.println("  ✓ Ferramentas salvas!");
            }

            if (detailsDTO.getFotos() != null && !detailsDTO.getFotos().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getFotos().size() + " fotos...");
                structurePhotoRecordService.saveAll(detailsDTO.getFotos(), phaseId);
                System.out.println("  ✓ Fotos salvas!");
            }

            System.out.println("--- FIM saveAllPhaseDetails\n");
        }catch (Exception e){
            System.err.println("ERRO em saveAllPhaseDetails: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }   
    }

    public Optional<Structure> getStructureById(String id){
        return structureRepository.findByIdOptional(id);
    }
}
