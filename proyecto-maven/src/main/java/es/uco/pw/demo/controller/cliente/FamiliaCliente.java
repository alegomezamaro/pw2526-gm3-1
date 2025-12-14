package es.uco.pw.demo.controller.cliente;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import es.uco.pw.demo.model.Familia;
import es.uco.pw.demo.model.Socio;

public class FamiliaCliente {

    private static final String DNI_AUXILIAR = "99999999Z";
    private static final String BASE_URL = "http://localhost:8080";
    private static Integer idFamiliaGenerada = null;

    public static void main(String[] args) {
        System.out.println(">>> INICIANDO PRUEBAS DE CLIENTE FAMILIA <<<");

        // 1. Preparación
        crearSocioAuxiliar();
        crearFamiliaPrueba();

        if (idFamiliaGenerada != null) {
            // 2. Operaciones de lectura y modificación básica
            obtenerFamilias();
            actualizarFamilia();

            // 3. NUEVO: Operaciones de Miembros (Vincular/Desvincular)
            vincularSocio();     // Aseguramos que está dentro
            desvincularSocio();  // Probamos a sacarlo (Lo que pedías)

            // 4. Limpieza de Familia
            borrarFamilia();
        }

        // 5. Limpieza de Socio
        borrarSocioAuxiliar(); 
        
        System.out.println(">>> FIN <<<");
    }

    // --------------------------------------------------------
    // MÉTODOS DE PRUEBA
    // --------------------------------------------------------

    private static void crearSocioAuxiliar() {
        RestTemplate rest = new RestTemplate();
        Socio socio = new Socio();
        socio.setDni(DNI_AUXILIAR);
        socio.setNombre("Test");
        socio.setApellidos("Familia Cliente");
        socio.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        socio.setFechaAlta(LocalDate.now());

        try {
            rest.postForEntity(BASE_URL + "/api/socios", socio, Socio.class);
            System.out.println("[OK] Socio auxiliar creado.");
        } catch (HttpClientErrorException e) {
            System.out.println("[INFO] Socio ya existe o error: " + e.getStatusCode());
        }
    }

    private static void crearFamiliaPrueba() {
        RestTemplate rest = new RestTemplate();
        Familia f = new Familia();
        f.setDniTitular(DNI_AUXILIAR);
        f.setNumAdultos(2);
        f.setNumNiños(1);

        try {
            ResponseEntity<Familia> res = rest.postForEntity(BASE_URL + "/api/familias", f, Familia.class);
            if (res.getBody() != null) {
                idFamiliaGenerada = res.getBody().getId();
                System.out.println("[OK] Familia creada ID: " + idFamiliaGenerada);
            }
        } catch (Exception e) {
            System.out.println("[ERROR] POST Familia: " + e.getMessage());
        }
    }

    private static void obtenerFamilias() {
        RestTemplate rest = new RestTemplate();
        try {
            rest.getForEntity(BASE_URL + "/api/familias", Familia[].class);
            System.out.println("[OK] GET Familias funcionó.");
        } catch (Exception e) {
            System.out.println("[ERROR] GET Familias: " + e.getMessage());
        }
    }

    private static void actualizarFamilia() {
        RestTemplate rest = new RestTemplate();
        rest.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        Familia update = new Familia();
        update.setNumNiños(5);

        try {
            rest.patchForObject(BASE_URL + "/api/familias/" + idFamiliaGenerada, update, Familia.class);
            System.out.println("[OK] Familia actualizada (PATCH miembros numéricos).");
        } catch (Exception e) {
            System.out.println("[ERROR] PATCH Familia: " + e.getMessage());
        }
    }

    // --- NUEVOS MÉTODOS DE VINCULACIÓN ---

    private static void vincularSocio() {
        RestTemplate rest = new RestTemplate();
        rest.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        
        Map<String, String> body = new HashMap<>();
        body.put("dni", DNI_AUXILIAR);

        try {
            // PATCH: /api/familias/{id}/vincular
            String url = BASE_URL + "/api/familias/" + idFamiliaGenerada + "/vincular";
            rest.patchForObject(url, body, String.class);
            System.out.println("[OK] Socio vinculado explícitamente a la familia.");
        } catch (Exception e) {
            System.out.println("[ERROR] Vincular Socio: " + e.getMessage());
        }
    }

    private static void desvincularSocio() {
        RestTemplate rest = new RestTemplate();
        rest.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        
        Map<String, String> body = new HashMap<>();
        body.put("dni", DNI_AUXILIAR);

        try {
            // PATCH: /api/familias/{id}/desvincular
            String url = BASE_URL + "/api/familias/" + idFamiliaGenerada + "/desvincular";
            rest.patchForObject(url, body, String.class);
            System.out.println("[OK] Socio desvinculado correctamente.");
        } catch (Exception e) {
            System.out.println("[ERROR] Desvincular Socio: " + e.getMessage());
        }
    }

    // -------------------------------------

    private static void borrarFamilia() {
        RestTemplate rest = new RestTemplate();
        try {
            rest.delete(BASE_URL + "/api/familias/" + idFamiliaGenerada);
            System.out.println("[OK] Familia borrada.");
        } catch (Exception e) {
            System.out.println("[ERROR] DELETE Familia: " + e.getMessage());
        }
    }

    private static void borrarSocioAuxiliar() {
        RestTemplate rest = new RestTemplate();
        try {
            rest.delete(BASE_URL + "/api/socios/{dni}", DNI_AUXILIAR);
            System.out.println("[OK] Socio auxiliar borrado.");
        } catch (Exception e) {}
    }
}