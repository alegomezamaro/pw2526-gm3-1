package es.uco.pw.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import es.uco.pw.demo.model.Patron;
import es.uco.pw.demo.model.PatronRepository;

@Controller
public class PatronController {

    private final PatronRepository patronRepository;

    public PatronController(PatronRepository patronRepository) {
        this.patronRepository = patronRepository;
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.patronRepository.setSQLQueriesFileName(sqlQueriesFileName);
    }

    // ------- VISTA: Formulario alta de patrón -------
    @GetMapping("/addPatron")
    public ModelAndView getAddPatronView() {
        // No creamos new Patron() porque tu modelo no tiene constructor vacío
        return new ModelAndView("addPatronView"); // sin .html
    }

    // ------- ACCIÓN: Procesar alta de patrón -------
    @PostMapping("/addPatron")
    public ModelAndView addPatron(
            @RequestParam("dni") String dni,
            @RequestParam("name") String name,
            @RequestParam("surname") String surname,
            @RequestParam("license") String license,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "experience", required = false) Integer experience,
            @RequestParam(value = "embarcacion", required = false) String embarcacion
    ) {
        ModelAndView mav = new ModelAndView();

        // TODO: AJUSTA ESTE CONSTRUCTOR al que tenga tu clase Patron
        // Ejemplo típico si tu constructor es "completo":
        Patron nuevo = new Patron(
                dni, name, surname, license, phone, email, experience, embarcacion
        );

        boolean ok = patronRepository.addPatron(nuevo);
        if (ok) {
            mav.setViewName("addPatronViewSuccess");
        } else {
            mav.setViewName("addPatronViewFail");
        }
        mav.addObject("patron", nuevo);
        return mav;
    }

    // ------- VISTA/ACCIÓN: Buscar patrón por DNI -------
    @GetMapping("/findPatronById")
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
