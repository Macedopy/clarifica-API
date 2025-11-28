package construction.hydraulic.entity_external;

import construction.components.tools.ToolCondition;
import construction.components.tools.ToolDTO;
import construction.hydraulic.Hydraulic;
import construction.hydraulic.HydraulicRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class HydraulicToolService {

    @Inject HydraulicToolRepository repository;
    @Inject HydraulicRepository hydraulicRepository;

    @Transactional
    public void saveAll(List<ToolDTO> dtos, String phaseId) {
        if (dtos == null || dtos.isEmpty()) return;

        Hydraulic hydraulic = hydraulicRepository.findByIdOptional(phaseId)
                .orElseThrow(() -> new NotFoundException("Hydraulic não encontrada com ID: " + phaseId));

        for (ToolDTO dto : dtos) {
            HydraulicTool entity = mapToEntity(dto);
            entity.setId(
                    dto.getId() != null && !dto.getId().isBlank()
                            ? dto.getId()
                            : UUID.randomUUID().toString()
            );

            entity.setPhaseId(phaseId);
            entity.setHydraulic(hydraulic);

            System.out.println("Persistindo ferramenta Hydraulic: " + entity.getId() + " - " + entity.getName());
            repository.persist(entity);
        }
    }

    private HydraulicTool mapToEntity(ToolDTO dto) {
        HydraulicTool entity = new HydraulicTool();

        // NOME - tratamento seguro
        entity.setName(
                dto.getName() != null && !dto.getName().trim().isEmpty()
                        ? dto.getName().trim()
                        : "Ferramenta sem nome (" + UUID.randomUUID().toString().substring(0, 8) + ")"
        );

        // CATEGORIA - tratamento seguro
        entity.setCategory(
                dto.getCategory() != null && !dto.getCategory().trim().isEmpty()
                        ? dto.getCategory().trim()
                        : "Manual"
        );

        // QUANTIDADE TOTAL
        Integer totalQty = dto.getTotalQuantity();
        entity.setTotalQuantity(totalQty != null && totalQty >= 1 ? totalQty : 1);

        // EM USO
        Integer inUse = dto.getInUse();
        entity.setInUse(inUse != null && inUse >= 0 ? inUse : 0);

        // EM MANUTENÇÃO
        Integer inMaintenance = dto.getInMaintenance();
        entity.setInMaintenance(inMaintenance != null && inMaintenance >= 0 ? inMaintenance : 0);

        // CONDIÇÃO
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
