package es.uco.pw.demo.controller.cliente;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import es.uco.pw.demo.model.Embarcacion;
import es.uco.pw.demo.model.EmbarcacionType;

public class EmbarcacionCliente {
  
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

		// 1. Muestra todas las embarcaciones
		ResponseEntity<Embarcacion[]> response = rest.getForEntity(baseURL + "/api/embarcaciones", Embarcacion[].class);
		List<Embarcacion> embarcacionesList = Arrays.asList(response.getBody());
		System.out.println("==== REQUEST 3: GET all embarcaciones ====");
		Date date = new Date(response.getHeaders().getDate());
		System.out.println("Response date: " + date);
		for(Embarcacion s: embarcacionesList){
			System.out.println(s.getMatricula() + " : " + s.getNombre() );
		}

		// 2. Embarcaciones de tipo YATE
		response = rest.getForEntity(baseURL + "/api/embarcaciones?tipo=YATE", Embarcacion[].class);
		embarcacionesList = Arrays.asList(response.getBody());
		System.out.println("==== REQUEST 4: GET Embarcacions por tipo (YATE) ====");
		date = new Date(response.getHeaders().getDate());
		System.out.println("Response date: " + date);
		for(Embarcacion s: embarcacionesList){
			System.out.println(s.getMatricula() + " : " + s.getNombre() );
		}

