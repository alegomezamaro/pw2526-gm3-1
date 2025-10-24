package es.uco.pw.demo.controller;

import es.uco.pw.demo.model.Patron;
import es.uco.pw.demo.model.PatronRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/patrones")
public class PatronController {

    private final PatronRepository patronRepository;

    public PatronController(PatronRepository patronRepository) {
        this.patronRepository = patronRepository;
    }

    // GET /patrones -> listar todos
    @GetMapping
    public ResponseEntity<List<Patron>> listAll() {
        List<Patron> list = patronRepository.findAllPatrones();
        return ResponseEntity.ok(list);
    }

    // POST /patrones/add -> alta de patrón
    @PostMapping("/add")
    public ResponseEntity<String> addPatron(
            @RequestParam(value = "id") int id,
            @RequestParam(value = "dni") String dni,
            @RequestParam(value = "nombre") String nombre,
            @RequestParam(value = "apellidos") String apellidos,
            @RequestParam(value = "fechaNacimiento") String fechaNacimiento,       // yyyy-MM-dd
            @RequestParam(value = "fechaTituloPatron") String fechaTituloPatron    // yyyy-MM-dd
    ) {
        try {
            LocalDate fnac = LocalDate.parse(fechaNacimiento);
            LocalDate ftit = LocalDate.parse(fechaTituloPatron);

            Patron p = new Patron(id, dni, nombre, apellidos, fnac, ftit);

            boolean ok = patronRepository.addPatron(p);
            if (ok) return ResponseEntity.ok("Patrón creado correctamente");
            return ResponseEntity.badRequest().body("No se pudo crear el patrón");
        } catch (DataAccessException dae) {
            return ResponseEntity.status(500).body("Error de base de datos al crear patrón");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Parámetros inválidos: " + e.getMessage());
        }
    }
}
