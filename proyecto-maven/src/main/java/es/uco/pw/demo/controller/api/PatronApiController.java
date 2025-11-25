package es.uco.pw.demo.controller.api;

import es.uco.pw.demo.model.Patron;
import es.uco.pw.demo.model.PatronRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patrones")
public class PatronApiController {

    private final PatronRepository patronRepository;

    public PatronApiController(PatronRepository patronRepository) {
        this.patronRepository = patronRepository;
    }

    // 3. Obtener la lista completa de patrones (GET)
    @GetMapping
    public ResponseEntity<List<Patron>> getAllPatrones() {
        List<Patron> patrones = patronRepository.findAllPatrones();
        return ResponseEntity.ok(patrones);
    }

    // 5. Crear un nuevo patrón, sin asociarle embarcación (POST)
    //    Body JSON ejemplo:
    //    {
    //      "id": 1,
    //      "dni": "12345678A",
    //      "nombre": "Juan",
    //      "apellidos": "Pérez Gómez",
    //      "fechaNacimiento": "1980-01-01",
    //      "fechaTituloPatron": "2010-06-10"
    //    }
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> createPatron(@RequestBody Patron patron) {

        if (patron == null || patron.getDni() == null) {
            return ResponseEntity
                    .badRequest()
                    .body("El DNI del patrón es obligatorio.");
        }

        // Comprobamos que no exista ya ese DNI
        List<Patron> existentes = patronRepository.findAllPatrones();
        boolean yaExiste = existentes.stream()
                .anyMatch(p -> patron.getDni().equalsIgnoreCase(p.getDni()));

        if (yaExiste) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Ya existe un patrón con DNI: " + patron.getDni());
        }

        boolean ok = patronRepository.addPatron(patron);
        if (!ok) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("No se ha podido crear el patrón en la base de datos.");
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(patron);
    }
}
