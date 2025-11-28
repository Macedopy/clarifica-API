package construction.coatings.entity_external;

import construction.coatings.Coatings;
import construction.coatings.CoatingsRepository;
import construction.components.team_present.MemberStatus;
import construction.components.team_present.TeamMemberDTO;
import construction.components.team_present.TeamMemberDetails;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class CoatingsTeamMemberService {
    
    @Inject
    CoatingsTeamMemberRepository teamMemberRepository;

    @Inject
    CoatingsRepository coatingsRepository;

    @Transactional
    public void saveAll(List<TeamMemberDTO> memberDTOs, String phaseId) {
        
        if (memberDTOs == null || memberDTOs.isEmpty()) {
            return;
        }

        Coatings coatings = coatingsRepository.findByIdOptional(phaseId)
            .orElseThrow(() -> new NotFoundException("Fase Coatings (Revestimentos) não encontrada com ID: " + phaseId));

        for (TeamMemberDTO dto : memberDTOs) {
            CoatingsTeamMember entity = mapDtoToEntity(dto);
            entity.setId(UUID.randomUUID().toString()); 
            entity.setPhaseId(phaseId);
            entity.setCoatings(coatings);
            
            System.out.println("Persistindo membro da equipe de revestimento: " + entity.getId() + " - " + entity.getDetails().getName());
            teamMemberRepository.persist(entity);
        }
    }

    private CoatingsTeamMember mapDtoToEntity(TeamMemberDTO dto) {
        CoatingsTeamMember entity = new CoatingsTeamMember();
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