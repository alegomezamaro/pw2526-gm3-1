package es.uco.pw.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import es.uco.pw.demo.model.Alquiler;
import es.uco.pw.demo.model.AlquilerRepository;

@Controller
public class AlquilerController {

    private final AlquilerRepository alquilerRepository;
    private final ModelAndView modelAndView = new ModelAndView();

    public AlquilerController(AlquilerRepository alquilerRepository) {
        this.alquilerRepository = alquilerRepository;
        // Igual que en el de Student
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.alquilerRepository.setSQLQueriesFileName(sqlQueriesFileName);
    }

    // ------- VISTA: Formulario alta de alquiler -------
    @GetMapping("/addAlquiler")
    public ModelAndView getAddAlquilerView() {
        this.modelAndView.setViewName("addAlquilerView.html"); // tu plantilla del formulario
        this.modelAndView.addObject("newAlquiler", new Alquiler());
        return this.modelAndView;
    }

    // ------- ACCIÓN: Procesar alta de alquiler -------
    @PostMapping("/addAlquiler")
    public ModelAndView addAlquiler(@ModelAttribute("newAlquiler") Alquiler newAlquiler) {
        String nextPage;

        // Generar ID si corresponde (igual que hace el de Student con size()+1)
            int nextId = alquilerRepository.findAllAlquileres().size() + 1;
            newAlquiler.setId(nextId);

            boolean ok = alquilerRepository.addAlquiler(newAlquiler);
            if (ok) {
                nextPage = "addAlquilerViewSuccess.html";
            } else {
                nextPage = "addAlquilerViewFail.html";
            }

        this.modelAndView.setViewName(nextPage);
        this.modelAndView.addObject("alquiler", newAlquiler);
        return this.modelAndView;
    }

    // ------- (Opcional) VISTA: Listado de alquileres -------
    // @GetMapping("/alquileres")
    // public ModelAndView listAlquileres() {
    //     List<Alquiler> alquileres = alquilerRepository.findAllAlquileres();
    //     this.modelAndView.setViewName("listAlquileresView.html"); // tu plantilla de listado
    //     this.modelAndView.addObject("alquileres", alquileres);
    //     return this.modelAndView;
    // }
}
