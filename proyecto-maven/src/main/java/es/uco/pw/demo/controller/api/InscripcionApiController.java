package es.uco.pw.demo.controller.api;

import es.uco.pw.demo.model.Inscripcion;
import es.uco.pw.demo.model.InscripcionRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import es.uco.pw.demo.model.InscripcionType;

@RestController
@RequestMapping("/api/inscripciones")
public class InscripcionApiController {

    private final InscripcionRepository inscripcionRepository;

    public InscripcionApiController(InscripcionRepository inscripcionRepository) {
        this.inscripcionRepository = inscripcionRepository;
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

        actual.setTipoCuota(InscripcionType.FAMILIAR);
        actual.setFamiliaId(familiaId);

        boolean ok = inscripcionRepository.updateInscripcion(actual);
        if (!ok) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar la inscripción en la BD.");
        }

        return ResponseEntity.ok(actual);
    }

    @DeleteMapping("/eliminar_inscripcion/{dni}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteInscripcion(@PathVariable String dni){
        inscripcionRepository.deleteInscripcionByDniTitular(dni);
    }



}


