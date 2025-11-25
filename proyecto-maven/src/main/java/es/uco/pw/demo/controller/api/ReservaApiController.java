package es.uco.pw.demo.controller.api;

import es.uco.pw.demo.model.Reserva;
import es.uco.pw.demo.model.ReservaRepository;
import es.uco.pw.demo.model.Embarcacion;
import es.uco.pw.demo.model.EmbarcacionRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reservas")
public class ReservaApiController {

    private final ReservaRepository reservaRepository;
    private final EmbarcacionRepository embarcacionRepository;

    public ReservaApiController(ReservaRepository reservaRepository,
                                EmbarcacionRepository embarcacionRepository) {
        this.reservaRepository = reservaRepository;
        this.embarcacionRepository = embarcacionRepository;
        this.reservaRepository.setSQLQueriesFileName("db/sql.properties");
        this.embarcacionRepository.setSQLQueriesFileName("db/sql.properties");
    }

    // ---------------------------------------------------------------------
    // 1. OBTENER TODAS LAS RESERVAS (GET)
    // ---------------------------------------------------------------------
    @GetMapping
    public ResponseEntity<List<Reserva>> getAllReservas() {
        List<Reserva> reservas = reservaRepository.findAllReservas();
        return ResponseEntity.ok(reservas);
    }

    // ---------------------------------------------------------------------
    // 2. OBTENER RESERVAS FUTURAS SEGÚN FECHA (GET)
    // ---------------------------------------------------------------------
    // Ejemplo: /api/reservas/futuras?fecha=2025-01-01
    @GetMapping("/futuras")
    public ResponseEntity<List<Reserva>> getReservasFuturas(
            @RequestParam("fecha") String fecha) {

        LocalDate fechaInicio = LocalDate.parse(fecha);

        List<Reserva> todas = reservaRepository.findAllReservas();

        List<Reserva> futuras = todas.stream()
                .filter(r -> r.getFechaReserva() != null &&
                             !r.getFechaReserva().isBefore(fechaInicio))
                .collect(Collectors.toList());

        return ResponseEntity.ok(futuras);
    }

    // ---------------------------------------------------------------------
    // 3. OBTENER RESERVA POR ID (GET)
    // ---------------------------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<?> getReservaById(@PathVariable("id") int id) {
        List<Reserva> todas = reservaRepository.findAllReservas();

        Reserva r = todas.stream()
                .filter(res -> res.getId() == id)
                .findFirst()
                .orElse(null);

        if (r == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No existe una reserva con ID " + id);

        return ResponseEntity.ok(r);
    }

    // ---------------------------------------------------------------------
    // 4. CREAR RESERVA SI EMBARCACIÓN ESTÁ DISPONIBLE (POST)
    // ---------------------------------------------------------------------
    // BODY JSON:
    // {
    //   "matriculaEmbarcacion": "ABC123",
    //   "plazasReserva": 5,
    //   "fechaReserva": "2025-01-20"
    // }
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> createReserva(@RequestBody Reserva reserva) {

        // 1. GENERAR ID AUTOMÁTICO SIN PEDIR NADA AL CLIENTE
        List<Reserva> existentes = reservaRepository.findAllReservas();
        int nuevoId = existentes.stream()
            .mapToInt(Reserva::getId)
            .max()
            .orElse(0) + 1;

            reserva.setId(nuevoId);

            
        // ------------------------------
        // Validaciones básicas
        // ------------------------------
        if (reserva.getMatriculaEmbarcacion() == null)
            return ResponseEntity.badRequest().body("La matrícula es obligatoria.");

        if (reserva.getPlazasReserva() <= 0)
            return ResponseEntity.badRequest().body("Plazas inválidas.");

        if (reserva.getFechaReserva() == null)
            return ResponseEntity.badRequest().body("La fecha es obligatoria.");

        // ------------------------------
        // 1. Comprobar que la embarcación existe
        // ------------------------------
        List<Embarcacion> embarcaciones = embarcacionRepository.findAllEmbarcaciones();

        Embarcacion emb = embarcaciones.stream()
                .filter(e -> e.getMatricula().equalsIgnoreCase(reserva.getMatriculaEmbarcacion()))
                .findFirst()
                .orElse(null);

        if (emb == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No existe ninguna embarcación con matrícula: " +
                          reserva.getMatriculaEmbarcacion());
        }

        // ------------------------------
        // 2. Debe tener patrón asignado
        // ------------------------------
        if (emb.getPatronAsignado() == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("La embarcación no tiene patrón y no se puede reservar.");
        }

        // ------------------------------
        // 3. Comprobar disponibilidad ese día
        // ------------------------------
        List<Reserva> reservasExistentes = reservaRepository.findAllReservas();

        boolean ocupada = reservasExistentes.stream().anyMatch(r ->
                r.getMatriculaEmbarcacion().equalsIgnoreCase(reserva.getMatriculaEmbarcacion()) &&
                r.getFechaReserva().equals(reserva.getFechaReserva())
        );

        if (ocupada) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("La embarcación está ocupada ese día.");
        }

        // ------------------------------
        // 4. Comprobar plazas
        // (embarcación de reservas RESTA 1 plaza por el patrón)
        // ------------------------------
        int plazasDisponibles = emb.getPlazas() - 1;

        if (reserva.getPlazasReserva() > plazasDisponibles) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("No hay suficientes plazas. La embarcación tiene "
                          + plazasDisponibles + " plazas disponibles.");
        }

        // ------------------------------
        // 5. Calcular precio (40€/persona)
        // ------------------------------
        double precio = reserva.getPlazasReserva() * 40.0;
        reserva.setPrecioReserva(precio);

        // ------------------------------
        // 6. Insertar en BD
        // ------------------------------
        boolean ok = reservaRepository.addReserva(reserva);

        if (!ok) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al guardar la reserva en la base de datos.");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(reserva);
    }
}
