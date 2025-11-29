package construction.eletric;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Optional;
import java.util.UUID;

@Path("/eletric")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EletricController {
    
    @Inject
    EletricService eletricService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createEletricAndDetails(EletricDTO detailsDTO) {
        String phaseId = UUID.randomUUID().toString();
        
        try {
            System.out.println("========== INICIANDO SALVAMENTO ELÉTRICA ==========");
            System.out.println("Phase ID: " + phaseId);
            System.out.println("DTO recebido: " + detailsDTO);
            
            Eletric eletric = new Eletric();
            eletric.setId(phaseId);
            eletric.setName(detailsDTO.getPhaseName()); 
            eletric.setContractor(detailsDTO.getContractor());
            
            System.out.println("[1/3] Salvando Eletric...");
            eletricService.saveEletric(eletric);
            System.out.println("[1/3] ✓ Eletric salva com sucesso!");
            
            System.out.println("[2/3] Verificando detalhes do DTO...");
            if (detailsDTO.getEquipe() != null) {
                System.out.println("  - Equipe: " + detailsDTO.getEquipe().size() + " membros");
            } else {
                System.out.println("  - Equipe: NULL");
            }
            
            System.out.println("[3/3] Salvando detalhes da fase...");
            eletricService.saveAllPhaseDetails(phaseId, detailsDTO);
            System.out.println("[3/3] ✓ Detalhes salvos com sucesso!");
            
            System.out.println("========== ELÉTRICA SALVA COM SUCESSO ==========\n");
            
            return Response.status(Response.Status.CREATED)
                           .entity(new ResponseDTO("Fase Elétrica criada com sucesso", phaseId))
                           .build();
                             
        } catch (Exception e) {
            System.err.println("========== ERRO NO SALVAMENTO ELÉTRICA ==========");
            System.err.println("Erro: " + e.getMessage());
            e.printStackTrace();
            System.err.println("=========================================\n");
            
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity(new ErrorDTO("Erro ao salvar Elétrica", e.getMessage()))
                           .build();
        }
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateEletric(@PathParam("id") String id, EletricDTO detailsDTO) {
        try {
            System.out.println("========== INICIANDO UPDATE COMPLETO ELÉTRICA ==========");
            Eletric tempEletric = new Eletric();
            
            if (detailsDTO.getPhaseName() != null) {
                tempEletric.setName(detailsDTO.getPhaseName());
            }
            tempEletric.setContractor(detailsDTO.getContractor());
            
            eletricService.updateEletric(id, tempEletric);

            eletricService.saveAllPhaseDetails(id, detailsDTO);
            
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
    public Response updateEletricDetails(
        @PathParam("id") String phaseId, 
        EletricDTO detailsDTO) {
        
        try {
            eletricService.saveAllPhaseDetails(phaseId, detailsDTO);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Erro ao atualizar detalhes: " + e.getMessage())
                           .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getEletric(@PathParam("id") String id) {
        Optional<Eletric> eletric = eletricService.getEletricById(id);

        if (eletric.isPresent()) {
            return Response.ok(eletric.get()).build();
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