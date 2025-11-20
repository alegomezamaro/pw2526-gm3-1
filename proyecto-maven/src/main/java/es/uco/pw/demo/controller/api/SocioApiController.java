package es.uco.pw.demo.controller.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import es.uco.pw.demo.model.Socio;
import es.uco.pw.demo.model.SocioRepository;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/socios")


public class SocioApiController {
    private final SocioRepository socioRepository;

    public SocioApiController(SocioRepository socioRepository) {
        this.socioRepository = socioRepository;
    }

// GET DE TODOS LOS SOCIOS


    @GetMapping
    public ResponseEntity<List<Socio>> getAllSocios() {
        List<Socio> socios = socioRepository.findAllSocios();
        return ResponseEntity.ok(socios);
    }

//GET DE LOS SOCIOS POR DNI

    @GetMapping("/{dni}")
    public ResponseEntity<Socio> getSocioByDni(@PathVariable String dni) {

        Socio socio = socioRepository.findSocioByDni(dni);

        if (socio != null) {
            return ResponseEntity.ok(socio);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

//CREAR NUEVO SOCIO SIN INSCRIPCION PREVIA
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> createSocioSinInscripcion(@RequestBody Socio socio) {

        if ( socio == null || socio.getDni() == null) {
            return ResponseEntity.badRequest().body("El DNI del socio es obligatorio.");
        }

        Socio existente = socioRepository.findSocioByDni(socio.getDni());
        if (existente != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Ya existe un socio con DNI: " + socio.getDni());
        }

        boolean ok = socioRepository.addSocio(socio);
        if ( !ok) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No se ha podido crear el socio en la base de datos.");
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(socio);
        }
        
    }






}