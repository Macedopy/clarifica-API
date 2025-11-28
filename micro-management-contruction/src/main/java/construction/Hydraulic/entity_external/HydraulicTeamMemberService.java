package construction.hydraulic.entity_external;

import construction.components.team_present.MemberStatus;
import construction.components.team_present.TeamMemberDTO;
import construction.components.team_present.TeamMemberDetails;
import construction.hydraulic.Hydraulic;
import construction.hydraulic.HydraulicRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class HydraulicTeamMemberService {

    @Inject
    HydraulicTeamMemberRepository teamMemberRepository;

    @Inject
    HydraulicRepository hydraulicRepository;

    @Transactional
    public void saveAll(List<TeamMemberDTO> memberDTOs, String phaseId) {

        if (memberDTOs == null || memberDTOs.isEmpty()) {
            return;
        }

        Hydraulic hydraulic = hydraulicRepository.findByIdOptional(phaseId)
                .orElseThrow(() -> new NotFoundException("Hydraulic não encontrada com ID: " + phaseId));

        for (TeamMemberDTO dto : memberDTOs) {
            HydraulicTeamMember entity = mapDtoToEntity(dto);
            entity.setId(UUID.randomUUID().toString());
            entity.setPhaseId(phaseId);
            entity.setHydraulic(hydraulic);

            System.out.println("Persistindo membro Hydraulic: "
                    + entity.getId() + " - " + entity.getDetails().getName());

            teamMemberRepository.persist(entity);
        }
    }

    private HydraulicTeamMember mapDtoToEntity(TeamMemberDTO dto) {
        HydraulicTeamMember entity = new HydraulicTeamMember();
        TeamMemberDetails details = new TeamMemberDetails();

        // Nome seguro
        String name = dto.getName();
        if (name == null || name.trim().isEmpty()) {
            name = "Membro sem identificação";
        }
        details.setName(name.trim());

        details.setRole(dto.getRole() != null ? dto.getRole().trim() : "Não informado");
        details.setCpf(dto.getCpf());

        entity.setDetails(details);
        entity.setHoursWorked(dto.getHoursWorked());

        // Status seguro
        entity.setStatus(dto.getStatus() != null ? dto.getStatus() : MemberStatus.PRESENT);

        entity.setNotes(dto.getNotes());

        return entity;
    }
}
