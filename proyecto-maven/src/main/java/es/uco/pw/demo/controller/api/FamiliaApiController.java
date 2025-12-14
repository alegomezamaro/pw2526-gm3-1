package es.uco.pw.demo.controller.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import es.uco.pw.demo.model.Familia;
import es.uco.pw.demo.model.FamiliaRepository;
import es.uco.pw.demo.model.SocioRepository;

@RestController
@RequestMapping("/api/socios")
public class SocioApiController {
    private final SocioRepository socioRepository;
    private final InscripcionRepository inscripcionRepository;
    private final FamiliaRepository familiaRepository; // Nuevo repositorio

    // 1. Actualizar constructor para inyectar FamiliaRepository
    public SocioApiController(SocioRepository socioRepository, 
                              InscripcionRepository inscripcionRepository,
                              FamiliaRepository familiaRepository) {
        this.socioRepository = socioRepository;
        this.inscripcionRepository = inscripcionRepository;
        this.familiaRepository = familiaRepository;
    }

    // ... [MANTENER LOS MÉTODOS GET, POST, PUT EXISTENTES] ...

    // 2. Vincular socio a inscripción familiar (PATCH)
    @PatchMapping(path = "/{dni}/vincular-inscripcion", consumes = "application/json")
    public ResponseEntity<?> vincularSocioInscripcionF(@PathVariable String dni, @RequestBody Integer idInscripcion) {
        
        // Validar Socio
        Socio socio = socioRepository.findSocioByDni(dni);
        if (socio == null) return ResponseEntity.notFound().build();
        if (socio.getFamiliaId() != null) {
            return ResponseEntity.badRequest().body("El socio ya pertenece a una familia.");
        }

        // Validar Inscripción
        Inscripcion inscripcion = inscripcionRepository.findInscripcionById(idInscripcion);
        if (inscripcion == null) {
            return ResponseEntity.badRequest().body("Inscripción no encontrada.");
        }
        if (inscripcion.getTipoCuota() != InscripcionType.FAMILIAR || inscripcion.getFamiliaId() == null) {
            return ResponseEntity.badRequest().body("La inscripción no es familiar.");
        }

        // Obtener Familia y Actualizar
        Integer famId = inscripcion.getFamiliaId();
        Familia familia = familiaRepository.findFamiliaById(famId);
        if (familia == null) return ResponseEntity.internalServerError().body("Familia no encontrada.");

        // Actualizar Socio
        socio.setFamiliaId(famId);
        boolean socioOk = socioRepository.updateSocio(socio);

        // Actualizar contador Familia (Asumimos adulto por defecto, o ajustar según lógica)
        familia.setNumAdultos(familia.getNumAdultos() + 1);
        boolean famOk = familiaRepository.updateFamilia(familia);

        if (!socioOk || !famOk) {
            return ResponseEntity.internalServerError().body("Error al actualizar datos en BD.");
        }

        return ResponseEntity.ok("Socio vinculado correctamente a la familia " + famId);
    }

    // 3. Desvincular socio de inscripción familiar (PATCH)
    @PatchMapping(path = "/{dni}/desvincular-familia")
    public ResponseEntity<?> desvincularSocioFamilia(@PathVariable String dni) {
        
        Socio socio = socioRepository.findSocioByDni(dni);
        if (socio == null) return ResponseEntity.notFound().build();

        Integer famId = socio.getFamiliaId();
        if (famId == null) {
            return ResponseEntity.badRequest().body("El socio no está vinculado a ninguna familia.");
        }

        // Obtener Familia para restar miembro
        Familia familia = familiaRepository.findFamiliaById(famId);
        if (familia != null) {
            // Evitar números negativos
            if (familia.getNumAdultos() > 0) {
                familia.setNumAdultos(familia.getNumAdultos() - 1);
                familiaRepository.updateFamilia(familia);
            }
        }

        // Desvincular Socio (sin borrarlo)
        socio.setFamiliaId(null);
        boolean ok = socioRepository.updateSocio(socio);

        if (!ok) return ResponseEntity.internalServerError().body("Error al desvincular socio.");

        return ResponseEntity.ok("Socio desvinculado correctamente.");
    }

    // 4. Eliminar familia por ID (DELETE)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFamilia(@PathVariable Integer id) {
        Familia familia = familiaRepository.findFamiliaById(id);
        
        if(familia != null){
            // Utilizamos la query definida en sql.properties para borrar por ID
            // Nota: Asegúrate de que tu repositorio tenga implementado deleteFamiliaresById usando esa query
            familiaRepository.deleteFamiliaresById(id);
        }
    }
}