package es.uco.pw.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import es.uco.pw.demo.model.Inscripcion;
import es.uco.pw.demo.model.InscripcionRepository;


@Controller
public class InscripcionController {

    private final InscripcionRepository inscripcionRepository;
    private final ModelAndView modelAndView = new ModelAndView();

    public InscripcionController(InscripcionRepository InscripcionRepository) {
        this.inscripcionRepository = InscripcionRepository;
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.inscripcionRepository.setSQLQueriesFileName(sqlQueriesFileName);
    }

    
    @GetMapping("/addInscripcion")
    public ModelAndView getAddInscripcionView() {
        this.modelAndView.setViewName("addInscripcionView");
        this.modelAndView.addObject("newInscripcion", new Inscripcion());
        return this.modelAndView;
    }
    
    @PostMapping("/addInscripcion")
    public ModelAndView addInscripcion(@ModelAttribute("newInscripcion") Inscripcion newInscripcion) {
        String nextPage;
            
            int nextId = inscripcionRepository.findAllInscripciones().size() + 1;
            newInscripcion.setId(nextId);

            boolean ok = inscripcionRepository.addInscripcion(newInscripcion);
            if (ok) {
                nextPage = "addInscripcionViewSuccess";
            } else {
                nextPage = "addInscripcionViewFail";
            }

        this.modelAndView.setViewName(nextPage);
        this.modelAndView.addObject("newInscripcion", newInscripcion);
        return this.modelAndView;
    }
}