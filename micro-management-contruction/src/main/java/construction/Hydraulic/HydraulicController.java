package construction.hydraulic;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Optional;
import java.util.UUID;

// O Controller de Hydraulic (Hidráulica) espelhado do Structure (Estrutura)
@Path("/hydraulic")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HydraulicController {

    @Inject
    HydraulicService hydraulicService; // Serviço injetado

    // ====================================================================
    // 1. POST: Criar Hydraulic e Detalhes
    // ====================================================================
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createHydraulicAndDetails(HydraulicDTO detailsDTO) {
        String phaseId = UUID.randomUUID().toString();
        try {
            System.out.println("========== INICIANDO SALVAMENTO ==========");
            System.out.println("Phase ID: " + phaseId);
            System.out.println("DTO recebido: " + detailsDTO);

            Hydraulic hydraulic = new Hydraulic();
            hydraulic.setId(phaseId);
            hydraulic.setName(detailsDTO.getPhaseName());
            hydraulic.setContractor(detailsDTO.getContractor());

            System.out.println("[1/3] Salvando Hydraulic...");
            hydraulicService.saveHydraulic(hydraulic);
            System.out.println("[1/3] ✓ Hydraulic salvo com sucesso!");

            System.out.println("[2/3] Verificando detalhes do DTO...");
            if (detailsDTO.getEquipe() != null) {
                System.out.println("  - Equipe: " + detailsDTO.getEquipe().size() + " membros");
            } else {
                System.out.println("  - Equipe: NULL");
            }

            System.out.println("[3/3] Salvando detalhes da fase...");
            hydraulicService.saveAllPhaseDetails(phaseId, detailsDTO);
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

    // ====================================================================
    // 2. PUT /{id}: Atualizar Hydraulic e Detalhes (UPDATE COMPLETO)
    // ESTE MÉTODO FOI CORRIGIDO PARA ESPELHAR O STRUCTURECONTROLLER
    // ====================================================================
    @PUT
    @Path("/{id}")
    @Transactional
    // Recebe HydraulicDTO e atualiza a entidade principal e os detalhes
    public Response updateHydraulic(@PathParam("id") String id, HydraulicDTO detailsDTO) {
        try {
            System.out.println("========== INICIANDO UPDATE COMPLETO ==========");

            // Criamos uma entidade temporária apenas com os campos que queremos atualizar na tabela principal
            Hydraulic tempHydraulic = new Hydraulic();

            if (detailsDTO.getPhaseName() != null) {
                tempHydraulic.setName(detailsDTO.getPhaseName());
            }
            // Supondo que 'contractor' nunca é nulo ou é obrigatório no DTO
            tempHydraulic.setContractor(detailsDTO.getContractor());

            // 1. Atualiza a entidade Hydraulic
            hydraulicService.updateHydraulic(id, tempHydraulic);

            // 2. Atualiza os detalhes da fase
            hydraulicService.saveAllPhaseDetails(id, detailsDTO);

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

    // ====================================================================
    // 3. PUT /{id}/details: Atualizar Apenas Detalhes
    // ====================================================================
    @PUT
    @Path("/{id}/details")
    @Transactional
    public Response updateHydraulicDetails(@PathParam("id") String phaseId, HydraulicDTO detailsDTO) {
        try {
            hydraulicService.saveAllPhaseDetails(phaseId, detailsDTO);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Erro ao atualizar detalhes: " + e.getMessage())
                    .build();
        }
    }

    // ====================================================================
    // 4. GET /{id}: Obter Hydraulic
    // ====================================================================
    @GET
    @Path("/{id}")
    public Response getHydraulic(@PathParam("id") String id) {
        Optional<Hydraulic> hydraulic = hydraulicService.getHydraulicById(id);
        if (hydraulic.isPresent()) {
            return Response.ok(hydraulic.get()).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // ====================================================================
    // Classes auxiliares para resposta (DTOs)
    // ====================================================================
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