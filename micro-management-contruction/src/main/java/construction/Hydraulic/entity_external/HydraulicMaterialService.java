package construction.hydraulic.entity_external;

import construction.components.used_material.MaterialCategory;
import construction.components.used_material.MaterialDTO;
import construction.components.used_material.MaterialUnit;
import construction.hydraulic.Hydraulic;
import construction.hydraulic.HydraulicRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class HydraulicMaterialService {

    @Inject
    HydraulicMaterialRepository repository;

    @Inject
    HydraulicRepository hydraulicRepository;

    @Transactional
    public void saveAll(List<MaterialDTO> dtos, String phaseId) {
        if (dtos == null || dtos.isEmpty()) return;

        Hydraulic hydraulic = hydraulicRepository.findByIdOptional(phaseId)
                .orElseThrow(() -> new NotFoundException("Hydraulic não encontrada com ID: " + phaseId));

        for (MaterialDTO dto : dtos) {
            HydraulicMaterial entity = mapToEntity(dto);

            String idToUse;
            if (dto.getId() != null && !dto.getId().isBlank()) {
                idToUse = dto.getId();
            } else {
                idToUse = UUID.randomUUID().toString();
            }

            entity.setId(idToUse);
            entity.setPhaseId(phaseId);
            entity.setHydraulic(hydraulic);

            System.out.println("Persistindo material Hydraulic: " + entity.getId() + " - " + entity.getName());
            repository.persist(entity);
        }
    }

    private HydraulicMaterial mapToEntity(MaterialDTO dto) {
        HydraulicMaterial entity = new HydraulicMaterial();

        // Tratamento seguro do nome
        entity.setName(
                dto.getName() != null && !dto.getName().trim().isEmpty()
                        ? dto.getName().trim()
                        : "Material sem nome (" + UUID.randomUUID().toString().substring(0, 8) + ")"
        );

        // Categoria com fallback
        try {
            entity.setCategory(dto.getCategory() != null && !dto.getCategory().trim().isEmpty()
                    ? MaterialCategory.fromValue(dto.getCategory().trim())
                    : MaterialCategory.OTHER);
        } catch (Exception e) {
            entity.setCategory(MaterialCategory.OTHER);
        }

        // Unidade segura com fallback
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

        // Atualiza status de estoque/reposição
        entity.updateRestockStatus();

        return entity;
    }
}
