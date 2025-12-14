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
@RequestMapping("/api/familias")
public class FamiliaApiController {

    private final FamiliaRepository familiaRepository;
    private final SocioRepository socioRepository;

    public FamiliaApiController(FamiliaRepository familiaRepository, SocioRepository socioRepository) {
        this.familiaRepository = familiaRepository;
        this.socioRepository = socioRepository;
    }

    // 1. Obtener la lista completa de familias (GET)
    @GetMapping
    public ResponseEntity<List<Familia>> getAllFamilias() {
        List<Familia> familias = familiaRepository.findAllFamilias();
        return ResponseEntity.ok(familias);
    }

    // 2. Obtener familia por ID (GET)
    @GetMapping("/{id}")
    public ResponseEntity<?> getFamiliaById(@PathVariable Integer id) {
        Familia familia = familiaRepository.findFamiliaById(id);
        
        if (familia == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(familia);
    }

    // 3. Crear una nueva familia (POST)
    //    Body JSON ejemplo:
    //    {
    //      "dniTitular": "12345678A",
    //      "numAdultos": 2,
    //      "numNiños": 1
    //    }
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> createFamilia(@RequestBody Familia familia) {

        // Validaciones básicas
        if (familia == null || familia.getDniTitular() == null) {
            return ResponseEntity
                    .badRequest()
                    .body("El DNI del titular es obligatorio para crear una familia.");
        }

        // Comprobamos que el socio titular exista
        if (!socioRepository.existsSocioByDni(familia.getDniTitular())) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("No existe un socio con DNI: " + familia.getDniTitular());
        }

        // Insertar en BD
        boolean ok = familiaRepository.addFamilia(familia);
        if (!ok) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("No se ha podido registrar la familia en la base de datos.");
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(familia);
    }

    // 4. Actualizar miembros de la familia (PATCH)
    //    Útil para: Vincular/Desvincular miembros (cambiando los contadores)
    //    Body JSON: { "numAdultos": 3 } o { "numNiños": 2 }
    @PatchMapping(path="/{id}", consumes="application/json")
    public ResponseEntity<?> actualizarFamilia(@PathVariable Integer id, @RequestBody Familia datosParciales){
        
        Familia familiaActual = familiaRepository.findFamiliaById(id);

        if(familiaActual == null){ 
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe la familia con ID: " + id);
        }

        // Actualizamos solo los campos que vengan en el JSON
        if(datosParciales.getNumAdultos() != null && datosParciales.getNumAdultos() >= 0){ 
            familiaActual.setNumAdultos(datosParciales.getNumAdultos());
        }
        
        if(datosParciales.getNumNiños() != null && datosParciales.getNumNiños() >= 0){ 
            familiaActual.setNumNiños(datosParciales.getNumNiños());
        }

        boolean ok = familiaRepository.updateFamilia(familiaActual);
        
        if (!ok) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("No se ha podido actualizar la familia con ID: " + id);
        } else {
            return ResponseEntity.ok(familiaActual);
        }
    }

    // 5. Eliminar familia por ID (DELETE)
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