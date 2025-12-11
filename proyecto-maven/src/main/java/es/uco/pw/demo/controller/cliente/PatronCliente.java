package es.uco.pw.demo.controller.cliente;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import es.uco.pw.demo.model.Patron;

public class PatronCliente {
	public static void main(String[] args){
		sendPostRequests();
		sendGetRequests();
		sendPatchRequests();
		sendDeleteRequests();
	}

	private static void sendPostRequests() {
        RestTemplate rest = new RestTemplate();
		String baseURL = "http://localhost:8080/api/patrones";
	}

	private static void sendGetRequests() {
        RestTemplate rest = new RestTemplate();
		String baseURL = "http://localhost:8080/api/patrones";
	}

	private static void sendPatchRequests() {
		RestTemplate rest = new RestTemplate(); 
        rest.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
		String baseURL = "http://localhost:8080/api/patrones";
        String dni;
        try{
			System.out.println("==== REQUEST 1: PATCH UPDATE A PATRON ====");
			dni = "44444444D";
			Patron patron1 = new Patron();
			patron1.setNombre("Rafa");
            patron1.setApellidos("Nadal");
			Patron response1 = rest.patchForObject(baseURL + "/{dni}", patron1, Patron.class, dni);
			System.out.println(response1.toString());
		}catch(RestClientException exception){
			System.out.println(exception);
		}
	}

	private static void sendDeleteRequests() {
		RestTemplate rest = new RestTemplate();
		String baseURL = "http://localhost:8080/api/patrones";
        try{
			String dni = "44444444D";
			System.out.println("==== REQUEST 2: DELETE PATRON ====");
			rest.delete(baseURL + "/{dni}", dni);
            System.out.println("Eliminado con exito!!!");
		}catch(RestClientException exception){
			System.out.println(exception);
		}
    }
}
