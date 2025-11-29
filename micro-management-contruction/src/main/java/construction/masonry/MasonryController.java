package construction.masonry;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Optional;
import java.util.UUID;

@Path("/masonry")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MasonryController {
    
    @Inject
    MasonryService masonryService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createMasonryAndDetails(MasonryDTO detailsDTO) {
        String phaseId = UUID.randomUUID().toString();
        
        try {
            System.out.println("========== INICIANDO SALVAMENTO MASONRY ==========");
            System.out.println("Phase ID: " + phaseId);
            System.out.println("DTO recebido: " + detailsDTO);
            
            Masonry masonry = new Masonry();
            masonry.setId(phaseId);
            masonry.setName(detailsDTO.getPhaseName()); 
            masonry.setContractor(detailsDTO.getContractor());
            
            System.out.println("[1/3] Salvando Masonry...");
            masonryService.saveMasonry(masonry);
            System.out.println("[1/3] ✓ Masonry salva com sucesso!");
            
            System.out.println("[2/3] Verificando detalhes do DTO...");
            if (detailsDTO.getEquipe() != null) {
                System.out.println("  - Equipe: " + detailsDTO.getEquipe().size() + " membros");
            } else {
                System.out.println("  - Equipe: NULL");
            }
            
            System.out.println("[3/3] Salvando detalhes da fase...");
            masonryService.saveAllPhaseDetails(phaseId, detailsDTO);
            System.out.println("[3/3] ✓ Detalhes salvos com sucesso!");
            
            System.out.println("========== MASONRY SALVO COM SUCESSO ==========\n");
            
            return Response.status(Response.Status.CREATED)
                           .entity(new ResponseDTO("Fase Masonry criada com sucesso", phaseId))
                           .build();
                             
        } catch (Exception e) {
            System.err.println("========== ERRO NO SALVAMENTO MASONRY ==========");
            System.err.println("Erro: " + e.getMessage());
            e.printStackTrace();
            System.err.println("=========================================\n");
            
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity(new ErrorDTO("Erro ao salvar Masonry", e.getMessage()))
                           .build();
        }
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateMasonry(@PathParam("id") String id, MasonryDTO detailsDTO) {
        try {
            System.out.println("========== INICIANDO UPDATE COMPLETO MASONRY ==========");
            Masonry tempMasonry = new Masonry();
            
            if (detailsDTO.getPhaseName() != null) {
                tempMasonry.setName(detailsDTO.getPhaseName());
            }
            tempMasonry.setContractor(detailsDTO.getContractor());
            
            masonryService.updateMasonry(id, tempMasonry);

            masonryService.saveAllPhaseDetails(id, detailsDTO);
            
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
    public Response updateMasonryDetails(
        @PathParam("id") String phaseId, 
        MasonryDTO detailsDTO) {
        
        try {
            masonryService.saveAllPhaseDetails(phaseId, detailsDTO);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Erro ao atualizar detalhes: " + e.getMessage())
                           .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getMasonry(@PathParam("id") String id) {
        Optional<Masonry> masonry = masonryService.getMasonryById(id);
        if (masonry.isPresent()) {
            return Response.ok(masonry.get()).build();
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