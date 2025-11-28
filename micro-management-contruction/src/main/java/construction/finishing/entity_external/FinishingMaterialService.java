package construction.finishing.entity_external;

import construction.components.used_material.MaterialCategory;
import construction.components.used_material.MaterialDTO;
import construction.components.used_material.MaterialUnit;
import construction.finishing.Finishing;
import construction.finishing.FinishingRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class FinishingMaterialService {

    @Inject FinishingMaterialRepository repository;
    @Inject FinishingRepository finishingRepository;

    @Transactional
    public void saveAll(List<MaterialDTO> dtos, String phaseId) {
        if (dtos == null || dtos.isEmpty()) return;

        Finishing finishing = finishingRepository.findByIdOptional(phaseId)
            .orElseThrow(() -> new NotFoundException("Fase Finishing (Acabamentos) não encontrada com ID: " + phaseId));

        for (MaterialDTO dto : dtos) {
            FinishingMaterial entity = mapToEntity(dto);
            String idToUse;
            if (dto.getId() != null && !dto.getId().isBlank()) {
                idToUse = dto.getId();
            } else {
                idToUse = UUID.randomUUID().toString();
            }
            entity.setId(idToUse);
            entity.setPhaseId(phaseId);
            entity.setFinishing(finishing);

            repository.persist(entity);
        }
    }

    private FinishingMaterial mapToEntity(MaterialDTO dto) {
        FinishingMaterial entity = new FinishingMaterial();

        // Tratamento de segurança para nome vazio
        entity.setName(
            dto.getName() != null && !dto.getName().trim().isEmpty()
                ? dto.getName().trim()
                : "Material de Acabamento sem nome (" + UUID.randomUUID().toString().substring(0, 8) + ")"
        );

        // Categoria com fallback
        try {
            entity.setCategory(dto.getCategory() != null && !dto.getCategory().trim().isEmpty()
                ? MaterialCategory.fromValue(dto.getCategory().trim())
                : MaterialCategory.OTHER);
        } catch (Exception e) {
            entity.setCategory(MaterialCategory.OTHER);
        }

        // Unidade com fallback
        try {
            entity.setUnit(dto.getUnit() != null && !dto.getUnit().trim().isEmpty()
                ? MaterialUnit.fromString(dto.getUnit().trim())
                : MaterialUnit.UNIT);
        } catch (Exception e) {
            entity.setUnit(MaterialUnit.UNIT);
        }

        // Quantidades seguras
        entity.setConsumedQuantity(dto.getQuantityUsed() != null ? dto.getQuantityUsed().doubleValue() : 0.0);
        entity.setCurrentStock(dto.getCurrentStock() != null ? dto.getCurrentStock().doubleValue() : 0.0);
        entity.setMinimumStock(dto.getMinimumStock() != null ? dto.getMinimumStock().doubleValue() : 10.0);
        entity.setUrgency(dto.getUrgency());

        // Atualiza flag de reposição
        entity.updateRestockStatus();

        return entity;
    }
}