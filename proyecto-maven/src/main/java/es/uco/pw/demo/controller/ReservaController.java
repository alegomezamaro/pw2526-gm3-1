package es.uco.pw.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import es.uco.pw.demo.model.Reserva;
import es.uco.pw.demo.model.ReservaRepository;

@Controller
public class ReservaController {

    private final ReservaRepository reservaRepository;

    public ReservaController(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
        String sqlQueriesFileName = "db/sql.properties";
        this.reservaRepository.setSQLQueriesFileName(sqlQueriesFileName);
    }

    // ------- VISTA: Formulario alta -------
    @GetMapping("/addReserva")
    public ModelAndView getAddReservaView() {
        ModelAndView mav = new ModelAndView("addReservaView"); // sin .html
        mav.addObject("newReserva", new Reserva());
        return mav;
    }

    // ------- ACCIÓN: Procesar alta -------
    @PostMapping("/addReserva")
    public ModelAndView addReserva(@ModelAttribute("newReserva") Reserva nueva) {
        ModelAndView mav = new ModelAndView();

        // ID sencillo tipo size()+1 (igual que en el resto del proyecto)
        int nextId = reservaRepository.findAllReservas().size() + 1;
        nueva.setId(nextId);

        boolean ok = reservaRepository.addReserva(nueva);
        if (ok) {
            mav.setViewName("addReservaViewSuccess");
        } else {
            mav.setViewName("addReservaViewFail");
        }
        mav.addObject("reserva", nueva);
        return mav;
    }

    // ------- VISTA/ACCIÓN: Buscar reserva por ID -------
    @GetMapping("/findReservaById")
    public ModelAndView findReservaById(
            @RequestParam(name = "id", required = false) Integer id) {

        ModelAndView mav = new ModelAndView("findReservaByIdView"); // sin .html

        if (id != null) {
            // Si tienes método directo en el repo, úsalo:
            // Reserva r = reservaRepository.findReservaById(id);

            // Alternativa: buscar en el listado
            Reserva r = reservaRepository.findAllReservas()
                                         .stream()
                                         .filter(x -> x.getId() == id)
                                         .findFirst()
                                         .orElse(null);

            if (r == null) {
                mav.addObject("errorMessage", "No se encontró ninguna reserva con ID " + id);
            } else {
                mav.addObject("reserva", r);
            }
        }
        return mav;
    }
}
