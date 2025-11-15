package es.uco.pw.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.uco.pw.demo.model.Embarcacion;
import es.uco.pw.demo.model.EmbarcacionRepository;
import es.uco.pw.demo.model.PatronRepository;


@Controller
public class EmbarcacionController {

    private final EmbarcacionRepository embarcacionRepository;
    private final PatronRepository patronRepository;

    public EmbarcacionController(EmbarcacionRepository embarcacionRepository, PatronRepository patronRepository) {
        this.embarcacionRepository = embarcacionRepository;
        this.patronRepository = patronRepository;
        // Igual que en otros controladores del proyecto
        String sqlQueriesFileName = "db/sql.properties";
        this.embarcacionRepository.setSQLQueriesFileName(sqlQueriesFileName);
        this.patronRepository.setSQLQueriesFileName(sqlQueriesFileName);
        
    }

    // ------- VISTA: Formulario alta -------
    @GetMapping("/addEmbarcacion")
    public ModelAndView getAddEmbarcacionView() {
        ModelAndView mav = new ModelAndView("addEmbarcacionView");
        mav.addObject("newEmbarcacion", new Embarcacion());
        return mav;
    }

    // ------- ACCIÓN: Procesar alta -------
    @PostMapping("/addEmbarcacion")
    public ModelAndView addEmbarcacion(@ModelAttribute("newEmbarcacion") Embarcacion nueva) {
        ModelAndView mav = new ModelAndView();

        // Si la repo devuelve boolean como en alquiler:
        boolean ok = embarcacionRepository.addEmbarcacion(nueva);

        if (ok) {
            mav.setViewName("addEmbarcacionViewSuccess");
        } else {
            mav.setViewName("addEmbarcacionViewFail");
        }
        mav.addObject("embarcacion", nueva);
        return mav;
    }

    // ------- VISTA/ACCIÓN: Buscar por matrícula -------
    @GetMapping("/findEmbarcacionById")
    public ModelAndView findEmbarcacionById(
            @RequestParam(name = "matricula", required = false) String matricula) {

        ModelAndView mav = new ModelAndView("findEmbarcacionByIdView");

        if (matricula != null && !matricula.isBlank()) {
            // Si tienes método directo:
            // Embarcacion e = embarcacionRepository.findByMatricula(matricula);

            // Alternativa: buscar en el listado si no existe método directo
            Embarcacion e = embarcacionRepository.findAllEmbarcaciones()
                    .stream()
                    .filter(x -> matricula.equalsIgnoreCase(x.getMatricula()))
                    .findFirst()
                    .orElse(null);

            if (e == null) {
                mav.addObject("errorMessage", "No se encontró ninguna embarcación con matrícula " + matricula);
            } else {
                mav.addObject("embarcacion", e);
            }
        }
        return mav;
    }

    // ------- VISTA: Buscar embarcaciones disponibles -------
    @GetMapping("/buscarEmbarcacionDisponible")
    public ModelAndView buscarEmbarcacionDisponible() {
        ModelAndView mav = new ModelAndView("buscarEmbarcacionDisponible");
        mav.addObject("embarcaciones", embarcacionRepository.findEmbarcacionesDisponibles());
        return mav;
    }

    @GetMapping("/consultarEmbarcacionesPorTipo")
    public ModelAndView consultarPorTipo(@RequestParam(name = "tipo", required = false) String tipo) {
        ModelAndView mav = new ModelAndView("consultarEmbarcacionesPorTipo");

        if (tipo != null && !tipo.isBlank()) {
            List<Embarcacion> embarcaciones = embarcacionRepository
                    .findAllEmbarcaciones()
                    .stream()
                    .filter(e -> e.getTipo().toString().equalsIgnoreCase(tipo))
                    .toList();
            mav.addObject("embarcaciones", embarcaciones);
            mav.addObject("tipoSeleccionado", tipo);
        }

        return mav;
    }  

    @GetMapping("/asignarPatron")
    public ModelAndView getAsignarPatronView() {
        ModelAndView mav = new ModelAndView("asignarPatronEmbarcacionView");
        mav.addObject("embarcaciones",embarcacionRepository.findAllEmbarcaciones());
        mav.addObject("patrones",patronRepository.findAllPatrones());
        return mav;
    }

    @PostMapping("/asignarPatron")
    public ModelAndView asignarPatron(
        @RequestParam("matricula") String matricula,
        @RequestParam(name = "idPatron", required = false) Integer idPatron,
        RedirectAttributes redirectAttrs) {

            boolean ok = embarcacionRepository.updatePatronAsignado(matricula, idPatron);

            if(ok){
                redirectAttrs.addFlashAttribute("successMessage", "Patrón asignado con éxito");
            }else{
                redirectAttrs.addFlashAttribute("errorMessage", "Ha ocurrido un error y no se pudo asignar el patrón");
            }

            return new ModelAndView("redirect:/asignarPatron");
    }
    
    


}
