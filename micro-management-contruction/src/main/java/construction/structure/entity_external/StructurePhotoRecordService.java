package construction.structure.entity_external;

import construction.components.photo.PhotoCategory;
import construction.components.photo.PhotoRecordDTO;
import construction.structure.Structure;
import construction.structure.StructureRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class StructurePhotoRecordService {

    @Inject
    StructurePhotoRecordRepository repository;

    @Inject
    StructureRepository structureRepository;
    
    @Transactional
    public void saveAll(List<PhotoRecordDTO> dtos, String phaseId) {
        if (dtos == null || dtos.isEmpty()) {
            return;
        }

        Structure structure = structureRepository.findByIdOptional(phaseId)
            .orElseThrow(() -> new NotFoundException("Structure não encontrada com ID: " + phaseId));
        
        for (PhotoRecordDTO dto : dtos) {
            StructurePhotoRecord entity = mapToEntity(dto);
            
            if (dto.getId() == null || dto.getId().isBlank()) {
                entity.setId(UUID.randomUUID().toString());
            } else {
                entity.setId(dto.getId());
            }
            
            entity.setPhaseId(phaseId);
            entity.setStructure(structure); 
            
            repository.persist(entity);
        }
    }

    protected StructurePhotoRecord mapToEntity(PhotoRecordDTO dto) {
        StructurePhotoRecord entity = new StructurePhotoRecord();
        
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