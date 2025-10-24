package es.uco.pw.demo.controller;

import es.uco.pw.demo.model.Familia;
import es.uco.pw.demo.model.FamiliaRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/familias")
public class FamiliaController {

    private final FamiliaRepository familiaRepository;

    public FamiliaController(FamiliaRepository familiaRepository) {
        this.familiaRepository = familiaRepository;
    }

    // GET /familias -> listar todas
    @GetMapping
    public ResponseEntity<List<Familia>> listAll() {
        List<Familia> list = familiaRepository.findAllFamilias();
        return ResponseEntity.ok(list);
    }

    // POST /familias/add -> alta de familia (simple: id + mainDni)
    @PostMapping("/add")
    public ResponseEntity<String> addFamilia(
            @RequestParam(value = "id") int id,
            @RequestParam(value = "mainDni") int mainDni
    ) {
        try {
            Familia f = new Familia();
            f.setId(id);
            f.setMainDni(mainDni);
            // Lista familiaDnis se gestionará en otra fase
            f.setFamiliaDnis(null);

            boolean ok = familiaRepository.addFamilia(f);
            if (ok) return ResponseEntity.ok("Familia creada correctamente");
            return ResponseEntity.badRequest().body("No se pudo crear la familia");

        } catch (DataAccessException dae) {
            return ResponseEntity.status(500).body("Error de base de datos al crear familia");
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Parámetros inválidos: " + ex.getMessage());
        }
    }
}
