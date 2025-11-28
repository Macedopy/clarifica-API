package construction.coatings.entity_external;

import construction.coatings.Coatings;
import construction.coatings.CoatingsRepository;
import construction.components.photo.PhotoCategory;
import construction.components.photo.PhotoRecordDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class CoatingsPhotoRecordService {

    @Inject
    CoatingsPhotoRecordRepository repository;

    @Inject
    CoatingsRepository coatingsRepository;
    
    @Transactional
    public void saveAll(List<PhotoRecordDTO> dtos, String phaseId) {
        if (dtos == null || dtos.isEmpty()) {
            return;
        }

        Coatings coatings = coatingsRepository.findByIdOptional(phaseId)
            .orElseThrow(() -> new NotFoundException("Fase Coatings (Revestimentos) não encontrada com ID: " + phaseId));
        
        for (PhotoRecordDTO dto : dtos) {
            CoatingsPhotoRecord entity = mapToEntity(dto);
            
            if (dto.getId() == null || dto.getId().isBlank()) {
                entity.setId(UUID.randomUUID().toString());
            } else {
                entity.setId(dto.getId());
            }
            
            entity.setPhaseId(phaseId);
            entity.setCoatings(coatings); 
            
            repository.persist(entity);
        }
    }

    protected CoatingsPhotoRecord mapToEntity(PhotoRecordDTO dto) {
        CoatingsPhotoRecord entity = new CoatingsPhotoRecord();
        
        // Tratamento simples se vier nulo/vazio
        entity.setFilePath(
            dto.getFilePath() != null && !dto.getFilePath().isBlank() 
            ? dto.getFilePath() 
            : "caminho/padrao/imagem_revestimento.jpg"
        );
        
        entity.setCaption(dto.getCaption());
        
        // Conversão com fallback de segurança
        try {
            entity.setCategory(PhotoCategory.fromString(dto.getCategory()));
        } catch (Exception e) {
             // Fallback para PROGRESS caso venha nulo ou inválido
             entity.setCategory(PhotoCategory.PROGRESS);
        }

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