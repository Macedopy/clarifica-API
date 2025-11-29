package construction.coatings;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Optional;
import java.util.UUID;

@Path("/coatings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CoatingsController {
    
    @Inject
    CoatingsService coatingsService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createCoatingsAndDetails(CoatingsDTO detailsDTO) {
        String phaseId = UUID.randomUUID().toString();
        
        try {
            System.out.println("========== INICIANDO SALVAMENTO COATINGS ==========");
            System.out.println("Phase ID: " + phaseId);
            System.out.println("DTO recebido: " + detailsDTO);
            
            Coatings coatings = new Coatings();
            coatings.setId(phaseId);
            coatings.setName(detailsDTO.getPhaseName()); 
            coatings.setContractor(detailsDTO.getContractor());
            
            System.out.println("[1/3] Salvando Coatings...");
            coatingsService.saveCoatings(coatings);
            System.out.println("[1/3] ✓ Coatings salvo com sucesso!");
            
            System.out.println("[2/3] Verificando detalhes do DTO...");
            if (detailsDTO.getEquipe() != null) {
                System.out.println("  - Equipe: " + detailsDTO.getEquipe().size() + " membros");
            } else {
                System.out.println("  - Equipe: NULL");
            }
            
            System.out.println("[3/3] Salvando detalhes da fase...");
            coatingsService.saveAllPhaseDetails(phaseId, detailsDTO);
            System.out.println("[3/3] ✓ Detalhes salvos com sucesso!");
            
            System.out.println("========== COATINGS SALVO COM SUCESSO ==========\n");
            
            return Response.status(Response.Status.CREATED)
                           .entity(new ResponseDTO("Fase Coatings criada com sucesso", phaseId))
                           .build();
                             
        } catch (Exception e) {
            System.err.println("========== ERRO NO SALVAMENTO COATINGS ==========");
            System.err.println("Erro: " + e.getMessage());
            e.printStackTrace();
            System.err.println("=========================================\n");
            
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity(new ErrorDTO("Erro ao salvar Coatings", e.getMessage()))
                           .build();
        }
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateCoatings(@PathParam("id") String id, CoatingsDTO detailsDTO) {
        try {
            System.out.println("========== INICIANDO UPDATE COMPLETO COATINGS ==========");
            Coatings tempCoatings = new Coatings();
            
            if (detailsDTO.getPhaseName() != null) {
                tempCoatings.setName(detailsDTO.getPhaseName());
            }
            tempCoatings.setContractor(detailsDTO.getContractor());
            
            coatingsService.updateCoatings(id, tempCoatings);

            coatingsService.saveAllPhaseDetails(id, detailsDTO);
            
            System.out.println("========== UPDATE CONCLUÍDO ==========");

            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Erro ao atualizar: " + e.getMessage())
                           .build();
        }
    }

    @PUT
    @Path("/{id}/details")
    @Transactional
    public Response updateCoatingsDetails(
        @PathParam("id") String phaseId, 
        CoatingsDTO detailsDTO) {
        
        try {
            coatingsService.saveAllPhaseDetails(phaseId, detailsDTO);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Erro ao atualizar detalhes: " + e.getMessage())
                           .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getCoatings(@PathParam("id") String id) {
        Optional<Coatings> coatings = coatingsService.getCoatingsById(id);

        if (coatings.isPresent()) {
            return Response.ok(coatings.get()).build();
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