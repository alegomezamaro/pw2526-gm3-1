package es.uco.pw.demo.controller.api;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import es.uco.pw.demo.model.Familia;
import es.uco.pw.demo.model.FamiliaRepository;
import es.uco.pw.demo.model.Socio;
import es.uco.pw.demo.model.SocioRepository;

@RestController
@RequestMapping("/api/familias")
public class FamiliaApiController {

    private final FamiliaRepository familiaRepository;
    private final SocioRepository socioRepository;

    public FamiliaApiController(FamiliaRepository familiaRepository, SocioRepository socioRepository) {
        this.familiaRepository = familiaRepository;
        this.socioRepository = socioRepository;
    }

    // 1. GET ALL
    @GetMapping
    public ResponseEntity<List<Familia>> getAllFamilias() {
        return ResponseEntity.ok(familiaRepository.findAllFamilias());
    }

    // 2. GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getFamiliaById(@PathVariable Integer id) {
        Familia familia = familiaRepository.findFamiliaById(id);
        if (familia == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(familia);
    }

    // 3. CREATE (POST)
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> createFamilia(@RequestBody Familia familia) {
        if (familia == null || familia.getDniTitular() == null) {
            return ResponseEntity.badRequest().body("El DNI del titular es obligatorio.");
        }
        if (!socioRepository.existsSocioByDni(familia.getDniTitular())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe socio con ese DNI.");
        }

        try {
            familiaRepository.addFamilia(familia);

            // Recuperar el ID generado buscando por el titular (estrategia segura)
            List<Familia> todas = familiaRepository.findAllFamilias();
            Familia creada = null;
            int maxId = -1;
            for (Familia f : todas) {
                if (f.getDniTitular().equalsIgnoreCase(familia.getDniTitular())) {
                    if (f.getId() > maxId) {
                        maxId = f.getId();
                        creada = f;
                    }
                }
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(creada != null ? creada : familia);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear familia: " + e.getMessage());
        }
    }

    // 4. UPDATE (PATCH)
    @PatchMapping(path="/{id}", consumes="application/json")
    public ResponseEntity<?> actualizarFamilia(@PathVariable Integer id, @RequestBody Familia datos){
        Familia actual = familiaRepository.findFamiliaById(id);
        if(actual == null) return ResponseEntity.notFound().build();

        if(datos.getNumAdultos() != null && datos.getNumAdultos() >= 0) 
            actual.setNumAdultos(datos.getNumAdultos());
        
        if(datos.getNumNiños() != null && datos.getNumNiños() >= 0) 
            actual.setNumNiños(datos.getNumNiños());

        try {
            familiaRepository.updateFamilia(actual);
            return ResponseEntity.ok(actual);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al actualizar.");
        }
    }

    // 5. VINCULAR SOCIO (Incrementa contador Adultos)
    @PatchMapping(path = "/{id}/vincular", consumes = "application/json")
    public ResponseEntity<?> vincularSocio(@PathVariable Integer id, @RequestBody Map<String, String> body) {
        String dni = body.get("dni");
        if (dni == null) return ResponseEntity.badRequest().body("Falta el DNI.");

        Familia fam = familiaRepository.findFamiliaById(id);
        if (fam == null) return ResponseEntity.notFound().build();

        // Validamos que el socio existe, aunque no guardemos la relación en él (limitación BD)
        Socio socio = socioRepository.findSocioByDni(dni);
        if (socio == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Socio no encontrado.");

        try {
            // Solo actualizamos el contador de la familia
            fam.setNumAdultos(fam.getNumAdultos() + 1);
            familiaRepository.updateFamilia(fam);
            
            return ResponseEntity.ok("Socio vinculado (Contador incrementado).");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al vincular.");
        }
    }

    // 6. DESVINCULAR SOCIO (Decrementa contador Adultos)
    @PatchMapping(path = "/{id}/desvincular", consumes = "application/json")
    public ResponseEntity<?> desvincularSocio(@PathVariable Integer id, @RequestBody Map<String, String> body) {
        String dni = body.get("dni");
        if (dni == null) return ResponseEntity.badRequest().body("Falta el DNI.");

        Familia fam = familiaRepository.findFamiliaById(id);
        if (fam == null) return ResponseEntity.notFound().build();

        // Validamos que el socio existe
        if (!socioRepository.existsSocioByDni(dni)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Socio no encontrado.");
        }

        try {
            // Solo decrementamos si hay adultos
            if (fam.getNumAdultos() > 0) {
                fam.setNumAdultos(fam.getNumAdultos() - 1);
                familiaRepository.updateFamilia(fam);
                return ResponseEntity.ok("Socio desvinculado (Contador decrementado).");
            } else {
                return ResponseEntity.badRequest().body("No hay adultos para desvincular.");
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al desvincular.");
        }
    }

    // 7. DELETE
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFamilia(@PathVariable Integer id) {
        if(familiaRepository.findFamiliaById(id) != null){
            try {
                familiaRepository.deleteFamilia(id); 
            } catch (Exception e) {
                System.err.println("Error al borrar: " + e.getMessage());
            }
        }
    }
}