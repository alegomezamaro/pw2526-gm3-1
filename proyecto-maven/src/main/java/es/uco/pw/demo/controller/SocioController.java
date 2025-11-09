package es.uco.pw.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import es.uco.pw.demo.model.Socio;
import es.uco.pw.demo.model.SocioRepository;

@Controller
public class SocioController {

    private final SocioRepository socioRepository;
    private final ModelAndView modelAndView = new ModelAndView();

    public SocioController(SocioRepository socioRepository) {
        this.socioRepository = socioRepository;
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.socioRepository.setSQLQueriesFileName(sqlQueriesFileName);
    }

    // ------- VISTA: Formulario alta de socio -------
    @GetMapping("/addSocio")
    public ModelAndView getAddSocioView() {
        this.modelAndView.setViewName("addSocioView.html");
        this.modelAndView.addObject("newSocio", new Socio()); // requiere constructor vacío
        return this.modelAndView;
    }

    // ------- ACCIÓN: Procesar alta de socio -------
    @PostMapping("/addSocio")
    public ModelAndView addSocio(@ModelAttribute("newSocio") Socio newSocio) {
        String nextPage;

        boolean ok = socioRepository.addSocio(newSocio);
        nextPage = ok ? "addSocioViewSuccess.html" : "addSocioViewFail.html";

        this.modelAndView.setViewName(nextPage);
        this.modelAndView.addObject("socio", newSocio);
        return this.modelAndView;
    }
}
