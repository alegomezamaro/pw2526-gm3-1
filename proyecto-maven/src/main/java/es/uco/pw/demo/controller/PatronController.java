package es.uco.pw.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import es.uco.pw.demo.model.Patron;
import es.uco.pw.demo.model.PatronRepository;

@Controller
public class PatronController {

    private final PatronRepository patronRepository;

    public PatronController(PatronRepository patronRepository) {
        this.patronRepository = patronRepository;
        String sqlQueriesFileName = "db/sql.properties";
        this.patronRepository.setSQLQueriesFileName(sqlQueriesFileName);
    }

    // ------- VISTA: Formulario alta de patrón -------
    @GetMapping("/addPatron")
    public ModelAndView getAddPatronView() {
        ModelAndView mav = new ModelAndView("/addPatronView");
        mav.addObject("patron", new Patron (0, "", "", "", null, null)
        );
        return mav;
    }

    // ------- ACCIÓN: Procesar alta de patrón -------
        @PostMapping("/addPatron")
        public ModelAndView addPatron(@ModelAttribute("patron") Patron patron, SessionStatus status) {
            ModelAndView mav;
            boolean success = patronRepository.addPatron(patron);

            if (success) {
                mav = new ModelAndView("/addPatronViewSuccess");
                mav.addObject("patron", patron);
            } else {
                mav = new ModelAndView("/addPatronViewFail");
            }

            status.setComplete();
            return mav;
        }

    // ------- VISTA/ACCIÓN: Buscar patrón por DNI -------
    @GetMapping("/findPatronByDNI")
    public ModelAndView findPatronById(@RequestParam(name = "dni", required = false) String dni) {
        ModelAndView mav = new ModelAndView("findPatronByIdView"); // sin .html

        if (dni != null && !dni.isBlank()) {
            String dniQuery = dni.trim();

            // Si tienes repo.findByDni(dniQuery), úsalo directamente
            Patron patron = patronRepository.findAllPatrones()
                    .stream()
                    .filter(p -> {
                        Object modelDni = p.getDni(); // soporta String o numérico
                        return modelDni != null && dniQuery.equals(String.valueOf(modelDni));
                    })
                    .findFirst()
                    .orElse(null);

            if (patron == null) {
                mav.addObject("errorMessage", "No se encontró ningún patrón con DNI " + dniQuery);
            } else {
                mav.addObject("patron", patron);
            }
        }
        return mav;
    }
}