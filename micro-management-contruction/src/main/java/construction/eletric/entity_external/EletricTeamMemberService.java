package construction.eletric.entity_external;

import construction.components.team_present.MemberStatus;
import construction.components.team_present.TeamMemberDTO;
import construction.components.team_present.TeamMemberDetails;
import construction.eletric.Eletric;
import construction.eletric.EletricRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class EletricTeamMemberService {
    
    @Inject
    EletricTeamMemberRepository teamMemberRepository;

    @Inject
    EletricRepository eletricRepository;

    @Transactional
    public void saveAll(List<TeamMemberDTO> memberDTOs, String phaseId) {
        
        if (memberDTOs == null || memberDTOs.isEmpty()) {
            return;
        }

        Eletric eletric = eletricRepository.findByIdOptional(phaseId)
            .orElseThrow(() -> new NotFoundException("Fase Elétrica não encontrada com ID: " + phaseId));

        for (TeamMemberDTO dto : memberDTOs) {
            EletricTeamMember entity = mapDtoToEntity(dto);
            entity.setId(UUID.randomUUID().toString()); 
            entity.setPhaseId(phaseId);
            entity.setEletric(eletric);
            
            teamMemberRepository.persist(entity);
        }
    }

    private EletricTeamMember mapDtoToEntity(TeamMemberDTO dto) {
        EletricTeamMember entity = new EletricTeamMember();
        TeamMemberDetails details = new TeamMemberDetails();
        
        // Tratamento de nome para evitar nulo
        String name = dto.getName();
        if (name == null || name.trim().isEmpty()) {
            name = "Membro sem identificação";
        }
        details.setName(name.trim());
        
        details.setRole(dto.getRole() != null ? dto.getRole().trim() : "Não informado");
        details.setCpf(dto.getCpf());

        entity.setDetails(details); 
        entity.setHoursWorked(dto.getHoursWorked());
        
        // Status padrão se vier nulo
        entity.setStatus(dto.getStatus() != null ? dto.getStatus() : MemberStatus.PRESENT);
        entity.setNotes(dto.getNotes());
        
        return entity;
    }
}