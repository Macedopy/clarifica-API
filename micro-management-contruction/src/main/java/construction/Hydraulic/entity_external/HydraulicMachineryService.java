package construction.hydraulic.entity_external;

import construction.components.machinery.Condition;
import construction.components.machinery.FuelUnit;
import construction.components.machinery.MachineryDTO;
import construction.hydraulic.Hydraulic;
import construction.hydraulic.HydraulicRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class HydraulicMachineryService {

    @Inject
    HydraulicMachineryRepository repository;

    @Inject
    HydraulicRepository hydraulicRepository;

    @Transactional
    public void saveAll(List<MachineryDTO> dtos, String phaseId) {
        if (dtos == null || dtos.isEmpty()) {
            return;
        }

        Hydraulic hydraulic = hydraulicRepository.findByIdOptional(phaseId)
                .orElseThrow(() -> new NotFoundException("Hydraulic não encontrada com ID: " + phaseId));

        for (MachineryDTO dto : dtos) {
            HydraulicMachinery entity = mapToEntity(dto);

            String idToUse;
            if (dto.getId() != null && !dto.getId().isBlank()) {
                idToUse = dto.getId();
            } else {
                idToUse = UUID.randomUUID().toString();
            }

            entity.setId(idToUse);
            entity.setPhaseId(phaseId);
            entity.setHydraulic(hydraulic);

            repository.persist(entity);
        }
    }

    protected HydraulicMachinery mapToEntity(MachineryDTO dto) {
        HydraulicMachinery entity = new HydraulicMachinery();

        entity.setName(dto.getName());

        String category = dto.getCategory();
        if (category == null || category.isBlank()) {
            category = "OTHER";
        }
        entity.setCategory(category);

        entity.setTotalQuantity(dto.getTotalQuantity());
        entity.setInOperation(dto.getInOperation());
        entity.setInMaintenance(dto.getInMaintenance());

        entity.setHoursWorked(dto.getHoursWorked());
        entity.setFuelUsed(dto.getFuelUsed());

        if (dto.getFuelUnit() != null) {
            entity.setFuelUnit(FuelUnit.valueOf(dto.getFuelUnit().toUpperCase()));
        }

        if (dto.getCondition() != null && !dto.getCondition().isBlank()) {
            entity.setCondition(Condition.valueOf(dto.getCondition().toUpperCase()));
        } else {
            entity.setCondition(Condition.GOOD);
        }

        entity.setNotes(dto.getNotes());

        return entity;
    }
}
