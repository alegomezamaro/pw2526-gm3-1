package es.uco.pw.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import es.uco.pw.demo.model.Inscripcion;
import es.uco.pw.demo.model.InscripcionRepository;
import es.uco.pw.demo.model.InscripcionType;

@Controller
public class InscripcionController {

    private final InscripcionRepository inscripcionRepository;

    public InscripcionController(InscripcionRepository inscripcionRepository) {
        this.inscripcionRepository = inscripcionRepository;
        // Igual que en el resto del proyecto
        String sqlQueriesFileName = "db/sql.properties";
        this.inscripcionRepository.setSQLQueriesFileName(sqlQueriesFileName);
    }

    // ------- VISTA: Formulario alta -------
    @GetMapping("/addInscripcion")
    public ModelAndView getAddInscripcionView() {
        ModelAndView mav = new ModelAndView("addInscripcionView"); // sin .html
        mav.addObject("newInscripcion", new Inscripcion());
        return mav;
    }

    // ------- ACCIÓN: Procesar alta -------
    @PostMapping("/addInscripcion")
    public ModelAndView addInscripcion(@ModelAttribute("newInscripcion") Inscripcion nueva) {
        ModelAndView mav = new ModelAndView();

        // Generar ID si sigues el patrón size()+1
        int nextId = inscripcionRepository.findAllInscripciones().size() + 1;
        nueva.setId(nextId);

        boolean ok = inscripcionRepository.addInscripcion(nueva);
        if (ok) {
            mav.setViewName("addInscripcionViewSuccess");
        } else {
            mav.setViewName("addInscripcionViewFail");
        }
        mav.addObject("inscripcion", nueva);
        return mav;
    }

    // ------- VISTA/ACCIÓN: Buscar inscripción por ID -------
    @GetMapping("/findInscripcionById")
    public ModelAndView findInscripcionById(
            @RequestParam(name = "id", required = false) Integer id) {

        ModelAndView mav = new ModelAndView("findInscripcionByIdView"); // sin .html

        if (id != null) {
            // Si tienes método directo:
            // Inscripcion i = inscripcionRepository.findInscripcionById(id);

            // Alternativa desde el listado:
            Inscripcion i = inscripcionRepository.findAllInscripciones()
                                                 .stream()
                                                 .filter(x -> x.getId() == id)
                                                 .findFirst()
                                                 .orElse(null);

            if (i == null) {
                mav.addObject("errorMessage", "No se encontró ninguna inscripción con ID " + id);
            } else {
                mav.addObject("inscripcion", i);
            }
        }
        return mav;
    }

    // ------- VISTA: Listar inscripciones por tipo -------
    @GetMapping("/listInscripciones")
public ModelAndView listInscripciones(
        @RequestParam(name = "tipo", required = false) String tipo) {

    ModelAndView mav = new ModelAndView("listInscripcionesView");

    var todas = inscripcionRepository.findAllInscripciones();

    if (tipo != null && !tipo.isEmpty()) {
        var filtradas = todas.stream()
                .filter(i -> i.getTipoCuota() != null &&
                             i.getTipoCuota().name().equals(tipo))
                .toList();
        mav.addObject("inscripciones", filtradas);
        mav.addObject("tipoSeleccionado", tipo);
    } else {
        mav.addObject("inscripciones", todas);
        mav.addObject("tipoSeleccionado", "");
    }

    mav.addObject("tipos", InscripcionType.values());
    return mav;
}

// ------- VISTA: formulario para ampliar a familiar -------
    @GetMapping("/ampliarInscripcion")
    public ModelAndView getAmpliarInscripcionView() {
        return new ModelAndView("ampliarInscripcionView");
    }

    // ------- ACCIÓN: procesar ampliación a familiar -------
    @PostMapping("/ampliarInscripcion")
    public ModelAndView ampliarInscripcion(
            @RequestParam("id") Integer id,
            @RequestParam("familiaId") Integer familiaId) {

        ModelAndView mav = new ModelAndView();

        // 1) Buscar inscripción
        Inscripcion ins = inscripcionRepository.findInscripcionById(id);
        if (ins == null) {
            mav.setViewName("ampliarInscripcionViewFail");
            mav.addObject("errorMessage", "No se encontró ninguna inscripción con ID " + id);
            return mav;
        }

        // 2) Comprobar si ya es familiar
        if (ins.getTipoCuota() == InscripcionType.FAMILIAR) {
            mav.setViewName("ampliarInscripcionViewFail");
            mav.addObject("errorMessage", "La inscripción ya es de tipo FAMILIAR.");
            mav.addObject("inscripcion", ins);
            return mav;
        }

        // 3) Cambiar a FAMILIAR y asignar familia
        ins.setTipoCuota(InscripcionType.FAMILIAR);
        ins.setFamiliaId(familiaId);
        // Si queréis cambiar cuotaAnual aquí, lo hacéis también

        boolean ok = inscripcionRepository.updateInscripcion(ins);

        if (ok) {
            mav.setViewName("ampliarInscripcionViewSuccess");
        } else {
            mav.setViewName("ampliarInscripcionViewFail");
            mav.addObject("errorMessage", "No se pudo actualizar la inscripción.");
        }

        mav.addObject("inscripcion", ins);
        return mav;
    }


}
