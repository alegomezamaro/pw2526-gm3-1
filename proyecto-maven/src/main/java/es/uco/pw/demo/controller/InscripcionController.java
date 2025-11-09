package es.uco.pw.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import es.uco.pw.demo.model.Inscripcion;
import es.uco.pw.demo.model.InscripcionRepository;

@Controller
public class InscripcionController {

    private final InscripcionRepository inscripcionRepository;

    public InscripcionController(InscripcionRepository inscripcionRepository) {
        this.inscripcionRepository = inscripcionRepository;
        // Igual que en el resto del proyecto
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
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
}
