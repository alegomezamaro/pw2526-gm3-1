package es.uco.pw.demo.controller.cliente;

import java.util.List;
import java.util.Arrays;
import java.util.Date;
import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import ch.qos.logback.core.pattern.util.RestrictedEscapeUtil;
import es.uco.pw.demo.model.Inscripcion;
import es.uco.pw.demo.model.InscripcionRepository;
import es.uco.pw.demo.model.InscripcionType;
import es.uco.pw.demo.model.Socio;

public class InscripcionCliente {
  
  public static void main(String[] args){
		sendGetRequests();
    sendPutRequests();
		sendDeleteRequests();
  }

	private static void sendGetRequests(){
		RestTemplate rest = new RestTemplate();
		String baseURL = "http://localhost:8080";

		// 1. Muestra todas las inscripciones
		ResponseEntity<Inscripcion[]> response = rest.getForEntity(baseURL + "/api/inscripciones", Inscripcion[].class);
		List<Inscripcion> inscripcionesList = Arrays.asList(response.getBody());
		System.out.println("==== REQUEST 3: GET all inscripciones ====");
		Date date = new Date(response.getHeaders().getDate());
		System.out.println("Response date: " + date);
		for(Inscripcion s: inscripcionesList){
		System.out.println(s.getId() + " : " + s.getDniTitular() + "- tipo: " + s.getTipoCuota());
		}
	}

  private static void sendPutRequests(){

    RestTemplate rest = new RestTemplate();
    String baseUrl = "http://localhost:8080/api/inscripciones";

		try{
			System.out.println("==== REQUEST 6: PUT (no response) ====");
			rest.put(baseUrl + "/individual_a_familiar/{id}", 3, 14);
			System.out.println("Update correct.");
		}catch(RestClientException exception){
			System.out.println(exception);
		}
  }

	private static void sendDeleteRequests(){

		RestTemplate rest = new RestTemplate();
		String baseUrl = "http://localhost:8080/api/inscripciones";

		// 1. Borrar una Inscripcion con dni 46072606H
    // Cambiar a DNI VALIDO
		try{
			String dni = "42356432J";
			System.out.println("==== REQUEST 10: DELETE ONE OBJECT (valid) ====");
			rest.delete(baseUrl + "/eliminar_inscripcion/{dni}", dni);
		}catch(RestClientException exception){
			System.out.println(exception);
		}

	}

}
