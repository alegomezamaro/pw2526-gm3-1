package es.uco.pw.demo.controller.api;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import es.uco.pw.demo.model.Embarcacion;
import es.uco.pw.demo.model.InscripcionRepository;
import es.uco.pw.demo.model.InscripcionType;
import es.uco.pw.demo.model.Socio;
import es.uco.pw.demo.model.SocioRepository;
import es.uco.pw.demo.model.Inscripcion;
import es.uco.pw.demo.model.InscripcionRepository;

@RestController
@RequestMapping("/api/socios")


public class SocioApiController {
    private final SocioRepository socioRepository;
    private final InscripcionRepository inscripcionRepository;

    public SocioApiController(SocioRepository socioRepository, InscripcionRepository inscripcionRepository) {
        this.socioRepository = socioRepository;
        this.inscripcionRepository = inscripcionRepository;
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

        Inscripcion newInscripcion = new Inscripcion();
        newInscripcion.setTipoCuota(InscripcionType.INDIVIDUAL);
        newInscripcion.setCuotaAnual(300);
        newInscripcion.setFechaInscripcion(LocalDate.now());
        newInscripcion.setDniTitular(socio.getDni());
        newInscripcion.setFamiliaId(null);
 
        socio.setFechaAlta(LocalDate.now());

        boolean ok = socioRepository.addSocio(socio);
        boolean insOk = inscripcionRepository.addInscripcion(newInscripcion);
        if ( !ok || !insOk ) {
            if(!ok) inscripcionRepository.deleteInscripcionByDniTitular(socio.getDni());
            if(!insOk) socioRepository.deleteSocioByDni(socio.getDni());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No se ha podido crear el socio y su inscripcion pertinente en la base de datos." + insOk);
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(socio);
        }
        
    }

    // PUT

    @PutMapping(path = "/{dni}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> updateSocio(
            @PathVariable String dni,
            @RequestBody Socio socio) {

        List<Socio> todas = socioRepository.findAllSocios();
        Socio actual = todas.stream()
                .filter(e -> e.getDni().equalsIgnoreCase(dni))
                .findFirst()
                .orElse(null);

        if (actual == null) {
            return ResponseEntity.notFound().build();
        }

        // Opcional: comprobar que no intentan cambiar la matrícula
        if (socio.getDni() != null &&
            !socio.getDni().equalsIgnoreCase(dni)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("No se puede cambiar el DNI con PUT.");
        }

        // PUT = reemplazo completo (salvo matrícula)
        actual.setNombre(socio.getNombre());
        actual.setApellidos(socio.getApellidos());
        actual.setDireccion(socio.getDireccion());
        actual.setFechaNacimiento(socio.getFechaNacimiento());
        actual.setFechaAlta(socio.getFechaAlta());
        actual.setPatronEmbarcacion(socio.esPatronEmbarcacion());

        boolean ok = socioRepository.updateSocio(actual);
        if (!ok) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("No se ha podido actualizar la embarcación en la base de datos.");
        }

        return ResponseEntity.ok(actual);
    }

    // MÉTODOS PATCH

    //Actualizamos socio excepto el dni
    @PatchMapping(path="/{dni}", consumes="application/json")
    public ResponseEntity<?> actualizarSocio(@PathVariable String dni, @RequestBody Socio socio){
        Socio socioActualizar = socioRepository.findSocioByDni(dni);

        if(socioActualizar == null) return ResponseEntity.notFound().build();

        if( socio.getNombre() != null){ socioActualizar.setNombre(socio.getNombre());}
        if( socio.getApellidos() != null){ socioActualizar.setApellidos(socio.getApellidos());}
        if( socio.getDireccion() != null){ socioActualizar.setDireccion(socio.getDireccion());}
        if( socio.getFechaNacimiento() != null){ socioActualizar.setFechaNacimiento(socio.getFechaNacimiento());}
        if( socio.getFechaAlta() != null){ socioActualizar.setFechaAlta(socio.getFechaAlta());}
        if( socio.esPatronEmbarcacion() == false || socio.esPatronEmbarcacion() == true){ socioActualizar.setPatronEmbarcacion(socio.esPatronEmbarcacion());}

        boolean ok = socioRepository.updateSocio(socioActualizar);
        if ( !ok) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No se ha podido actualizar el socio con dni: " + dni);
        } else {
            return ResponseEntity.ok(socioActualizar);
        }
    }

    //Vinculamos socio a una inscripcion familiar NO HAY RELACION ENTRE UN SOCIO (NO TITULAR) Y UNA INSCRIPCION FAMILIAR
    // @PatchMapping(path="/vincular_socio_a_inscripcionf", consumes="application/json")
    //     public ResponseEntity<?> vincularSocioInscripcionF(@RequestBody Integer id, String dni){

    //     Inscripcion ins = inscripcionRepository.findInscripcionById(id);
    //     if(ins == null){
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No se ha podido encontrar ninguna inscripcion válida con id: " + id + " para vincular al socio con dni: " + dni);
    //     }
    //     if(ins.getTipoCuota() != InscripcionType.FAMILIAR){
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("La inscripcion con id: " + id + " no es válida para este proceso");
    //     }

    //     Socio socio = socioRepository.findSocioByDni(dni);
    //     if(socio == null){
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No se ha encontrado a ningun socio con dni: " + dni);
    //     }

    //     boolean ok = socioRepository.updateSocio(socio);
    //     if ( !ok) {
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No se ha podido vincular el socio con dni: " + dni + " a la inscripcion familiar con id: " + id);
    //     } else {
    //         return ResponseEntity.ok(socio);
    //     }
    // }

    // MÉTODOs DELETE

    // Eliminar inscripcion
    @DeleteMapping("/eliminar_inscripcion/{dni}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteInsByDni(@PathVariable String dni) {
        Socio socio = socioRepository.findSocioByDni(dni);
        if(socio != null){
            if(inscripcionRepository.existsInscripcionByTitular(dni)){
                inscripcionRepository.deleteInscripcionByDniTitular(dni);
            }
        }
    }

    //Eliminar socio sin inscripcion
    @DeleteMapping("/{dni}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSocioByDni(@PathVariable String dni) {
        Socio socio = socioRepository.findSocioByDni(dni);
        if(socio != null){
            if(!inscripcionRepository.existsInscripcionByTitular(dni)){
                socioRepository.deleteSocioByDni(dni);
            }
        }
    }
    
}