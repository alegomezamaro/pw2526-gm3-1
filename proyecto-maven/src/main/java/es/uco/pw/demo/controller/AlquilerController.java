package es.uco.pw.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import es.uco.pw.demo.model.Alquiler;
import es.uco.pw.demo.model.AlquilerRepository;

@Controller
public class AlquilerController {

    private final AlquilerRepository alquilerRepository;

    public AlquilerController(AlquilerRepository alquilerRepository) {
        this.alquilerRepository = alquilerRepository;
        // Igual que en el de Student
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.alquilerRepository.setSQLQueriesFileName(sqlQueriesFileName);
    }

    // ------- VISTA: Formulario alta de alquiler -------
    @GetMapping("/addAlquiler")
    public ModelAndView getAddAlquilerView() {
        ModelAndView mav = new ModelAndView("addAlquilerView"); // sin .html
        mav.addObject("newAlquiler", new Alquiler());
        return mav;
    }

    // ------- ACCIÓN: Procesar alta de alquiler -------
    @PostMapping("/addAlquiler")
    public ModelAndView addAlquiler(@ModelAttribute("newAlquiler") Alquiler newAlquiler) {
        ModelAndView mav = new ModelAndView();

        // Generar ID (como en Student)
        int nextId = alquilerRepository.findAllAlquileres().size() + 1;
        newAlquiler.setId(nextId);

        boolean ok = alquilerRepository.addAlquiler(newAlquiler);
        if (ok) {
            mav.setViewName("addAlquilerViewSuccess");
        } else {
            mav.setViewName("addAlquilerViewFail");
        }
        mav.addObject("alquiler", newAlquiler);
        return mav;
    }

    // ------- VISTA/ACCIÓN: Buscar alquiler por ID -------
    @GetMapping("/findAlquilerById")
    public ModelAndView findAlquilerById(
            @RequestParam(name = "id", required = false) Integer id) {

        ModelAndView mav = new ModelAndView("findAlquilerByIdView"); // sin .html

        if (id != null) {
            // Si tienes método específico:
            // Alquiler alquiler = alquilerRepository.findAlquilerById(id);
            // Si no lo tienes, puedes resolverlo desde el listado:
            Alquiler alquiler = alquilerRepository.findAllAlquileres()
                                                  .stream()
                                                  .filter(a -> a.getId() == id)
                                                  .findFirst()
                                                  .orElse(null);

            if (alquiler == null) {
                mav.addObject("errorMessage", "No se encontró ningún alquiler con ID " + id);
            } else {
                mav.addObject("alquiler", alquiler);
            }
        }
        return mav;
    }

    // // (Opcional) Listado
    // @GetMapping("/alquileres")
    // public ModelAndView listAlquileres() {
    //     ModelAndView mav = new ModelAndView("listAlquileresView");
    //     mav.addObject("alquileres", alquilerRepository.findAllAlquileres());
    //     return mav;
    // }
}
