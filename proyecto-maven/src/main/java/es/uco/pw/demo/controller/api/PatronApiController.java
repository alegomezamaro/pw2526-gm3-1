package es.uco.pw.demo.controller.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.uco.pw.demo.model.Patron;
import es.uco.pw.demo.model.PatronRepository;

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

    //ACTUALIZAR CAMPOS DE PATRON EXCEPTUANDO EL CAMPO DNI
    @PatchMapping(path="/actualizar_patron/{dni}", consumes="application/json")
    public ResponseEntity<?> actualizarPatron(@PathVariable String dni, @RequestBody Patron Patron){
        Patron PatronActualizar = patronRepository.findPatronByDNI(dni);

        if(PatronActualizar == null){ return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No existe el Patron con dni: " + dni);}

        if( Patron.getNombre() != null){ PatronActualizar.setNombre(Patron.getNombre());}
        if( Patron.getApellidos() != null){ PatronActualizar.setApellidos(Patron.getApellidos());}
        if( Patron.getFechaNacimiento() != null){ PatronActualizar.setFechaNacimiento(Patron.getFechaNacimiento());}
        if( Patron.getFechaTituloPatron() != null){ PatronActualizar.setFechaTituloPatron(Patron.getFechaTituloPatron());}

        boolean ok = patronRepository.updatePatron(PatronActualizar);
        if ( !ok) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No se ha podido actualizar el Patron con dni: " + dni);
        } else {
            return ResponseEntity.ok(PatronActualizar);
        }
    }
}
