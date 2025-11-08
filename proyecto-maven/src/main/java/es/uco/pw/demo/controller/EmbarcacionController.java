package es.uco.pw.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import es.uco.pw.demo.model.Embarcacion;
import es.uco.pw.demo.model.EmbarcacionRepository;

@Controller
public class EmbarcacionController {

    private final EmbarcacionRepository embarcacionRepository;
    private final ModelAndView modelAndView = new ModelAndView();

    public EmbarcacionController(EmbarcacionRepository embarcacionRepository) {
        this.embarcacionRepository = embarcacionRepository;
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.embarcacionRepository.setSQLQueriesFileName(sqlQueriesFileName);
    }

    
    @GetMapping("/addEmbarcacion")
    public ModelAndView getAddEmbarcacionView() {
        this.modelAndView.setViewName("addEmbarcacionView.html");
        this.modelAndView.addObject("newEmbarcacion", new Embarcacion());
        return this.modelAndView;
    }
    
    @PostMapping("/addEmbarcacion")
    public ModelAndView addEmbarcacion(@ModelAttribute("newEmbarcacion") Embarcacion newEmbarcacion) {
        String nextPage;

            boolean ok = embarcacionRepository.addEmbarcacion(newEmbarcacion);
            if (ok) {
                nextPage = "addEmbarcacionViewSuccess.html";
            } else {
                nextPage = "addEmbarcacionViewFail.html";
            }

        this.modelAndView.setViewName(nextPage);
        this.modelAndView.addObject("embarcacion", newEmbarcacion);
        return this.modelAndView;
    }
}

