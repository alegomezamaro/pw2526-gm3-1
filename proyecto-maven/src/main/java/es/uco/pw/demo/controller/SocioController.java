package es.uco.pw.demo.controller;

import es.uco.pw.demo.model.Socio;
import es.uco.pw.demo.model.FamiliaType;
import es.uco.pw.demo.model.SocioRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/socios")
public class SocioController {

    private final SocioRepository socioRepository;

    public SocioController(SocioRepository socioRepository) {
        this.socioRepository = socioRepository;
    }

    // GET /socios -> listar todos
    @GetMapping
    public ResponseEntity<List<Socio>> listAll() {
        List<Socio> socios = socioRepository.findAllSocios();
        return ResponseEntity.ok(socios);
    }

    // POST /socios/add -> crear socio
    @PostMapping("/add")
    public ResponseEntity<String> addSocio(
            @RequestParam(value = "dni") Integer dni,
            @RequestParam(value = "name") String name,
            @RequestParam(value = "surname") String surname,
            @RequestParam(value = "birthDate") String birthDateStr,          // yyyy-MM-dd
            @RequestParam(value = "inscriptionDate") String inscriptionDateStr, // yyyy-MM-dd
            @RequestParam(value = "address") String address,
            @RequestParam(value = "patronEmbarcacion", defaultValue = "false") boolean patronEmbarcacion,
            @RequestParam(value = "inscriptionId") Integer inscriptionId,
            // Familia (opcionales esta semana)
            @RequestParam(value = "familiaId", required = false) Integer familiaId,
            @RequestParam(value = "relacionFamiliar", required = false) String relacionFamiliar // p.ej. "PADRE", "MADRE"...
    ) {
        try {
            LocalDate birthDate = LocalDate.parse(birthDateStr);
            LocalDate inscriptionDate = LocalDate.parse(inscriptionDateStr);

            Socio s;
            if (familiaId == null || relacionFamiliar == null || relacionFamiliar.isBlank()) {
                
                s = new Socio(dni, name, surname, birthDate, inscriptionDate, address, patronEmbarcacion, inscriptionId);
            } else {
                
                s = new Socio(dni, name, surname, birthDate, inscriptionDate, address, patronEmbarcacion, inscriptionId);
                s.setFamiliaId(familiaId);
                s.setRelacionFamiliar(FamiliaType.valueOf(relacionFamiliar.toUpperCase()));
            }

            boolean ok = socioRepository.addSocio(s);
            if (ok) return ResponseEntity.ok("Socio creado correctamente");
            return ResponseEntity.badRequest().body("No se pudo crear el socio");

        } catch (DataAccessException ex) {
            return ResponseEntity.status(500).body("Error de base de datos al crear socio");
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Parámetros inválidos: " + ex.getMessage());
        }
    }
}
