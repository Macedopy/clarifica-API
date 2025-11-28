package construction.finishing.entity_external;

import construction.components.team_present.MemberStatus;
import construction.components.team_present.TeamMemberDTO;
import construction.components.team_present.TeamMemberDetails;
import construction.finishing.Finishing;
import construction.finishing.FinishingRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class FinishingTeamMemberService {
    
    @Inject
    FinishingTeamMemberRepository teamMemberRepository;

    @Inject
    FinishingRepository finishingRepository;

    @Transactional
    public void saveAll(List<TeamMemberDTO> memberDTOs, String phaseId) {
        
        if (memberDTOs == null || memberDTOs.isEmpty()) {
            return;
        }

        Finishing finishing = finishingRepository.findByIdOptional(phaseId)
            .orElseThrow(() -> new NotFoundException("Fase Finishing (Acabamentos) não encontrada com ID: " + phaseId));

        for (TeamMemberDTO dto : memberDTOs) {
            FinishingTeamMember entity = mapDtoToEntity(dto);
            entity.setId(UUID.randomUUID().toString()); 
            entity.setPhaseId(phaseId);
            entity.setFinishing(finishing);
            
            System.out.println("Persistindo membro da equipe de acabamento: " + entity.getId() + " - " + entity.getDetails().getName());
            teamMemberRepository.persist(entity);
        }
    }

    private FinishingTeamMember mapDtoToEntity(TeamMemberDTO dto) {
        FinishingTeamMember entity = new FinishingTeamMember();
        TeamMemberDetails details = new TeamMemberDetails();
        
        // Tratamento de segurança para nome
        String name = dto.getName();
        if (name == null || name.trim().isEmpty()) {
            name = "Membro sem identificação";
        }
        details.setName(name.trim());
        
        details.setRole(dto.getRole() != null ? dto.getRole().trim() : "Não informado");
        details.setCpf(dto.getCpf());

        entity.setDetails(details); 
        entity.setHoursWorked(dto.getHoursWorked());
        
        // Status padrão
        entity.setStatus(dto.getStatus() != null ? dto.getStatus() : MemberStatus.PRESENT);
        entity.setNotes(dto.getNotes());
        
        return entity;
    }
}