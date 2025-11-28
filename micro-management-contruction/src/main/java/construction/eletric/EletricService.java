package construction.eletric;

import java.util.Optional;

import construction.eletric.entity_external.EletricExecutedServiceService;
import construction.eletric.entity_external.EletricMachineryService;
import construction.eletric.entity_external.EletricMaterialService;
import construction.eletric.entity_external.EletricPhotoRecordService;
import construction.eletric.entity_external.EletricTeamMemberService;
import construction.eletric.entity_external.EletricToolService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class EletricService {
    
    @Inject EletricRepository eletricRepository;    
    @Inject EletricMaterialService eletricMaterialService;
    @Inject EletricToolService eletricToolService; 
    @Inject EletricMachineryService eletricMachineryService;
    @Inject EletricTeamMemberService eletricTeamMemberService;
    @Inject EletricExecutedServiceService eletricExecutedServiceService;
    @Inject EletricPhotoRecordService eletricPhotoRecordService;
    
    @Transactional
    public void saveEletric(Eletric eletric) {
        eletricRepository.persist(eletric);
    }
    
    @Transactional
    public void updateEletric(String id, Eletric updatedEletric) {
        Eletric entity = eletricRepository.findByIdOptional(id)
            .orElseThrow(() -> new NotFoundException("Fase Elétrica não encontrada com ID: " + id));
            
        entity.setName(updatedEletric.getName());
        entity.setContractor(updatedEletric.getContractor());
        
        eletricRepository.persist(entity);
    }

    @Transactional
    public void saveAllPhaseDetails(String phaseId, EletricDTO detailsDTO) {
        try {
            System.out.println("\n--- INICIANDO saveAllPhaseDetails (Eletric) para phaseId: " + phaseId);
            
            if (detailsDTO.getEquipe() != null && !detailsDTO.getEquipe().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getEquipe().size() + " membros da equipe...");
                eletricTeamMemberService.saveAll(detailsDTO.getEquipe(), phaseId);
                System.out.println("  ✓ Equipe salva!");
            }

            if (detailsDTO.getServicos() != null && !detailsDTO.getServicos().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getServicos().size() + " serviços executados...");
                eletricExecutedServiceService.saveAll(detailsDTO.getServicos(), phaseId);
                System.out.println("  ✓ Serviços salvos!");
            }

            if (detailsDTO.getMaquinarios() != null && !detailsDTO.getMaquinarios().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getMaquinarios().size() + " maquinários...");
                eletricMachineryService.saveAll(detailsDTO.getMaquinarios(), phaseId);
                System.out.println("  ✓ Maquinários salvos!");
            }

            if (detailsDTO.getMateriais() != null && !detailsDTO.getMateriais().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getMateriais().size() + " materiais...");
                eletricMaterialService.saveAll(detailsDTO.getMateriais(), phaseId);
                System.out.println("  ✓ Materiais salvos!");
            }

            if (detailsDTO.getFerramentas() != null && !detailsDTO.getFerramentas().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getFerramentas().size() + " ferramentas...");
                eletricToolService.saveAll(detailsDTO.getFerramentas(), phaseId);
                System.out.println("  ✓ Ferramentas salvas!");
            }
            
            if (detailsDTO.getFotos() != null && !detailsDTO.getFotos().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getFotos().size() + " fotos...");
                eletricPhotoRecordService.saveAll(detailsDTO.getFotos(), phaseId);
                System.out.println("  ✓ Fotos salvas!");
            }
            
            System.out.println("--- FIM saveAllPhaseDetails (Eletric)\n");
        } catch (Exception e) {
            System.err.println("ERRO em saveAllPhaseDetails (Eletric): " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public Optional<Eletric> getEletricById(String id) {
        return eletricRepository.findByIdOptional(id);
    }
}