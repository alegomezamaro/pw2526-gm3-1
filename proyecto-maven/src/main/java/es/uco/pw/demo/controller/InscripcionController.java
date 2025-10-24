package es.uco.pw.demo.controller;

import es.uco.pw.demo.model.Inscripcion;
import es.uco.pw.demo.model.InscripcionRepository;
import es.uco.pw.demo.model.InscripcionType;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/inscripciones")
public class InscripcionController {

    private final InscripcionRepository inscripcionRepository;

    public InscripcionController(InscripcionRepository inscripcionRepository) {
        this.inscripcionRepository = inscripcionRepository;
    }

    // GET /inscripciones -> listar todas
    @GetMapping
    public ResponseEntity<List<Inscripcion>> listAll() {
        List<Inscripcion> list = inscripcionRepository.findAllInscripciones();
        return ResponseEntity.ok(list);
    }

    // POST /inscripciones/add -> alta de inscripción
    @PostMapping("/add")
    public ResponseEntity<String> addInscripcion(
            @RequestParam(value = "id") int id,
            @RequestParam(value = "type") String type,            // p.ej. ANUAL | MENSUAL...
            @RequestParam(value = "yearFee") int yearFee,
            @RequestParam(value = "socioTitular") int socioTitular,
            @RequestParam(value = "date") String dateStr,         // yyyy-MM-dd
            @RequestParam(value = "familiaId") int familiaId
    ) {
        try {
            LocalDate date = LocalDate.parse(dateStr);
            Inscripcion ins = new Inscripcion();
            ins.setId(id);
            ins.setType(InscripcionType.valueOf(type.toUpperCase()));
            ins.setYearFee(yearFee);
            ins.setSocioTitular(socioTitular);
            ins.setDate(date);
            ins.setFamiliaId(familiaId);

            boolean ok = inscripcionRepository.addInscripcion(ins);
            if (ok) return ResponseEntity.ok("Inscripción creada correctamente");
            return ResponseEntity.badRequest().body("No se pudo crear la inscripción");

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Tipo de inscripción inválido: " + type);
        } catch (DataAccessException dae) {
            return ResponseEntity.status(500).body("Error de base de datos al crear inscripción");
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Parámetros inválidos: " + ex.getMessage());
        }
    }
}
