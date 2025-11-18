package es.uco.pw.demo.controller;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import es.uco.pw.demo.model.Inscripcion;
import es.uco.pw.demo.model.InscripcionType;
import es.uco.pw.demo.model.InscripcionRepository;

import es.uco.pw.demo.model.Socio;
import es.uco.pw.demo.model.SocioRepository;

@Controller
public class SocioController {

    private final SocioRepository socioRepository;
    private final InscripcionRepository inscripcionRepository;

    public SocioController(SocioRepository socioRepository,
                           InscripcionRepository inscripcionRepository) {
        this.socioRepository = socioRepository;
        this.inscripcionRepository = inscripcionRepository;
    }
    // ------- VISTA: Formulario alta de socio -------
    @GetMapping("/addSocio")
    public ModelAndView getAddSocioView() {
        return new ModelAndView("addSocioView"); // nombre de la plantilla sin .html
    }

    // ------- ACCIÓN: Procesar alta de socio -------
    @PostMapping("/addSocio")
    public ModelAndView addSocio(
            @RequestParam("dni") String dni,
            @RequestParam("nombre") String nombre,
            @RequestParam("apellidos") String apellidos,
            @RequestParam("direccion") String direccion,
            @RequestParam("fechaNacimiento") String fechaNacimiento,
            @RequestParam("inscripcion") InscripcionType inscripcion
            ) {

        ModelAndView mav = new ModelAndView();

        // Verificar si el DNI ya existe en la base de datos
        Socio existingSocio = socioRepository.findSocioByDni(dni);
        if (existingSocio != null) {
            mav.addObject("errorMessage", "El DNI ya está registrado en otro socio: " + dni);
            mav.setViewName("addSocioViewFail");
            return mav;
        }

        // Convertir la fechaNacimiento de String a LocalDate
        LocalDate fechaNac = LocalDate.parse(fechaNacimiento);  // Convierte String a LocalDate
        LocalDate fechaAlta = LocalDate.now();

        Socio nuevoSocio = new Socio(dni, nombre, apellidos, fechaNac,direccion, false, fechaAlta);

        boolean ok = socioRepository.addSocio(nuevoSocio);
        
        if (!ok) {
            mav.setViewName("addSocioViewFail");
        }

        Socio socioGuardado = socioRepository.findSocioByDni(dni);

        Inscripcion nuevaInscripcion = new Inscripcion(
                0,
                inscripcion,
                300,
                fechaAlta,
                socioGuardado.getDni(),
                0
            );

        if(inscripcion == InscripcionType.FAMILIAR){
            // Debe de pedir si vas a crear una familia o te vas a unir a una
            // (Si creas familia)
            // Se añade el socio y se crea una familia con valores dfl
            // Se muestra por pantalla el ID de familia
            // (Si no creas familia)
            // Te pide ID de familia
            // Te une a dicha familia, añadiendo socio e incrementando el numAdultos o numNiños de Familia en funcion de tu fecha de nacimiento
        }

        boolean okInscripcion = inscripcionRepository.addInscripcion(nuevaInscripcion);
        if (!okInscripcion) {
        mav.addObject("warningMessage",
                "El socio se ha creado correctamente, pero ha habido un problema creando la inscripción.");
        }

        mav.setViewName("addSocioViewSuccess");
        mav.addObject("socio", nuevoSocio);
        mav.addObject("inscripcion", nuevaInscripcion);
        return mav;
    }

    // ------- VISTA/ACCIÓN: Añadir título de patrón a un socio -------
    @GetMapping("/addTituloPatronASocio")
    public ModelAndView getAddTituloPatronView() {
        return new ModelAndView("addTituloPatronASocio"); // nombre de la plantilla sin .html
    }

    @PostMapping("/addTituloPatronASocio")
    public ModelAndView addTituloPatronASocio(
            @RequestParam("dni") String dni,
            @RequestParam(name = "tituloPatron", defaultValue = "false") boolean tituloPatron) {  // Usar defaultValue para manejar el caso de checkbox no seleccionado

        ModelAndView mav = new ModelAndView();

        // Buscar al socio por su DNI
        Socio socio = socioRepository.findSocioByDni(dni);

        // Verificar si el socio existe
        if (socio == null) {
            mav.addObject("errorMessage", "No se encontró socio con el DNI: " + dni);
            mav.setViewName("addTituloPatronASocioFail"); // Vista de error
            return mav;
        }

        // Si no se seleccionó el título de patrón, mostrar que no se realizó ningún cambio
        if (!tituloPatron) {
            mav.addObject("errorMessage", "El socio no tiene el título de patrón, por lo que no se realizó ningún cambio.");
            mav.setViewName("addTituloPatronASocioFail"); // Vista de error
            return mav;
        }

        // Verificar si el socio ya tiene el título de patrón
        if (socio.esPatronEmbarcacion()) {
            mav.addObject("errorMessage", "El socio ya tiene el título de patrón.");
            mav.setViewName("addTituloPatronASocioFail"); // Mostrar vista de error si ya tiene el título
            return mav;
        }

        // Actualizar el título de patrón
        socio.setPatronEmbarcacion(tituloPatron);

        boolean ok = socioRepository.updateSocio(socio);
        if (ok) {
            mav.setViewName("addTituloPatronASocioSuccess"); // Vista de éxito
        } else {
            mav.addObject("errorMessage", "Hubo un problema al actualizar el título de patrón.");
            mav.setViewName("addTituloPatronASocioFail"); // Vista de error
        }
        mav.addObject("socio", socio);
        return mav;
    }

    // ------- VISTA: Listar socios -------
    @GetMapping("/listSocios")
    public ModelAndView listSocios() {
        ModelAndView mav = new ModelAndView("listSocios"); // nombre de la plantilla sin .html
        mav.addObject("socios", socioRepository.findAllSocios());
        return mav;
    }
}
