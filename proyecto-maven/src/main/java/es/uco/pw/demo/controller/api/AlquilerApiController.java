package es.uco.pw.demo.controller.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.uco.pw.demo.model.Alquiler;
import es.uco.pw.demo.model.AlquilerRepository;
import es.uco.pw.demo.model.EmbarcacionType;




@RestController
@RequestMapping("api/alquileres")

public class AlquilerApiController {
    public final AlquilerRepository alquilerRepository;

    public AlquilerApiController(AlquilerRepository alquilerRepository){
        this.alquilerRepository = alquilerRepository;
    }

    @GetMapping
    public ResponseEntity<List<Alquiler>> getAllAlquileres() {
        List<Alquiler> alquileres = alquilerRepository.findAllAlquileres();
        return  ResponseEntity.ok(alquileres);
    }
    
    @GetMapping("/tipo={tipo}")
    public ResponseEntity<List<Alquiler>> getAlquilerByEmbarcacion(@PathVariable EmbarcacionType tipo) {
        List<Alquiler> alquileres = alquilerRepository.findAlquilerByEmbarcacionType(tipo);
        return  ResponseEntity.ok(alquileres);
    }

    @GetMapping("/id={id}")
    public ResponseEntity<Alquiler> getAlquilerById(@PathVariable Integer id) {
        Alquiler alquiler = alquilerRepository.findAlquilerById(id);
        if (alquiler != null) {
            return ResponseEntity.ok(alquiler);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    






}
