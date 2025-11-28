package construction.finishing.entity_external;

import construction.components.tools.ToolCondition;
import construction.components.tools.ToolDTO;
import construction.finishing.Finishing;
import construction.finishing.FinishingRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class FinishingToolService {

    @Inject FinishingToolRepository repository;
    @Inject FinishingRepository finishingRepository;

    @Transactional
    public void saveAll(List<ToolDTO> dtos, String phaseId) {
        if (dtos == null || dtos.isEmpty()) return;

        Finishing finishing = finishingRepository.findByIdOptional(phaseId)
            .orElseThrow(() -> new NotFoundException("Finishing não encontrada com ID: " + phaseId));

        for (ToolDTO dto : dtos) {
            FinishingTool entity = mapToEntity(dto);
            entity.setId(dto.getId() != null && !dto.getId().isBlank() ? dto.getId() : UUID.randomUUID().toString());
            entity.setPhaseId(phaseId);
            entity.setFinishing(finishing);

            repository.persist(entity);
        }
    }

    private FinishingTool mapToEntity(ToolDTO dto) {
        FinishingTool entity = new FinishingTool();

        entity.setName(
            dto.getName() != null && !dto.getName().trim().isEmpty()
                ? dto.getName().trim()
                : "Ferramenta sem nome (" + UUID.randomUUID().toString().substring(0, 8) + ")"
        );

        entity.setCategory(
            dto.getCategory() != null && !dto.getCategory().trim().isEmpty()
                ? dto.getCategory().trim()
                : "Manual"
        );

        Integer totalQty = dto.getTotalQuantity();
        entity.setTotalQuantity(totalQty != null && totalQty >= 1 ? totalQty : 1);

        Integer inUse = dto.getInUse();
        entity.setInUse(inUse != null && inUse >= 0 ? inUse : 0);

        Integer inMaintenance = dto.getInMaintenance();
        entity.setInMaintenance(inMaintenance != null && inMaintenance >= 0 ? inMaintenance : 0);

        try {
            String cond = dto.getCondition() != null ? dto.getCondition().toUpperCase() : "GOOD";
            entity.setCondition(ToolCondition.valueOf(cond));
        } catch (Exception e) {
            entity.setCondition(ToolCondition.GOOD);
        }

        entity.setNotes(dto.getNotes());

        return entity;
    }
}