package es.uco.pw.demo.controller.api;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.uco.pw.demo.model.Alquiler;
import es.uco.pw.demo.model.AlquilerRepository;
import es.uco.pw.demo.model.Embarcacion;
import es.uco.pw.demo.model.EmbarcacionRepository;
import es.uco.pw.demo.model.EmbarcacionType;





@RestController
@RequestMapping("api/alquileres")

public class AlquilerApiController {
    public final AlquilerRepository alquilerRepository;
    public final EmbarcacionRepository embarcacionRepository;

    public AlquilerApiController(AlquilerRepository alquilerRepository,EmbarcacionRepository embarcacionRepository){
        this.alquilerRepository = alquilerRepository;
        this.embarcacionRepository = embarcacionRepository;
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




}
