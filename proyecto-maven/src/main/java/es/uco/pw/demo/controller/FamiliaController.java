package es.uco.pw.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import es.uco.pw.demo.model.Familia;
import es.uco.pw.demo.model.FamiliaRepository;

@Controller
public class FamiliaController {

    private final FamiliaRepository familiaRepository;
    private final ModelAndView modelAndView = new ModelAndView();

    public FamiliaController(FamiliaRepository familiaRepository) {
        this.familiaRepository = familiaRepository;
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.familiaRepository.setSQLQueriesFileName(sqlQueriesFileName);
    }

    // ------- VISTA: Formulario alta de familia -------
    @GetMapping("/addFamilia")
    public ModelAndView getAddFamiliaView() {
        this.modelAndView.setViewName("addFamiliaView");
        this.modelAndView.addObject("newFamilia", new Familia());
        return this.modelAndView;
    }

    // ------- ACCIÓN: Procesar alta de familia -------
    @PostMapping("/addFamilia")
    public ModelAndView addFamilia(@ModelAttribute("newFamilia") Familia newFamilia) {
        String nextPage;

        int nextId = familiaRepository.findAllFamilias().size() + 1;
        newFamilia.setId(nextId);

        boolean ok = familiaRepository.addFamilia(newFamilia);
        if (ok) {
            nextPage = "addFamiliaViewSuccess";
        } else {
            nextPage = "addFamiliaViewFail";
        }

        this.modelAndView.setViewName(nextPage);
        this.modelAndView.addObject("familia", newFamilia);
        return this.modelAndView;
    }
}
