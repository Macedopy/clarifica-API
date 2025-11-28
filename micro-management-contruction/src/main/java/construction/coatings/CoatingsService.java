package construction.coatings;

import java.util.Optional;

import construction.coatings.entity_external.CoatingsExecutedServiceService;
import construction.coatings.entity_external.CoatingsMachineryService;
import construction.coatings.entity_external.CoatingsMaterialService;
import construction.coatings.entity_external.CoatingsPhotoRecordService;
import construction.coatings.entity_external.CoatingsTeamMemberService;
import construction.coatings.entity_external.CoatingsToolService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class CoatingsService {
    
    @Inject CoatingsRepository coatingsRepository;    
    @Inject CoatingsMaterialService coatingsMaterialService;
    @Inject CoatingsToolService coatingsToolService; 
    @Inject CoatingsMachineryService coatingsMachineryService;
    @Inject CoatingsTeamMemberService coatingsTeamMemberService;
    @Inject CoatingsExecutedServiceService coatingsExecutedServiceService;
    @Inject CoatingsPhotoRecordService coatingsPhotoRecordService;
    
    @Transactional
    public void saveCoatings(Coatings coatings) {
        coatingsRepository.persist(coatings);
    }
    
    @Transactional
    public void updateCoatings(String id, Coatings updatedCoatings) {
        Coatings entity = coatingsRepository.findByIdOptional(id)
            .orElseThrow(() -> new NotFoundException("Fase Coatings não encontrada com ID: " + id));
            
        entity.setName(updatedCoatings.getName());
        entity.setContractor(updatedCoatings.getContractor());
        
        coatingsRepository.persist(entity);
    }

    @Transactional
    public void saveAllPhaseDetails(String phaseId, CoatingsDTO detailsDTO) {
        try {
            System.out.println("\n--- INICIANDO saveAllPhaseDetails (Coatings) para phaseId: " + phaseId);
            
            if (detailsDTO.getEquipe() != null && !detailsDTO.getEquipe().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getEquipe().size() + " membros da equipe...");
                coatingsTeamMemberService.saveAll(detailsDTO.getEquipe(), phaseId);
                System.out.println("  ✓ Equipe salva!");
            }

            if (detailsDTO.getServicos() != null && !detailsDTO.getServicos().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getServicos().size() + " serviços executados...");
                coatingsExecutedServiceService.saveAll(detailsDTO.getServicos(), phaseId);
                System.out.println("  ✓ Serviços salvos!");
            }

            if (detailsDTO.getMaquinarios() != null && !detailsDTO.getMaquinarios().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getMaquinarios().size() + " maquinários...");
                coatingsMachineryService.saveAll(detailsDTO.getMaquinarios(), phaseId);
                System.out.println("  ✓ Maquinários salvos!");
            }

            if (detailsDTO.getMateriais() != null && !detailsDTO.getMateriais().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getMateriais().size() + " materiais...");
                coatingsMaterialService.saveAll(detailsDTO.getMateriais(), phaseId);
                System.out.println("  ✓ Materiais salvos!");
            }

            if (detailsDTO.getFerramentas() != null && !detailsDTO.getFerramentas().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getFerramentas().size() + " ferramentas...");
                coatingsToolService.saveAll(detailsDTO.getFerramentas(), phaseId);
                System.out.println("  ✓ Ferramentas salvas!");
            }
            
            if (detailsDTO.getFotos() != null && !detailsDTO.getFotos().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getFotos().size() + " fotos...");
                coatingsPhotoRecordService.saveAll(detailsDTO.getFotos(), phaseId);
                System.out.println("  ✓ Fotos salvas!");
            }
            
            System.out.println("--- FIM saveAllPhaseDetails (Coatings)\n");
        } catch (Exception e) {
            System.err.println("ERRO em saveAllPhaseDetails (Coatings): " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public Optional<Coatings> getCoatingsById(String id) {
        return coatingsRepository.findByIdOptional(id);
    }
}