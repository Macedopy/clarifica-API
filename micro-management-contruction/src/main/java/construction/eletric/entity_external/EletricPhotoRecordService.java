package construction.eletric.entity_external;

import construction.components.photo.PhotoCategory;
import construction.components.photo.PhotoRecordDTO;
import construction.eletric.Eletric;
import construction.eletric.EletricRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class EletricPhotoRecordService {

    @Inject
    EletricPhotoRecordRepository repository;

    @Inject
    EletricRepository eletricRepository;
    
    @Transactional
    public void saveAll(List<PhotoRecordDTO> dtos, String phaseId) {
        if (dtos == null || dtos.isEmpty()) {
            return;
        }

        Eletric eletric = eletricRepository.findByIdOptional(phaseId)
            .orElseThrow(() -> new NotFoundException("Fase Elétrica não encontrada com ID: " + phaseId));
        
        for (PhotoRecordDTO dto : dtos) {
            EletricPhotoRecord entity = mapToEntity(dto);
            
            if (dto.getId() == null || dto.getId().isBlank()) {
                entity.setId(UUID.randomUUID().toString());
            } else {
                entity.setId(dto.getId());
            }
            
            entity.setPhaseId(phaseId);
            entity.setEletric(eletric); 
            
            repository.persist(entity);
        }
    }

    protected EletricPhotoRecord mapToEntity(PhotoRecordDTO dto) {
        EletricPhotoRecord entity = new EletricPhotoRecord();
        
        // Tratamento simples se vier nulo/vazio (já que removemos @NotBlank)
        entity.setFilePath(
            dto.getFilePath() != null && !dto.getFilePath().isBlank() 
            ? dto.getFilePath() 
            : "caminho/padrao/imagem_eletrica.jpg"
        );
        
        entity.setCaption(dto.getCaption());
        
        // Conversão direta igual Foundation
        try {
            entity.setCategory(PhotoCategory.fromString(dto.getCategory()));
        } catch (Exception e) {
             // Fallback caso venha nulo ou inválido
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