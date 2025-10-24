package es.uco.pw.demo.controller;

import es.uco.pw.demo.model.Embarcacion;
import es.uco.pw.demo.model.EmbarcacionRepository;
import es.uco.pw.demo.model.EmbarcacionType;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/embarcaciones")
public class EmbarcacionController {

    private final EmbarcacionRepository embarcacionRepository;

    public EmbarcacionController(EmbarcacionRepository embarcacionRepository) {
        this.embarcacionRepository = embarcacionRepository;
    }

    // GET /embarcaciones -> listar todas
    @GetMapping
    public ResponseEntity<List<Embarcacion>> listAll() {
        List<Embarcacion> list = embarcacionRepository.findAllEmbarcaciones();
        return ResponseEntity.ok(list);
    }

    // POST /embarcaciones/add -> alta de embarcación
    @PostMapping("/add")
    public ResponseEntity<String> addEmbarcacion(
            @RequestParam(value = "matricula") String matricula,
            @RequestParam(value = "nombre") String nombre,
            @RequestParam(value = "tipo") String tipo,          // VELERO | YATE | CATAMARAN | LANCHA
            @RequestParam(value = "plazas") int plazas,
            @RequestParam(value = "dimensiones") String dimensiones
            // patronAsignado lo dejamos para más adelante (null)
    ) {
        try {
            Embarcacion e = new Embarcacion(
                    matricula,
                    nombre,
                    EmbarcacionType.valueOf(tipo.toUpperCase()),
                    plazas,
                    dimensiones,
                    null // patronAsignado sin hidratar en esta fase
            );

            boolean ok = embarcacionRepository.addEmbarcacion(e);
            if (ok) return ResponseEntity.ok("Embarcación creada correctamente");
            return ResponseEntity.badRequest().body("No se pudo crear la embarcación");

        } catch (IllegalArgumentException iae) {
            // valueOf del enum falló (tipo inválido)
            return ResponseEntity.badRequest().body("Tipo de embarcación inválido: " + tipo);
        } catch (DataAccessException dae) {
            return ResponseEntity.status(500).body("Error de base de datos al crear embarcación");
        }
    }
}

