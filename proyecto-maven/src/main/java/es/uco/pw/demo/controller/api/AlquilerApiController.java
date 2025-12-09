package es.uco.pw.demo.controller.api;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import es.uco.pw.demo.model.Alquiler;
import es.uco.pw.demo.model.AlquilerRepository;
import es.uco.pw.demo.model.Embarcacion;
import es.uco.pw.demo.model.EmbarcacionRepository;
import es.uco.pw.demo.model.EmbarcacionType;
import es.uco.pw.demo.model.Socio;
import es.uco.pw.demo.model.SocioRepository;





@RestController
@RequestMapping("api/alquileres")

public class AlquilerApiController {
    public final AlquilerRepository alquilerRepository;
    public final EmbarcacionRepository embarcacionRepository;
    public final SocioRepository socioRepository;

    public AlquilerApiController(AlquilerRepository alquilerRepository,EmbarcacionRepository embarcacionRepository,SocioRepository socioRepository){
        this.alquilerRepository = alquilerRepository;
        this.embarcacionRepository = embarcacionRepository;
        this.socioRepository = socioRepository;
    }

    @GetMapping
    public ResponseEntity<List<Alquiler>> getAllAlquileres() {
        List<Alquiler> alquileres = alquilerRepository.findAllAlquileres();
        return  ResponseEntity.ok(alquileres);
    }
    
    @GetMapping("/buscar_alquiler_por_tipo_de_embarcacion")
    public ResponseEntity<List<Alquiler>> getAlquilerByEmbarcacion(@RequestParam("tipo") EmbarcacionType tipo) {
        List<Alquiler> alquileres = alquilerRepository.findAlquilerByEmbarcacionType(tipo);
        return  ResponseEntity.ok(alquileres);
    }

    @GetMapping("/buscar_por_id")
    public ResponseEntity<Alquiler> getAlquilerById(@RequestParam("id") Integer id) {
        Alquiler alquiler = alquilerRepository.findAlquilerById(id);
        if (alquiler != null) {
            return ResponseEntity.ok(alquiler);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/buscar_por_fecha")
    public ResponseEntity<List<Alquiler>> getAlquilerById(@RequestParam("fecha") LocalDate fecha) {
        List<Alquiler> alquileres = alquilerRepository.findAlquileresByFechaInicio(fecha);
        return ResponseEntity.ok(alquileres);
    }
    
    @GetMapping("/embarcaciones_disponibles")
    public ResponseEntity<List<Embarcacion>> getEmbarcacionesDisponibles(
            @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam("plazas") Integer plazas) {
        
        List<Embarcacion> embarcacionesDisponibles = embarcacionRepository.findEmbarcacionesDisponibles(fecha, plazas);
        return ResponseEntity.ok(embarcacionesDisponibles);
    }

    // Añadimos un alquiler si ningun titular/socio asociado
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> createAlquilerSinSocioVinculado(@RequestBody Alquiler alquiler) {

        if ( alquiler == null || alquiler.getMatriculaEmbarcacion() == null) {
            return ResponseEntity.badRequest().body("La matricula de la embarcacion es obligatoria.");
        }

        Alquiler existente = alquilerRepository.findAlquilerByMatricula(alquiler.getMatriculaEmbarcacion());
        if (existente != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Ya existe un alquiler para la embarcacion con matricula: " + alquiler.getMatriculaEmbarcacion());
        }

        boolean ok = alquilerRepository.addAlquiler(alquiler);
        if ( !ok) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No se ha podido crear el alquiler en la base de datos.");
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(alquiler);
        }
        
    }

    // Vincular socio (nuevo) a un alquiler
    @PatchMapping(path="/vincular_socio/{id}", consumes = "application/json")
    public ResponseEntity<?> vincularSocio(@PathVariable Integer id ,@RequestBody Socio socio){

        if(!socioRepository.existsSocioByDni(socio.getDni())){
            boolean ok = socioRepository.addSocio(socio);
            if ( !ok ) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No se ha podido añadir el socio con DNI : " + socio.getDni() + " para la realizacion del alquiler");
            }
        }

        Alquiler alquiler = alquilerRepository.findAlquilerById(id);

        alquiler.setDniTitular(socio.getDni());

        boolean ok2 = alquilerRepository.updateAlquiler(alquiler);
        if ( !ok2 ) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No se ha podido actualizar el socio (" + socio.getDni() + ") para la actualizacion del alquiler " + id);
        } else {
            return ResponseEntity.ok("Se ha vinculado el nuevo socio con DNI: " + socio.getDni() + " al alquiler con id: " + id);
        }
    }

    // Desvincular socio (nuevo) a un alquiler
    @PatchMapping(path="/desvincular_socio/{id}")
    public ResponseEntity<?> desvincularSocio(@PathVariable Integer id){

        Alquiler alquiler = alquilerRepository.findAlquilerById(id);

        String dni = alquiler.getDniTitular();
        alquiler.setDniTitular(null);

     

        boolean ok2 = alquilerRepository.updateAlquiler(alquiler);
        if ( !ok2 ) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No se ha podido desvincular el socio (" + alquiler.getDniTitular() + ") del alquiler " + id);
        } else {
            return ResponseEntity.ok("Se ha desvinculado el socio con DNI: " + dni + " al alquiler con id: " + id);
        }
    }

    @DeleteMapping("/delete_alquiler_no_iniciado/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSocioByDni(@PathVariable Integer id) {
        Alquiler alq = alquilerRepository.findAlquilerById(id);
        if(alq != null){
            if(LocalDate.now().isBefore(alq.getFechaInicio())){
                alquilerRepository.deleteAlquiler(id);
            }
        }
    }



}
