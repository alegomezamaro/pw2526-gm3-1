package es.uco.pw.demo.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import es.uco.pw.demo.model.Familia;
import es.uco.pw.demo.model.FamiliaRepository;

@Controller
public class FamiliaController {

    private final FamiliaRepository familiaRepository;

    public FamiliaController(FamiliaRepository familiaRepository) {
        this.familiaRepository = familiaRepository;
        // Igual que en el resto del proyecto:
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.familiaRepository.setSQLQueriesFileName(sqlQueriesFileName);
    }

    // ------- VISTA/ACCIÓN: Buscar familia por ID -------
    @GetMapping("/findFamiliaById")
    public ModelAndView findFamiliaById(
            @RequestParam(name = "id", required = false) Integer id) {

        ModelAndView mav = new ModelAndView("findFamiliaByIdView"); // sin .html

        if (id != null) {
            // Si existe un método directo:
            // Familia f = familiaRepository.findFamiliaById(id);

            // Alternativa si solo hay método de listado:
            Familia f = familiaRepository.findAllFamilias()
                                         .stream()
                                         .filter(x -> x.getId().equals(id))
                                         .findFirst()
                                         .orElse(null);

            if (f == null) {
                mav.addObject("errorMessage", "No se encontró ninguna familia con ID " + id);
            } else {
                mav.addObject("familia", f);
            }
        }
        return mav;
    }

        // ------- VISTA: Formulario alta -------
    @GetMapping("/addFamilia")
    public ModelAndView getAddFamiliaView() {
        ModelAndView mav = new ModelAndView("addFamiliaView");
        mav.addObject("newFamilia", new Familia());
        return mav;
    }

    // ------- ACCIÓN: Procesar alta -------
    @PostMapping("/addFamilia")
    public ModelAndView addFamilia(@ModelAttribute("newFamilia") Familia newFamilia) {
        ModelAndView mav = new ModelAndView();


        try {

            familiaRepository.addFamilia(newFamilia);
            mav.setViewName("addFamiliaViewSuccess");

        }catch(DataIntegrityViolationException e){

            mav.setViewName("addFamiliaViewFail");
            mav.addObject("error", "El DNI introducido no esta asociado con ningún socio actual.");
        } 
        catch (Exception e) {
            mav.setViewName("addFamiliaViewFail");
        }

        mav.addObject("Familia", newFamilia);
        return mav;
    }

}
