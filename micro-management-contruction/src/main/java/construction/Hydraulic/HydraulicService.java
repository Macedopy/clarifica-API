package construction.hydraulic;

import java.util.Optional;

import construction.hydraulic.entity_external.HydraulicExecutedServiceService;
import construction.hydraulic.entity_external.HydraulicMachineryService;
import construction.hydraulic.entity_external.HydraulicMaterialService;
import construction.hydraulic.entity_external.HydraulicPhotoRecordService;
import construction.hydraulic.entity_external.HydraulicTeamMemberService;
import construction.hydraulic.entity_external.HydraulicToolService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class HydraulicService {

    @Inject HydraulicRepository hydraulicRepository;
    @Inject HydraulicMaterialService hydraulicMaterialService;
    @Inject HydraulicToolService hydraulicToolService;
    @Inject HydraulicMachineryService hydraulicMachineryService;
    @Inject HydraulicTeamMemberService hydraulicTeamMemberService;
    @Inject HydraulicExecutedServiceService hydraulicExecutedServiceService;
    @Inject HydraulicPhotoRecordService hydraulicPhotoRecordService;

    @Transactional
    public void saveHydraulic(Hydraulic hydraulic) {
        hydraulicRepository.persist(hydraulic);
    }

    @Transactional
    public void updateHydraulic(String id, Hydraulic updatedHydraulic) {
        Hydraulic entity = hydraulicRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Fase Hydraulic não encontrada com ID: " + id));

        entity.setName(updatedHydraulic.getName());
        entity.setContractor(updatedHydraulic.getContractor());

        hydraulicRepository.persist(entity);
    }

    @Transactional
    public void saveAllPhaseDetails(String phaseId, HydraulicDTO detailsDTO) {
        try {
            System.out.println("\n--- INICIANDO saveAllPhaseDetails para phaseId: " + phaseId);

            if (detailsDTO.getEquipe() != null && !detailsDTO.getEquipe().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getEquipe().size() + " membros da equipe...");
                hydraulicTeamMemberService.saveAll(detailsDTO.getEquipe(), phaseId);
                System.out.println("  ✓ Equipe salva!");
            } else {
                System.out.println("  ⊘ Equipe vazia ou null");
            }

            if (detailsDTO.getServicos() != null && !detailsDTO.getServicos().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getServicos().size() + " serviços executados...");
                hydraulicExecutedServiceService.saveAll(detailsDTO.getServicos(), phaseId);
                System.out.println("  ✓ Serviços salvos!");
            }

            if (detailsDTO.getMaquinarios() != null && !detailsDTO.getMaquinarios().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getMaquinarios().size() + " maquinários...");
                hydraulicMachineryService.saveAll(detailsDTO.getMaquinarios(), phaseId);
                System.out.println("  ✓ Maquinários salvos!");
            }

            if (detailsDTO.getMateriais() != null && !detailsDTO.getMateriais().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getMateriais().size() + " materiais...");
                hydraulicMaterialService.saveAll(detailsDTO.getMateriais(), phaseId);
                System.out.println("  ✓ Materiais salvos!");
            }

            if (detailsDTO.getFerramentas() != null && !detailsDTO.getFerramentas().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getFerramentas().size() + " ferramentas...");
                hydraulicToolService.saveAll(detailsDTO.getFerramentas(), phaseId);
                System.out.println("  ✓ Ferramentas salvas!");
            }

            if (detailsDTO.getFotos() != null && !detailsDTO.getFotos().isEmpty()) {
                System.out.println("  → Salvando " + detailsDTO.getFotos().size() + " fotos...");
                hydraulicPhotoRecordService.saveAll(detailsDTO.getFotos(), phaseId);
                System.out.println("  ✓ Fotos salvas!");
            }

            System.out.println("--- FIM saveAllPhaseDetails\n");
        } catch (Exception e) {
            System.err.println("ERRO em saveAllPhaseDetails: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public Optional<Hydraulic> getHydraulicById(String id) {
        return hydraulicRepository.findByIdOptional(id);
    }
}
