package construction.hydraulic.entity_external;

import construction.components.executed_services.ExecutedServiceDTO;
import construction.components.executed_services.ExecutedServiceStatus;
import construction.hydraulic.Hydraulic;
import construction.hydraulic.HydraulicRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class HydraulicExecutedServiceService {

    @Inject
    HydraulicExecutedServiceRepository repository;

    @Inject
    HydraulicRepository hydraulicRepository;

    @Transactional
    public void saveAll(List<ExecutedServiceDTO> dtos, String phaseId) {

        if (dtos == null || dtos.isEmpty()) {
            return;
        }

        // Busca a Hydraulic (Fase pai)
        Hydraulic hydraulic = hydraulicRepository.findByIdOptional(phaseId)
                .orElseThrow(() ->
                        new NotFoundException("Hydraulic não encontrada com ID: " + phaseId)
                );

        for (ExecutedServiceDTO dto : dtos) {
            HydraulicExecutedService entity = mapDtoToEntity(dto);
            entity.setId(UUID.randomUUID().toString());
            entity.setHydraulic(hydraulic);
            entity.setPhaseId(phaseId);

            System.out.println("Persistindo serviço Hidráulico: " + entity.getId() + " - " + entity.getName());

            repository.persist(entity);
        }
    }

    private HydraulicExecutedService mapDtoToEntity(ExecutedServiceDTO dto) {
        HydraulicExecutedService entity = new HydraulicExecutedService();

        // Evita qualquer valor nulo ou vazio que quebraria o banco
        entity.setName(
                dto.getName() != null && !dto.getName().trim().isEmpty()
                        ? dto.getName().trim()
                        : "Serviço Hidráulico (" + UUID.randomUUID().toString().substring(0, 8) + ")"
        );

        entity.setTeam(
                dto.getTeam() != null && !dto.getTeam().trim().isEmpty()
                        ? dto.getTeam().trim()
                        : "Equipe Hidráulica"
        );

        entity.setPlannedHours(
                dto.getPlannedHours() > 0 ? dto.getPlannedHours() : 8.0
        );

        entity.setExecutedHours(
                dto.getExecutedHours() >= 0 ? dto.getExecutedHours() : 0.0
        );

        // Correção do status
        String statusStr = dto.getStatus() != null ? dto.getStatus().toUpperCase() : "PLANEJADO";
        statusStr = statusStr.replace("ANDAMENTO", "EM_ANDAMENTO");

        try {
            entity.setStatus(ExecutedServiceStatus.valueOf(statusStr));
        } catch (IllegalArgumentException e) {
            entity.setStatus(ExecutedServiceStatus.PLANEJADO);
        }

        entity.setNotes(dto.getNotes());

        if (entity.getPlannedHours() > 0) {
            int progress = (int) Math.round((entity.getExecutedHours() / entity.getPlannedHours()) * 100);
            entity.setProgress(Math.min(100, Math.max(0, progress)));
        } else {
            entity.setProgress(0);
        }

        return entity;
    }
}
