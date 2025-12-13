package es.uco.pw.demo.controller.api;

import es.uco.pw.demo.model.Inscripcion;
import es.uco.pw.demo.model.InscripcionRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import es.uco.pw.demo.model.InscripcionType;
import es.uco.pw.demo.model.Familia;
import es.uco.pw.demo.model.FamiliaRepository;

@RestController
@RequestMapping("/api/inscripciones")
public class InscripcionApiController {

    private final InscripcionRepository inscripcionRepository;
    private final FamiliaRepository familiaRepository;

    public InscripcionApiController(InscripcionRepository inscripcionRepository,
    FamiliaRepository familiaRepository
    ) {
        this.inscripcionRepository = inscripcionRepository;
        this.familiaRepository = familiaRepository;
    }

    @GetMapping
    public ResponseEntity<?> getAllInscripciones() {
        return ResponseEntity.ok(inscripcionRepository.findAllInscripciones());
    }

    @PutMapping(path = "/individual_a_familiar/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> putInscripcion(
            @PathVariable Integer id,
            @RequestBody Integer familiaId) {

        Inscripcion actual = inscripcionRepository.findInscripcionById(id);
        if (actual == null) {
            return ResponseEntity.notFound().build();
        }

        // Si tiene familia es que no es individual

        if(actual.getFamiliaId() != null){
            System.out.println("Esta inscripcion ya es familiar");
            return ResponseEntity.badRequest().build();
        }

        Familia newFamilia = familiaRepository.findFamiliaById(familiaId);

        if(newFamilia == null ){
            System.out.println("No existe esta familia\n");
            return ResponseEntity.notFound().build();
        }

        // Habría que hacerlo distinguiendo niños de adultos pero no da tiempo asi que directamente solo adultos!

        actual.setTipoCuota(InscripcionType.FAMILIAR);
        actual.setFamiliaId(familiaId);

        boolean ok = inscripcionRepository.updateInscripcion(actual);

        Integer num = newFamilia.getNumAdultos();
        newFamilia.setNumAdultos(num+1);

        boolean famOk = familiaRepository.updateFamilia(newFamilia);

        if (!ok || !famOk) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar la inscripción en la BD.");
        }

        return ResponseEntity.ok(actual);
    }

    @DeleteMapping("/eliminar_inscripcion/{dni}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteInscripcion(@PathVariable String dni){

        Inscripcion previa = inscripcionRepository.findInscripcionByDNI(dni);

        if(previa == null){
            System.out.println("No existe esta inscripcion\n");
        }
        else{
            Integer famId = previa.getFamiliaId();

        if(famId != null){
            Familia famPrevia = familiaRepository.findFamiliaById(famId);

            if(famPrevia.getDniTitular() == dni){
                boolean ok = familiaRepository.deleteFamilia(famId);

                if(!ok){
                    System.out.println("No se ha podido borrar la familia \n");
                }
            }
            else{
                famPrevia.setNumAdultos(famPrevia.getNumAdultos()-1);
                boolean ok = familiaRepository.updateFamilia(famPrevia);
                if(!ok){
                    System.out.println("No se ha podido actualizar el numero de adultos de la familia");
                }
            }
        }

        inscripcionRepository.deleteInscripcionByDniTitular(dni);
        }

    }



}


