package es.uco.pw.demo.controller;

import es.uco.pw.demo.model.Alquiler;
import es.uco.pw.demo.model.AlquilerRepository;
import es.uco.pw.demo.model.Embarcacion;
import es.uco.pw.demo.model.EmbarcacionRepository;
import es.uco.pw.demo.model.Socio;
import es.uco.pw.demo.model.SocioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.support.SessionStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@SessionAttributes("alquiler")
public class AddAlquilerController {

    @Autowired private AlquilerRepository alquilerRepository;
    @Autowired private SocioRepository socioRepository;
    @Autowired private EmbarcacionRepository embarcacionRepository;

    // ------- HELPERS: buscar en memoria a partir de las listas --------
    private Embarcacion getEmbarcacionByMatricula(String matricula) {
        for (Embarcacion e : embarcacionRepository.findAllEmbarcaciones()) {
            if (e != null && e.getMatricula() != null && e.getMatricula().equals(matricula)) return e;
        }
        return null;
    }

    private Socio getSocioByDni(Integer dni) {
        if (dni == null) return null;
        for (Socio s : socioRepository.findAllSocios()) {
            if (s != null && s.getDni() == dni) return s;
        }
        return null;
    }
    // -------------------------------------------------------------------

    @GetMapping("/alquiler/nuevo")
    public ModelAndView showForm() {
        ModelAndView mav = new ModelAndView("alquiler/addAlquilerView");
        mav.addObject("alquiler", new Alquiler()); // tu Alquiler tiene ctor vacío
        // Para poblar selects en la vista
        mav.addObject("socios", socioRepository.findAllSocios());
        mav.addObject("embarcaciones", embarcacionRepository.findAllEmbarcaciones());
        return mav;
    }

    @PostMapping("/alquiler/nuevo")
    public ModelAndView addAlquiler(
            @RequestParam("matriculaEmbarcacion") String matriculaEmbarcacion,
            @RequestParam("dniTitular") Integer dniTitular,
            @RequestParam(value = "participantes", required = false) List<Integer> dnisParticipantes,
            @RequestParam("fechaInicio") String fechaInicioStr,
            @RequestParam("fechaFin") String fechaFinStr,
            @RequestParam("plazasSolicitadas") Integer plazasSolicitadas,
            @RequestParam("precioTotal") Double precioTotal,
            SessionStatus status
    ) {
        Embarcacion embarcacion = getEmbarcacionByMatricula(matriculaEmbarcacion);
        Socio socioTitular = getSocioByDni(dniTitular);

        List<Socio> participantes = new ArrayList<>();
        if (dnisParticipantes != null) {
            for (Integer dni : dnisParticipantes) {
                Socio s = getSocioByDni(dni);
                if (s != null) participantes.add(s);
            }
        }

        Alquiler alquiler = new Alquiler();
        alquiler.setEmbarcacion(embarcacion);
        alquiler.setSocioTitular(socioTitular);
        alquiler.setParticipantes(participantes);
        alquiler.setFechaInicio(LocalDate.parse(fechaInicioStr)); // YYYY-MM-DD
        alquiler.setFechaFin(LocalDate.parse(fechaFinStr));
        alquiler.setPlazasSolicitadas(plazasSolicitadas);
        alquiler.setPrecioTotal(precioTotal);

        boolean success = alquilerRepository.addAlquiler(alquiler); // <-- nombre correcto

        ModelAndView mav = success
                ? new ModelAndView("alquiler/addAlquilerViewSuccess")
                : new ModelAndView("alquiler/addAlquilerViewFail");

        if (success) mav.addObject("alquiler", alquiler);

        status.setComplete();
        return mav;
    }

    // Necesario por @SessionAttributes("alquiler")
    @ModelAttribute("alquiler")
    public Alquiler alquilerAttr() { return new Alquiler(); }
}
