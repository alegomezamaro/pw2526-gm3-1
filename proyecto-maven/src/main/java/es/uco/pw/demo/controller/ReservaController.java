package es.uco.pw.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import es.uco.pw.demo.model.Reserva;
import es.uco.pw.demo.model.ReservaRepository;

@Controller
public class ReservaController {

    private final ReservaRepository reservaRepository;
    private final ModelAndView modelAndView = new ModelAndView();

    public ReservaController(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.reservaRepository.setSQLQueriesFileName(sqlQueriesFileName);
    }

    // ------- VISTA: Formulario alta de reserva -------
    @GetMapping("/addReserva")
    public ModelAndView getAddReservaView() {
        this.modelAndView.setViewName("addReservaView");
        this.modelAndView.addObject("newReserva", new Reserva());
        return this.modelAndView;
    }

    // ------- ACCIÓN: Procesar alta de reserva -------
    @PostMapping("/addReserva")
    public ModelAndView addReserva(@ModelAttribute("newReserva") Reserva newReserva) {
        String nextPage;

        int nextId = reservaRepository.findAllReservas().size() + 1;
        newReserva.setId(nextId);

        boolean ok = reservaRepository.addReserva(newReserva);
        nextPage = ok ? "addReservaViewSuccess" : "addReservaViewFail";

        this.modelAndView.setViewName(nextPage);
        this.modelAndView.addObject("reserva", newReserva);
        return this.modelAndView;
    }
}

