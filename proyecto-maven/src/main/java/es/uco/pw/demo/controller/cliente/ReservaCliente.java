package es.uco.pw.demo.controller.cliente;

import java.util.List;
import java.util.Arrays;
import java.util.Date;
import java.time.LocalDate;

import org.springframework.cglib.core.Local;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import ch.qos.logback.core.pattern.util.RestrictedEscapeUtil;
import es.uco.pw.demo.model.Reserva;
import es.uco.pw.demo.model.ReservaRepository;

public class ReservaCliente {
  
  public static void main(String[] args){
		sendPostRequests();
		sendGetRequests();
    sendPutRequests();
		sendPatchRequests();
		sendDeleteRequests();
  }

	private static void sendGetRequests(){
		RestTemplate rest = new RestTemplate();
		String baseURL = "http://localhost:8080";

		// 1. Muestra todas las reservas
		ResponseEntity<Reserva[]> response = rest.getForEntity(baseURL + "/api/reservas", Reserva[].class);
		List<Reserva> reservasList = Arrays.asList(response.getBody());
		System.out.println("==== REQUEST 3: GET all reservas ====");
		Date date = new Date(response.getHeaders().getDate());
		System.out.println("Response date: " + date);
		for(Reserva s: reservasList){
			System.out.println(s.getMatriculaEmbarcacion() + " " + s.getPlazasReserva() + " : " + s.getFechaReserva() );
		}

		// Request to retrive one Reserva
		Reserva s = rest.getForObject(baseURL + "/api/reservas/{matricula}", Reserva.class, "1");
		System.out.println("==== REQUEST 4: GET Reserva por ID ====");
		if(s != null) System.out.println(s.getMatriculaEmbarcacion() + " " + s.getPlazasReserva() + " : " + s.getFechaReserva() );


    // por fecha
    ResponseEntity<Reserva[]> response2 = rest.getForEntity(baseURL + "/api/reservas/futuras?fecha={fecha}", Reserva[].class, "2025-06-21");
		List<Reserva> reservasList2 = Arrays.asList(response2.getBody());
		System.out.println("==== REQUEST 5: GET reservas after Fecha ====");
		Date date2 = new Date(response.getHeaders().getDate());
		System.out.println("Response date: " + date);
		for(Reserva s2: reservasList2){
			System.out.println(s2.getMatriculaEmbarcacion() + " " + s2.getPlazasReserva() + " : " + s2.getFechaReserva() );
		}
	}

	private static void sendPostRequests(){

		RestTemplate rest = new RestTemplate();
		String baseURL = "http://localhost:8080";

		// POST a new Reserva (valid)
		Reserva newReserva = new Reserva(0, "8a-BD-1-1-23", 8, LocalDate.parse("2028-10-03"), 500);
		ResponseEntity<Reserva> response;
		
		try{
			response = rest.postForEntity(baseURL + "/api/reservas", newReserva, Reserva.class);
			System.out.println("==== REQUEST 1: POST Reserva (valid) ====");
			System.out.println("Status code: " + response.getStatusCode());
			if(response != null)
				System.err.println("Response body:\n" + response.getBody().getMatriculaEmbarcacion() + " " + response.getBody().getPlazasReserva() + " : " + response.getBody().getFechaReserva());
		}catch(HttpClientErrorException exception){
			System.out.println(exception);
		}

		// POST a Reserva (invalid)
		newReserva = new Reserva(1, "Invalida", 3, LocalDate.parse("2025-01-01"), 300);

		System.out.println("==== REQUEST 2: POST Reserva (invalid) ====");
		try{
			response = rest.postForEntity(baseURL + "/api/reservas", newReserva, Reserva.class);
		}catch(HttpClientErrorException exception){
			System.out.println(exception);
		}

	}

  private static void sendPutRequests(){

    RestTemplate rest = new RestTemplate();
    String baseUrl = "http://localhost:8080/api/reservas";

    // Modificar la Reserva 6a-BC-1-3-12 de manera integra
    Integer id = 1;

    // Valores previos:
    /*
      1	/ 7a-AB-1-2-20 /	5 / 2025-06-20 / 200
    */

    Reserva modificarReserva = new Reserva(1, "7a-AB-1-2-20", 8, LocalDate.parse("2025-04-12"), 400);
		try{
			System.out.println("==== REQUEST 6: PUT (no response) ====");
			rest.put(baseUrl + "/{id}", modificarReserva, id);
			System.out.println("Update correct.");
		}catch(RestClientException exception){
			System.out.println(exception);
		}
  }

  private static void sendPatchRequests(){

    RestTemplate rest = new RestTemplate();
    rest.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    String baseUrl = "http://localhost:8080/api/reservas";
    String matricula;

    // 1. Update de la fecha de una reserva
    try{
			System.out.println("==== REQUEST 7: PATCH (valid) ====");
			matricula = "6a-AB-1-1-15";
			Reserva reserva1 = new Reserva();

      // Valor previo: Martin

			reserva1.setFechaReserva(LocalDate.parse("2030-01-01"));
			Reserva response1 = rest.patchForObject(baseUrl + "/16", reserva1, Reserva.class);
			System.out.println(response1.toString());
		}catch(RestClientException exception){
			System.out.println(exception);
		}

    // 3. Intento de actualizar una reserva inválida
    try{
			System.out.println("==== REQUEST 9: PATCH (invalid) ====");
			matricula = "Dni Invalida";
			Reserva reserva3 = new Reserva();
      reserva3.setMatriculaEmbarcacion("Invalido");
			Reserva response3 = rest.patchForObject(baseUrl + "/{id}", reserva3, Reserva.class, 3);
			if(response3 != null) System.out.println(response3.toString());
		}catch(RestClientException exception){
			System.out.println(exception);
		}

  }

	private static void sendDeleteRequests(){

		RestTemplate rest = new RestTemplate();
		String baseUrl = "http://localhost:8080/api/reservas";

		// 1. Borrar una Reserva con matricula 46072606H
		try{
			
      ResponseEntity<Reserva[]> response = rest.getForEntity(baseUrl, Reserva[].class);
      List<Reserva> reservasList = Arrays.asList(response.getBody());
      Integer id = reservasList.getLast().getId();

			System.out.println("==== REQUEST 10: DELETE ONE OBJECT (valid) ====");
			rest.delete(baseUrl + "/{id}", id);
		}catch(RestClientException exception){
			System.out.println(exception);
		}

		// 2. Borrar una Reserva que no existe
		try{
			System.out.println("==== REQUEST 11: DELETE AN OBJECT THAT DOESNT EXIST (no effect) ====");
			rest.delete(baseUrl + "/{id}", 3);
		}catch(RestClientException exception){
			System.out.println(exception);
		}

		// 3. Borrarlo todo
		// Descomentar en caso de querer usar

		/*
			try{
				System.out.println("==== REQUEST 12: DELETE ALL OBJECTS (valid) ====");
				rest.delete(baseUrl);
			}catch(RestClientException exception){
				System.out.println(exception);
			}
		*/

	}

}
