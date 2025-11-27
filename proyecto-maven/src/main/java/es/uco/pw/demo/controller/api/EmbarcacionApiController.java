package es.uco.pw.demo.controller.api;

import es.uco.pw.demo.model.Embarcacion;
import es.uco.pw.demo.model.EmbarcacionRepository;
import es.uco.pw.demo.model.EmbarcacionType;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/embarcaciones")
public class EmbarcacionApiController {

    private final EmbarcacionRepository embarcacionRepository;

    public EmbarcacionApiController(EmbarcacionRepository embarcacionRepository) {
        this.embarcacionRepository = embarcacionRepository;
    }

    // 1. Obtener la lista completa de embarcaciones (GET)
    @GetMapping
    public ResponseEntity<List<Embarcacion>> getAllEmbarcaciones() {
        List<Embarcacion> embarcaciones = embarcacionRepository.findAllEmbarcaciones();
        return ResponseEntity.ok(embarcaciones);
    }

    // 2. Obtener la lista de embarcaciones según el tipo (GET)
    //    Ejemplo: GET /api/embarcaciones/tipo/VELERO
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<?> getEmbarcacionesByTipo(@PathVariable String tipo) {
        EmbarcacionType tipoEnum;
        try {
            tipoEnum = EmbarcacionType.valueOf(tipo.toUpperCase());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Tipo de embarcación inválido: " + tipo);
        }

        List<Embarcacion> todas = embarcacionRepository.findAllEmbarcaciones();
        List<Embarcacion> filtradas = todas.stream()
                .filter(e -> e.getTipo() == tipoEnum)
                .collect(Collectors.toList());

        return ResponseEntity.ok(filtradas);
    }

    // 4. Crear una nueva embarcación sin asociarle patrón (POST)
    //    Body JSON como:
    //    {
    //      "matricula": "ABC123",
    //      "nombre": "Mi barco",
    //      "tipo": "VELERO",
    //      "plazas": 6,
    //      "dimensiones": "10x3"
    //    }
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> createEmbarcacion(@RequestBody Embarcacion embarcacion) {

        if (embarcacion == null || embarcacion.getMatricula() == null) {
            return ResponseEntity
                    .badRequest()
                    .body("La matrícula de la embarcación es obligatoria.");
        }

        // No debe tener patrón asociado según el enunciado
        embarcacion.setPatronAsignado(null);

        // Comprobamos que no exista ya esa matrícula
        List<Embarcacion> existentes = embarcacionRepository.findAllEmbarcaciones();
        boolean yaExiste = existentes.stream()
                .anyMatch(e -> e.getMatricula().equalsIgnoreCase(embarcacion.getMatricula()));

        if (yaExiste) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Ya existe una embarcación con matrícula: " + embarcacion.getMatricula());
        }

        boolean ok = embarcacionRepository.addEmbarcacion(embarcacion);
        if (!ok) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("No se ha podido crear la embarcación en la base de datos.");
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(embarcacion);
    }

      @PatchMapping(path = "/{matricula}", consumes = "application/json", produces = "application/json")
         public ResponseEntity<?> patchEmbarcacion(
            @PathVariable String matricula,
            @RequestBody Embarcacion embPatch) {

        List<Embarcacion> todas = embarcacionRepository.findAllEmbarcaciones();
        Embarcacion actual = todas.stream()
                .filter(e -> e.getMatricula().equalsIgnoreCase(matricula))
                .findFirst()
                .orElse(null);

        if (actual == null) {
            return ResponseEntity.notFound().build();
        }

        // No se permite cambiar matrícula

        if (embPatch.getNombre() != null) {
            actual.setNombre(embPatch.getNombre());
        }
        if (embPatch.getTipo() != null) {
            actual.setTipo(embPatch.getTipo());
        }
        if (embPatch.getPlazas() != 0) {
            actual.setPlazas(embPatch.getPlazas());
        }
        if (embPatch.getDimensiones() != null) {
            actual.setDimensiones(embPatch.getDimensiones());
        }
        if (embPatch.getPatronAsignado() != null) {
            actual.setPatronAsignado(embPatch.getPatronAsignado());
        }

        boolean ok = embarcacionRepository.updateEmbarcacion(actual);
        if (!ok) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("No se ha podido actualizar la embarcación en la base de datos.");
        }

        return ResponseEntity.ok(actual);
    }
}
