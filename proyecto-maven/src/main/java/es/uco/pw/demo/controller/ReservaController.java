package es.uco.pw.demo.controller;

import es.uco.pw.demo.model.Reserva;
import es.uco.pw.demo.model.ReservaRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    private final ReservaRepository reservaRepository;

    public ReservaController(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    // GET /reservas -> listar todas
    @GetMapping
    public ResponseEntity<List<Reserva>> listAll() {
        List<Reserva> list = reservaRepository.findAllReservas();
        return ResponseEntity.ok(list);
    }

    // POST /reservas/add -> alta de reserva
    @PostMapping("/add")
    public ResponseEntity<String> addReserva(
            @RequestParam(value = "id") int id,
            @RequestParam(value = "embarcacionMatricula") String embarcacionMatricula,
            @RequestParam(value = "descripcion") String descripcion,
            @RequestParam(value = "socioDni") String socioDni,
            @RequestParam(value = "plazasReserva") int plazasReserva,
            @RequestParam(value = "fechaReserva") String fechaReserva,  // yyyy-MM-dd
            @RequestParam(value = "precioReserva") double precioReserva
    ) {
        try {
            LocalDate fecha = LocalDate.parse(fechaReserva);

            Reserva r = new Reserva(
                    id,
                    null,               // embarcación no hidratada
                    descripcion,
                    null,               // socioSolicitante no hidratado
                    plazasReserva,
                    fecha,
                    precioReserva
            );

            boolean ok = reservaRepository.addReserva(r);
            if (ok) return ResponseEntity.ok("Reserva creada correctamente");
            return ResponseEntity.badRequest().body("No se pudo crear la reserva");

        } catch (DataAccessException dae) {
            return ResponseEntity.status(500).body("Error de base de datos al crear reserva");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Parámetros inválidos: " + e.getMessage());
        }
    }
}

