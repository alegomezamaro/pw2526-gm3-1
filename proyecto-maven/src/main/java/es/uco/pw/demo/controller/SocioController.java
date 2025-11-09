package es.uco.pw.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import es.uco.pw.demo.model.Socio;
import es.uco.pw.demo.model.SocioRepository;

@Controller
public class SocioController {

    private final SocioRepository socioRepository;

    public SocioController(SocioRepository socioRepository) {
        this.socioRepository = socioRepository;
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.socioRepository.setSQLQueriesFileName(sqlQueriesFileName);
    }

    // --- GET: Buscar socio SOLO por DNI (en tu modelo el DNI es numérico)
    @GetMapping("/findSocioById")
    public ModelAndView findSocioByDni(
            @RequestParam(name = "dni", required = false) String dni) {

        ModelAndView mav = new ModelAndView("findSocioByIdView"); // sin .html

        if (dni != null && !dni.isBlank()) {
            String dniQuery = dni.trim();

            Socio socio = socioRepository.findAllSocios()
                    .stream()
                    .filter(s -> {
                        // getDni() es Integer/int en tu modelo → comparamos como String
                        Integer dniModel = s.getDni();
                        return dniModel != null && dniQuery.equals(String.valueOf(dniModel));
                    })
                    .findFirst()
                    .orElse(null);

            if (socio == null) {
                mav.addObject("errorMessage", "No se encontró ningún socio con DNI " + dniQuery);
            } else {
                mav.addObject("socio", socio);
            }
        }
        return mav;
    }
}
