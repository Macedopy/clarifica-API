package construction.eletric.entity_external;

import construction.components.tools.ToolCondition;
import construction.components.tools.ToolDTO;
import construction.eletric.Eletric;
import construction.eletric.EletricRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class EletricToolService {

    @Inject EletricToolRepository repository;
    @Inject EletricRepository eletricRepository;

    @Transactional
    public void saveAll(List<ToolDTO> dtos, String phaseId) {
        if (dtos == null || dtos.isEmpty()) return;

        Eletric eletric = eletricRepository.findByIdOptional(phaseId)
            .orElseThrow(() -> new NotFoundException("Fase Elétrica não encontrada com ID: " + phaseId));

        for (ToolDTO dto : dtos) {
            EletricTool entity = mapToEntity(dto);
            entity.setId(dto.getId() != null && !dto.getId().isBlank() ? dto.getId() : UUID.randomUUID().toString());
            entity.setPhaseId(phaseId);
            entity.setEletric(eletric);

            repository.persist(entity);
        }
    }

    private EletricTool mapToEntity(ToolDTO dto) {
        EletricTool entity = new EletricTool();

        // NOME — tratamento seguro
        entity.setName(
            dto.getName() != null && !dto.getName().trim().isEmpty()
                ? dto.getName().trim()
                : "Ferramenta Elétrica sem nome (" + UUID.randomUUID().toString().substring(0, 8) + ")"
        );

        // CATEGORIA — tratamento seguro
        entity.setCategory(
            dto.getCategory() != null && !dto.getCategory().trim().isEmpty()
                ? dto.getCategory().trim()
                : "Manual"
        );

        // QUANTIDADE TOTAL — mínimo 1
        Integer totalQty = dto.getTotalQuantity();
        entity.setTotalQuantity(totalQty != null && totalQty >= 1 ? totalQty : 1);

        // EM USO — pode ser 0
        Integer inUse = dto.getInUse();
        entity.setInUse(inUse != null && inUse >= 0 ? inUse : 0);

        // EM MANUTENÇÃO — pode ser 0
        Integer inMaintenance = dto.getInMaintenance();
        entity.setInMaintenance(inMaintenance != null && inMaintenance >= 0 ? inMaintenance : 0);

        // CONDIÇÃO — fallback seguro
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