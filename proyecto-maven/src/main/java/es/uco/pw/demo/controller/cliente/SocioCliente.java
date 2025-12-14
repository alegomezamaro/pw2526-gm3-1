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
import es.uco.pw.demo.model.Socio;
import es.uco.pw.demo.model.SocioRepository;

public class SocioCliente {
  
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

		// 1. Muestra todas las socios
		ResponseEntity<Socio[]> response = rest.getForEntity(baseURL + "/api/socios", Socio[].class);
		List<Socio> sociosList = Arrays.asList(response.getBody());
		System.out.println("==== REQUEST 3: GET all socios ====");
		Date date = new Date(response.getHeaders().getDate());
		System.out.println("Response date: " + date);
		for(Socio s: sociosList){
			System.out.println(s.getNombre() + " " + s.getApellidos() + " : " + s.getDni() );
		}

		// Request to retrive one Socio
		Socio s = rest.getForObject(baseURL + "/api/socios/{dni}", Socio.class, "10293847D");
		System.out.println("==== REQUEST 5: GET Socio por DNI ====");
		if(s != null) System.out.println(s.getNombre() + " " + s.getApellidos() + " : " + s.getDni() );
	}

	private static void sendPostRequests(){

		RestTemplate rest = new RestTemplate();
		String baseURL = "http://localhost:8080";

		// POST a new Socio (valid)
		Socio newSocio = new Socio("46072606H","Javier", "Castilla Ordoñez", LocalDate.parse("2005-10-03"), "C/Obispo Torres n5", false , LocalDate.parse("2024-10-10"));
		ResponseEntity<Socio> response;
		
		try{
			response = rest.postForEntity(baseURL + "/api/socios", newSocio, Socio.class);
			System.out.println("==== REQUEST 1: POST Socio (valid) ====");
			System.out.println("Status code: " + response.getStatusCode());
			if(response != null)
				System.err.println("Response body:\n" + response.getBody().getNombre() + " " + response.getBody().getApellidos() + " : " + response.getBody().getDni());
		}catch(HttpClientErrorException exception){
			System.out.println(exception);
		}

		// POST a Socio (invalid)
		newSocio = new Socio("46072606H","Javier", "Castilla Ordoñez", LocalDate.parse("1992-08-03"), "C/Obispo Torres n3", true , LocalDate.parse("2024-02-10"));

		System.out.println("==== REQUEST 2: POST Socio (invalid) ====");
		try{
			response = rest.postForEntity(baseURL + "/api/socios", newSocio, Socio.class);
		}catch(HttpClientErrorException exception){
			System.out.println(exception);
		}

	}

  private static void sendPutRequests(){

    RestTemplate rest = new RestTemplate();
    String baseUrl = "http://localhost:8080/api/socios";

    // Modificar la Socio 6a-BC-1-3-12 de manera integra
    String dni = "10293847D";

    // Valores previos:
    /*
      10293847D	/ Alejandro	/ Antúnez	 / Calle Séneca, SN, Córdoba	/2005-12-10	/ 1	/ 2023-11-12
    */

    Socio modificarSocio = new Socio("10293847D","Alejandro", "Antúnez", LocalDate.parse("2005-12-11"), "Nueva Direccion 7", true , LocalDate.parse("2025-12-09"));
		try{
			System.out.println("==== REQUEST 6: PUT (no response) ====");
			rest.put(baseUrl + "/{dni}", modificarSocio, dni);
			System.out.println("Update correct.");
		}catch(RestClientException exception){
			System.out.println(exception);
		}
  }

  private static void sendPatchRequests(){

    RestTemplate rest = new RestTemplate();
    rest.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    String baseUrl = "http://localhost:8080/api/socios";
    String dni;

    // 1. Update del nombre de Socio
    try{
			System.out.println("==== REQUEST 7: PATCH (valid) ====");
			dni = "12345678S";
			Socio socio1 = new Socio();

      // Valor previo: Martin

			socio1.setNombre("Álvaro");
			Socio response1 = rest.patchForObject(baseUrl + "/{dni}", socio1, Socio.class, dni);
			System.out.println(response1.toString());
		}catch(RestClientException exception){
			System.out.println(exception);
		}

    // // 2. Cambio de fechaAlta y 
    // try{
		// 	System.out.println("==== REQUEST 8: PATCH (valid) ====");
		// 	dni = "7a-AB-1-2-20";
		// 	Socio socio2 = new Socio();
		// 	socio2.setTipo(EmbarcacionType.CATAMARAN);
    //   socio2.setPlazas(12);
		// 	Socio response2 = rest.patchForObject(baseUrl + "/{dni}", socio2, Socio.class, dni);
		// 	System.out.println(response2.toString());
		// }catch(RestClientException exception){
		// 	System.out.println(exception);
		// }

    // 3. Intento de actualizar una dni inválida
    try{
			System.out.println("==== REQUEST 9: PATCH (invalid) ====");
			dni = "Dni Invalida";
			Socio socio3 = new Socio();
      socio3.setNombre("Invalido");
			Socio response3 = rest.patchForObject(baseUrl + "/{dni}", socio3, Socio.class, dni);
			if(response3 != null) System.out.println(response3.toString());
		}catch(RestClientException exception){
			System.out.println(exception);
		}

  }

	private static void sendDeleteRequests(){

		RestTemplate rest = new RestTemplate();
		String baseUrl = "http://localhost:8080/api/socios";

		// 1. Borrar una Socio con dni 46072606H

		try{
			String dni = "46072606H";
			System.out.println("==== REQUEST 10: DELETE AN INSCRIPTION (valid) ====");
			rest.delete(baseUrl + "/eliminar_inscripcion/{dni}", dni);
		}catch(RestClientException exception){
			System.out.println(exception);
		}

		try{
			String dni = "46072606H";
			System.out.println("==== REQUEST 11: DELETE ONE OBJECT (valid) ====");
			rest.delete(baseUrl + "/{dni}", dni);
		}catch(RestClientException exception){
			System.out.println(exception);
		}

		// 2. Borrar una Socio que no existe
		try{
			System.out.println("==== REQUEST 12: DELETE AN OBJECT THAT DOESNT EXIST (no effect) ====");
			rest.delete(baseUrl + "/{dni}", "MatriculaInvalida");
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
