package es.uco.pw.demo.controller;

import es.uco.pw.demo.model.Alquiler;
import es.uco.pw.demo.model.AlquilerRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/alquileres")
public class AlquilerController {

    private final AlquilerRepository alquilerRepository;

    public AlquilerController(AlquilerRepository alquilerRepository) {
        this.alquilerRepository = alquilerRepository;
    }

    // GET /alquileres -> listar todos
    @GetMapping
    public ResponseEntity<List<Alquiler>> listAll() {
        List<Alquiler> list = alquilerRepository.findAllAlquileres();
        return ResponseEntity.ok(list);
    }

    // POST /alquileres/add -> alta de alquiler
    @PostMapping("/add")
    public ResponseEntity<String> addAlquiler(
            @RequestParam(value = "id") int id,
            @RequestParam(value = "embarcacionMatricula") String embarcacionMatricula, // se recoge pero NO se hidrata aún
            @RequestParam(value = "socioDni") Integer socioDni,                         // idem
            @RequestParam(value = "fechaInicio") String fechaInicio,                   // yyyy-MM-dd
            @RequestParam(value = "fechaFin") String fechaFin,                         // yyyy-MM-dd
            @RequestParam(value = "plazasSolicitadas") int plazasSolicitadas,
            @RequestParam(value = "precioTotal") double precioTotal
    ) {
        try {
            LocalDate fi = LocalDate.parse(fechaInicio);
            LocalDate ff = LocalDate.parse(fechaFin);

            
            Alquiler a = new Alquiler(
                    id,
                    null,        
                    null,        
                    null,        
                    fi,
                    ff,
                    plazasSolicitadas,
                    precioTotal
            );

            boolean ok = alquilerRepository.addAlquiler(a);
            if (ok) return ResponseEntity.ok("Alquiler creado correctamente");
            return ResponseEntity.badRequest().body("No se pudo crear el alquiler");

        } catch (DataAccessException dae) {
            return ResponseEntity.status(500).body("Error de base de datos al crear alquiler");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Parámetros inválidos: " + e.getMessage());
        }
    }
}