		// Request to retrive one Embarcacion
		Embarcacion Embarcacion = rest.getForObject(baseURL + "/api/embarcaciones/{matricula}", Embarcacion.class, "6a-AB-1-1-15");
		System.out.println("==== REQUEST 5: GET Embarcacion por matricula ====");
		if(Embarcacion != null) System.out.println(Embarcacion.getMatricula() + " : " + Embarcacion.getNombre() );
	}

	private static void sendPostRequests(){

		RestTemplate rest = new RestTemplate();
		String baseURL = "http://localhost:8080";

		// POST a new Embarcacion (valid)
		Embarcacion newEmbarcacion = new Embarcacion("ABC123","Client Boat",EmbarcacionType.YATE, 8, "80x40m", null);
		ResponseEntity<Embarcacion> response;
		
		try{
			response = rest.postForEntity(baseURL + "/api/embarcaciones", newEmbarcacion, Embarcacion.class);
			System.out.println("==== REQUEST 1: POST Embarcacion (valid) ====");
			System.out.println("Status code: " + response.getStatusCode());
			if(response != null)
				System.err.println("Response body:\n" + response.getBody().getNombre() +  " : " + response.getBody().getMatricula());
		}catch(HttpClientErrorException exception){
			System.out.println(exception);
		}

		// POST a Embarcacion (invalid)
		newEmbarcacion = new Embarcacion("ABC123","Repeat Boat",EmbarcacionType.YATE, 10, "100x40m", null);

		System.out.println("==== REQUEST 2: POST Embarcacion (invalid) ====");
		try{
			response = rest.postForEntity(baseURL + "/api/embarcaciones", newEmbarcacion, Embarcacion.class);
		}catch(HttpClientErrorException exception){
			System.out.println(exception);
		}

	}

  private static void sendPutRequests(){

    RestTemplate rest = new RestTemplate();
    String baseUrl = "http://localhost:8080/api/embarcaciones";

    // Modificar la embarcacion 6a-BC-1-3-12 de manera integra
    EmbarcacionType tipo = EmbarcacionType.YATE;
    String matricula = "6a-BC-1-3-12";
    Embarcacion newEmbarcacion = new Embarcacion(matricula, "Api Boat", tipo, 15, "100x20m", 0);
		try{
			System.out.println("==== REQUEST 6: PUT (no response) ====");
			rest.put(baseUrl + "/{matricula}", newEmbarcacion, matricula);
			System.out.println("Update correct.");
		}catch(RestClientException exception){
			System.out.println(exception);
		}
  }

  private static void sendPatchRequests(){

    RestTemplate rest = new RestTemplate();
    rest.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    String baseUrl = "http://localhost:8080/api/embarcaciones";
    String matricula;

    // 1. Update del nombre de embarcacion
    try{
			System.out.println("==== REQUEST 7: PATCH (Actualizacion nombre) ====");
			matricula = "6a-AB-1-1-15";
			Embarcacion embarcacion1 = new Embarcacion();
			embarcacion1.setNombre("Patch Boat");
			Embarcacion response1 = rest.patchForObject(baseUrl + "/{matricula}", embarcacion1, Embarcacion.class, matricula);
			System.out.println(response1.toString());
		}catch(RestClientException exception){
			System.out.println(exception);
		}

    // 2. Cambio de Tipo y Plazas
    try{
			System.out.println("==== REQUEST 8: PATCH (Actualizacion tipos y plazas) ====");
			matricula = "7a-AB-1-2-20";
			Embarcacion embarcacion2 = new Embarcacion();
			embarcacion2.setTipo(EmbarcacionType.CATAMARAN);
      embarcacion2.setPlazas(12);
			Embarcacion response2 = rest.patchForObject(baseUrl + "/{matricula}", embarcacion2, Embarcacion.class, matricula);
			System.out.println(response2.toString());
		}catch(RestClientException exception){
			System.out.println(exception);
		}

    // 3. Intento de actualizar una matricula inválida
    try{
			System.out.println("==== REQUEST 9: PATCH (Actualizar con una mátricula invalida) ====");
			matricula = "Matricula Invalida";
			Embarcacion embarcacion3 = new Embarcacion();
      embarcacion3.setNombre("Invalido");
			embarcacion3.setTipo(EmbarcacionType.CATAMARAN);
      embarcacion3.setPlazas(3);
			Embarcacion response3 = rest.patchForObject(baseUrl + "/{matricula}", embarcacion3, Embarcacion.class, matricula);
			System.out.println(response3.toString());
	}catch(RestClientException exception){
			System.out.println(exception);
		}

	try{
			System.out.println("==== REQUEST 10: PATCH (Vincular patron) ====");
			matricula = "6a-AB-1-1-15";
			Integer id = 1;
			Embarcacion response4 = rest.patchForObject(baseUrl + "/{matricula}/{id}", null, Embarcacion.class, matricula,id);
			System.out.println("Respuesta del servidor: \n" + response4.toString());
	}catch(RestClientException exception){
			System.out.println("Error: " + exception.getMessage());
		}
	
	
	try{
			System.out.println("==== REQUEST 11: PATCH (Desvincular patron) ====");
			matricula = "6a-AB-1-1-15";
			Embarcacion response5 = rest.patchForObject(baseUrl + "/patron/{matricula}", null, Embarcacion.class, matricula);
			System.out.println("Respuesta del servidor: \n" + response5.toString());
	}catch(RestClientException exception){
			System.out.println("Error: " + exception.getMessage());
		}

  }

	private static void sendDeleteRequests(){

		RestTemplate rest = new RestTemplate();
		String baseUrl = "http://localhost:8080/api/embarcaciones";

		// 1. Borrar una embarcacion con matricula ABC123
		try{
			String matricula = "ABC123";
			System.out.println("==== REQUEST 12: Eliminar una embarcacion (valid) ====");
			rest.delete(baseUrl + "/{matricula}", matricula);
		}catch(RestClientException exception){
			System.out.println(exception);
		}

		// 2. Borrar una embarcacion que no existe
		try{
			System.out.println("==== REQUEST 13: Eliminar una embarcacion inexistente (no effect) ====");
			rest.delete(baseUrl + "/{matricula}", "MatriculaInvalida");
		}catch(RestClientException exception){
			System.out.println(exception);
		}

		try{
			System.out.println("==== REQUEST 14: ELIMINAR EMBARCACION NO ALQUILADA ====");
			rest.delete(baseUrl + "/delete_embarcacion_no_alquilada/{matricula}", "MatriculaInvalida");
		}catch(RestClientException exception){
			System.out.println(exception);
		}

				try{
			System.out.println("==== REQUEST 15: ELIMINAR UNA EMBARCACION NO ALQUILADA ====");
			rest.delete(baseUrl + "/delete_embarcacion_no_alquilada/{matricula}", "MatriculaInvalida");
		}catch(RestClientException exception){
			System.out.println(exception);
		}

	}

}
