package construction.hydraulic.entity_external;

import construction.components.photo.PhotoCategory;
import construction.components.photo.PhotoRecordDTO;
import construction.hydraulic.Hydraulic;
import construction.hydraulic.HydraulicRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class HydraulicPhotoRecordService {

    @Inject
    HydraulicPhotoRecordRepository repository;

    @Inject
    HydraulicRepository hydraulicRepository;

    @Transactional
    public void saveAll(List<PhotoRecordDTO> dtos, String phaseId) {
        if (dtos == null || dtos.isEmpty()) {
            return;
        }

        Hydraulic hydraulic = hydraulicRepository.findByIdOptional(phaseId)
                .orElseThrow(() -> new NotFoundException("Hydraulic não encontrada com ID: " + phaseId));

        for (PhotoRecordDTO dto : dtos) {
            HydraulicPhotoRecord entity = mapToEntity(dto);

            if (dto.getId() == null || dto.getId().isBlank()) {
                entity.setId(UUID.randomUUID().toString());
            } else {
                entity.setId(dto.getId());
            }

            entity.setPhaseId(phaseId);
            entity.setHydraulic(hydraulic);

            repository.persist(entity);
        }
    }

    protected HydraulicPhotoRecord mapToEntity(PhotoRecordDTO dto) {
        HydraulicPhotoRecord entity = new HydraulicPhotoRecord();

        entity.setFilePath(dto.getFilePath() != null ? dto.getFilePath() : "caminho/padrao/imagem.jpg");
        entity.setCaption(dto.getCaption());

        entity.setCategory(PhotoCategory.fromString(dto.getCategory()));

        if (dto.getUploadedAt() != null) {
            try {
                entity.setUploadedAt(LocalDateTime.parse(dto.getUploadedAt()));
            } catch (Exception e) {
                entity.setUploadedAt(LocalDateTime.now());
            }
        } else {
            entity.setUploadedAt(LocalDateTime.now());
        }

        return entity;
    }
}
