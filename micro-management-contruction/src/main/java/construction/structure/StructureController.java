package construction.structure;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Optional;
import java.util.UUID;

@Path("/structure")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StructureController {

    @Inject
    StructureService structureService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createStructureAndDetails(StructureDTO detailsDTO) {
        String phaseId = UUID.randomUUID().toString();
        try {
            System.out.println("========== INICIANDO SALVAMENTO ==========");
            System.out.println("Phase ID: " + phaseId);
            System.out.println("DTO recebido: " + detailsDTO);

            Structure structure = new Structure();
            structure.setId(phaseId);
            structure.setName(detailsDTO.getPhaseName());
            structure.setContractor(detailsDTO.getContractor());

            System.out.println("[1/3] Salvando Structure...");
            structureService.saveStructure(structure);
            System.out.println("[1/3] ✓ Structure salva com sucesso!");

            System.out.println("[2/3] Verificando detalhes do DTO...");
            if (detailsDTO.getEquipe() != null) {
                System.out.println("  - Equipe: " + detailsDTO.getEquipe().size() + " membros");
            } else {
                System.out.println("  - Equipe: NULL");
            }

            System.out.println("[3/3] Salvando detalhes da fase...");
            structureService.saveAllPhaseDetails(phaseId, detailsDTO);
            System.out.println("[3/3] ✓ Detalhes salvos com sucesso!");
            System.out.println("========== SALVAMENTO CONCLUÍDO COM SUCESSO ==========\n");

            return Response.status(Response.Status.CREATED)
                    .entity(new ResponseDTO("Fase criada com sucesso", phaseId))
                    .build();

        } catch (Exception e) {
            System.err.println("========== ERRO NO SALVAMENTO ==========");
            System.err.println("Erro: " + e.getMessage());
            e.printStackTrace();
            System.err.println("=========================================\n");

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorDTO("Erro ao salvar", e.getMessage()))
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateStructure(@PathParam("id") String id, StructureDTO detailsDTO) {
        try {
            System.out.println("========== INICIANDO UPDATE COMPLETO ==========");
            Structure tempStructure = new Structure();
            
            if (detailsDTO.getPhaseName() != null) {
                tempStructure.setName(detailsDTO.getPhaseName());
            }
            tempStructure.setContractor(detailsDTO.getContractor());
            
            structureService.updateStructure(id, tempStructure);

            structureService.saveAllPhaseDetails(id, detailsDTO);
            
            System.out.println("========== UPDATE CONCLUÍDO ==========");

            return Response.status(Response.Status.NO_CONTENT).build();

        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (Exception e) {
            e.printStackTrace(); // Ajuda a ver o erro no terminal se acontecer
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Erro ao atualizar: " + e.getMessage())
                    .build();
        }
    }

    @PUT
    @Path("/{id}/details")
    @Transactional
    public Response updateStructureDetails(@PathParam("id") String phaseId, StructureDTO detailsDTO) {
        try {
            structureService.saveAllPhaseDetails(phaseId, detailsDTO);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Erro ao atualizar detalhes: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getStructure(@PathParam("id") String id) {
        Optional<Structure> structure = structureService.getStructureById(id);
        if (structure.isPresent()) {
            return Response.ok(structure.get()).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    public static class ResponseDTO {
        public String message;
        public String phaseId;

        public ResponseDTO(String message, String phaseId) {
            this.message = message;
            this.phaseId = phaseId;
        }
    }

    public static class ErrorDTO {
        public String error;
        public String message;

        public ErrorDTO(String error, String message) {
            this.error = error;
            this.message = message;
        }
    }
}