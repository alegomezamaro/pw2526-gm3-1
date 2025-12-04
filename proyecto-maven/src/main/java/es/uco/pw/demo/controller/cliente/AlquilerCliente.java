package es.uco.pw.demo.controller.cliente;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import es.uco.pw.demo.model.Alquiler;
import es.uco.pw.demo.model.Embarcacion;
import es.uco.pw.demo.model.Socio;

public class AlquilerCliente {

    public static void main(String[] args) {
        // 1. Creamos datos primero para poder probar lo demas
        sendPostRequests(); 
        
        // 2. Probamos las lecturas
        sendGetRequests();
        
        // 3. Probamos modificaciones (Vincular/Desvincular)
        sendPatchRequests();
        
        // 5. Borrado
        sendDeleteRequests();
    }

    private static void sendGetRequests() {
        RestTemplate rest = new RestTemplate();
        String baseURL = "http://localhost:8080/api/alquileres";

        try {
            // 1. Obtener todos los alquileres
            System.out.println("==== REQUEST: GET all alquileres ====");
            ResponseEntity<Alquiler[]> response = rest.getForEntity(baseURL, Alquiler[].class);
            List<Alquiler> alquileres = Arrays.asList(response.getBody());
            for (Alquiler a : alquileres) {
                System.out.println("ID: " + a.getId() + " | Matricula: " + a.getMatriculaEmbarcacion());
            }

            // 2. Buscar por Tipo de Embarcación
            System.out.println("==== REQUEST: GET alquiler por tipo (VELERO) ====");
            // Nota: Asegúrate de que VELERO exista en tu Enum o usa uno válido
            response = rest.getForEntity(baseURL + "/buscar_alquiler_por_tipo_de_embarcacion?tipo=VELERO", Alquiler[].class);
            if(response.getBody() != null) {
                Arrays.stream(response.getBody()).forEach(a -> System.out.println("Alquiler ID: " + a.getId()));
            }

            // 3. Buscar por ID
            System.out.println("==== REQUEST: GET alquiler por ID (1) ====");
            try {
                ResponseEntity<Alquiler> alqResponse = rest.getForEntity(baseURL + "/buscar_por_id?id=1", Alquiler.class);
                System.out.println("Encontrado: " + alqResponse.getBody().getMatriculaEmbarcacion());
            } catch (HttpClientErrorException.NotFound e) {
                System.out.println("Alquiler con ID 1 no encontrado.");
            }

            // 4. Buscar por Fecha
            System.out.println("==== REQUEST: GET alquiler por fecha ====");
            String fecha = LocalDate.now().toString(); // Busca los de hoy
            response = rest.getForEntity(baseURL + "/buscar_por_fecha?fecha=" + fecha, Alquiler[].class);
            System.out.println("Alquileres encontrados para hoy: " + response.getBody().length);

            // 5. Embarcaciones Disponibles (Devuelve lista de Embarcaciones, no Alquileres)
            System.out.println("==== REQUEST: GET embarcaciones disponibles ====");
            String urlDisponibles = baseURL + "/embarcaciones_disponibles?fecha=" + LocalDate.now().plusDays(1) + "&plazas=4";
            ResponseEntity<Embarcacion[]> respEmb = rest.getForEntity(urlDisponibles, Embarcacion[].class);
            if(respEmb.getBody() != null){
                for(Embarcacion e : respEmb.getBody()) {
                    System.out.println("Disponible: " + e.getMatricula());
                }
            }

        } catch (RestClientException e) {
            System.out.println("Error en GET: " + e.getMessage());
        }
    }

    private static void sendPostRequests() {
        RestTemplate rest = new RestTemplate();
        String baseURL = "http://localhost:8080/api/alquileres";

        System.out.println("==== REQUEST: POST crear alquiler sin socio ====");
        
        Alquiler newAlquiler = new Alquiler();
        newAlquiler.setMatriculaEmbarcacion("6a-AB-1-1-15"); 
        newAlquiler.setFechaInicio(LocalDate.now().plusDays(5));
        newAlquiler.setFechaFin(LocalDate.now().plusDays(7));
        newAlquiler.setPlazasSolicitadas(4);
        newAlquiler.setPrecioTotal(200.0);

        try {
            ResponseEntity<Alquiler> response = rest.postForEntity(baseURL, newAlquiler, Alquiler.class);
            System.out.println("Status: " + response.getStatusCode());
            System.out.println("Creado alquiler ID: " + response.getBody().getId());
        } catch (HttpClientErrorException e) {
            System.out.println("Error POST: " + e.getResponseBodyAsString());
        }
    }

    private static void sendPatchRequests() {
        RestTemplate rest = new RestTemplate();
        // Importante para que funcione PATCH en Java estándar
        rest.setRequestFactory(new HttpComponentsClientHttpRequestFactory()); 
        
        String baseURL = "http://localhost:8080/api/alquileres";
        
        // Asumimos que queremos modificar el alquiler con ID 1 (ajusta según tus datos reales)
        Integer idAlquiler = 1; 

        // 1. Vincular Socio
        System.out.println("==== REQUEST: PATCH Vincular Socio ====");
        Socio socio = new Socio();
        socio.setDni("12345678Z");
        socio.setNombre("Cliente");
        socio.setApellidos("Test");
        socio.setFechaNacimiento(LocalDate.parse("1999-01-01"));
        socio.setPatronEmbarcacion(false);
        socio.setFechaAlta(LocalDate.now());
        
        try {
            String url = baseURL + "/vincular_socio/" + idAlquiler;
            // patchForObject devuelve lo que responda el controller (String en tu caso, o el objeto)
            String resultado = rest.patchForObject(url, socio, String.class);
            System.out.println("Resultado vinculación: " + resultado);
        } catch (HttpClientErrorException e) {
            System.out.println("Error PATCH Vincular: " + e.getResponseBodyAsString());
        } catch (RestClientException e) {
             System.out.println("Error conexión: " + e.getMessage());
        }

        // 2. Desvincular Socio
        System.out.println("==== REQUEST: PATCH Desvincular Socio ====");
        try {
            String url = baseURL + "/desvincular_socio/" + idAlquiler;
            // Enviamos null en el cuerpo porque el controller no usa @RequestBody para desvincular
            String resultado = rest.patchForObject(url, null, String.class);
            System.out.println("Resultado desvinculación: " + resultado);
        } catch (HttpClientErrorException e) {
            System.out.println("Error PATCH Desvincular: " + e.getResponseBodyAsString());
        }
    }

   
    private static void sendDeleteRequests() {
        RestTemplate rest = new RestTemplate();
        String baseURL = "http://localhost:8080/api/alquileres";

        // Borrar alquiler no iniciado
        System.out.println("==== REQUEST: DELETE alquiler no iniciado ====");
        Integer idParaBorrar = 7; // Ajusta el ID

        try {
            rest.delete(baseURL + "/delete_alquiler_no_iniciado/{id}", idParaBorrar);
            System.out.println("Solicitud de borrado enviada correctamente.");
        } catch (HttpClientErrorException e) {
            System.out.println("Error DELETE: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        } catch (RestClientException e) {
            System.out.println("Error DELETE: " + e.getMessage());
        }
    }
}