package construction.coatings.entity_external;

import construction.components.executed_services.ExecutedServiceDTO;
import construction.components.executed_services.ExecutedServiceStatus;
import construction.coatings.Coatings;
import construction.coatings.CoatingsRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class CoatingsExecutedServiceService {
    
    @Inject
    CoatingsExecutedServiceRepository repository;

    @Inject
    CoatingsRepository coatingsRepository;

    @Transactional
    public void saveAll(List<ExecutedServiceDTO> dtos, String phaseId) {
        if (dtos == null || dtos.isEmpty()) {
            return;
        }

        Coatings coatings = coatingsRepository.findByIdOptional(phaseId)
            .orElseThrow(() -> new NotFoundException("Fase Coatings (Revestimentos) não encontrada com ID: " + phaseId));

        for (ExecutedServiceDTO dto : dtos) {
            CoatingsExecutedService entity = mapDtoToEntity(dto);
            entity.setId(UUID.randomUUID().toString());
            entity.setCoatings(coatings);
            entity.setPhaseId(phaseId);
            
            System.out.println("Persistindo serviço de revestimento: " + entity.getId() + " - " + entity.getName());
            repository.persist(entity);
        }
    }

    private CoatingsExecutedService mapDtoToEntity(ExecutedServiceDTO dto) {
        CoatingsExecutedService entity = new CoatingsExecutedService();

        // Tratamento de segurança para nulos/vazios
        entity.setName(
            dto.getName() != null && !dto.getName().trim().isEmpty() 
                ? dto.getName().trim() 
                : "Serviço de Revestimento sem nome"
        );

        entity.setTeam(
            dto.getTeam() != null && !dto.getTeam().trim().isEmpty()
                ? dto.getTeam().trim()
                : "Equipe de Revestimento Padrão"
        );

        entity.setPlannedHours(
            dto.getPlannedHours() > 0 ? dto.getPlannedHours() : 8.0
        );

        entity.setExecutedHours(dto.getExecutedHours() >= 0 ? dto.getExecutedHours() : 0.0);

        String statusStr = dto.getStatus() != null ? dto.getStatus().toUpperCase() : "PLANEJADO";
        statusStr = statusStr.replace("ANDAMENTO", "EM_ANDAMENTO");
        
        try {
            entity.setStatus(ExecutedServiceStatus.valueOf(statusStr));
        } catch (IllegalArgumentException e) {
            entity.setStatus(ExecutedServiceStatus.PLANEJADO);
        }

        entity.setNotes(dto.getNotes());

        // Progresso calculado
        if (entity.getPlannedHours() > 0) {
            int progress = (int) Math.round((entity.getExecutedHours() / entity.getPlannedHours()) * 100);
            entity.setProgress(Math.min(100, Math.max(0, progress)));
        } else {
            entity.setProgress(0);
        }

        return entity;
    }
}