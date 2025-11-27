package es.uco.pw.demo.controller;

import java.time.LocalDate;
import java.time.Period;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import es.uco.pw.demo.model.Familia;
import es.uco.pw.demo.model.FamiliaRepository;
import es.uco.pw.demo.model.Inscripcion;
import es.uco.pw.demo.model.InscripcionRepository;
import es.uco.pw.demo.model.InscripcionType;
import es.uco.pw.demo.model.Socio;
import es.uco.pw.demo.model.SocioRepository;

@Controller
public class SocioController {

    private final SocioRepository socioRepository;
    private final InscripcionRepository inscripcionRepository;
    private final FamiliaRepository familiaRepository;
    private static final int mayorEdad = 18;

    public SocioController(SocioRepository socioRepository,
                           InscripcionRepository inscripcionRepository, FamiliaRepository familiaRepository) {
        this.socioRepository = socioRepository;
        this.inscripcionRepository = inscripcionRepository;
        this.familiaRepository = familiaRepository; 
    }

    private int calcularEdad(LocalDate date){
        return Period.between(date, LocalDate.now()).getYears();
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
            @RequestParam("inscripcion") InscripcionType inscripcion,
            @RequestParam(value = "familyAction", required = false) String familyAction,
            @RequestParam(value = "familiaId", required = false) Integer familiaId
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

        boolean isAdulto = calcularEdad(fechaNac) >= mayorEdad;

        boolean ok = socioRepository.addSocio(nuevoSocio);

        
        if(inscripcion == InscripcionType.FAMILIAR){
            if( familyAction == null ){
                mav.addObject("errorMessage", "Debe seleccionar una acción (Crear o unirse a una Familia).");
                mav.setViewName("addSocioViewFail");
                return mav;
            }
            if( "JOIN".equals(familyAction) && familiaId == null){
                mav.addObject("errorMessage", "Debe introducir un ID de Familia para unirse.");
                mav.setViewName("addSocioViewFail");
                return mav;
            }
        }
        
        if (!ok) {
            mav.setViewName("addSocioViewFail");
        }

        Socio socioGuardado = socioRepository.findSocioByDni(dni);
        Integer familiaAsociadaId = null;

        if(inscripcion == InscripcionType.FAMILIAR){
            if("CREATE".equals(familyAction)){
                Familia nuevaF = new Familia();
                nuevaF.setDniTitular(socioGuardado.getDni());
                nuevaF.setNumAdultos(isAdulto ? 1 : 0);
                nuevaF.setNumNiños(isAdulto ? 0 : 1);

                familiaRepository.addFamilia(nuevaF);

                familiaAsociadaId = familiaRepository.getLastId();

                if(familiaAsociadaId != null){
                    nuevaF.setId(familiaAsociadaId);
                    mav.addObject("familiaIdCreada", familiaAsociadaId);
                }
            } else if("JOIN".equals(familyAction) && familiaId != null){
                Familia familiaUnir = familiaRepository.findFamiliaById(familiaId);
                if(familiaUnir == null){
                    mav.addObject("errorMessage", "No se encontró familia con ID: " + familiaId);
                    mav.setViewName("addSocioViewFail");
                    return mav;
                }
                if(isAdulto){
                    familiaUnir.setNumAdultos(familiaUnir.getNumAdultos()+1);
                }else{
                    familiaUnir.setNumNiños(familiaUnir.getNumNiños()+1);
                }
                boolean okUpdate = familiaRepository.updateFamilia(familiaUnir);
                if (!okUpdate) {
                    mav.addObject("warningMessage", "El socio se unió, pero hubo un problema al actualizar la familia.");
                }
                familiaAsociadaId = familiaId;
            }
        }


        Inscripcion nuevaInscripcion = new Inscripcion();
            nuevaInscripcion.setTipoCuota(inscripcion);
            nuevaInscripcion.setCuotaAnual(300);
            nuevaInscripcion.setFechaInscripcion(fechaAlta);
            nuevaInscripcion.setDniTitular(socioGuardado.getDni());
            nuevaInscripcion.setFamiliaId(null);


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
