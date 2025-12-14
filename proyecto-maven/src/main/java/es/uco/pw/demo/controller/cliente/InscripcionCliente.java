package es.uco.pw.demo.controller.cliente;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import es.uco.pw.demo.model.Inscripcion;

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


		try{
			String dni = "46072606H";
			System.out.println("==== REQUEST 10: DELETE ONE OBJECT (valid) ====");
			rest.delete(baseUrl + "/eliminar_inscripcion/{dni}", dni);
		}catch(RestClientException exception){
			System.out.println(exception);
		}

	}

}
