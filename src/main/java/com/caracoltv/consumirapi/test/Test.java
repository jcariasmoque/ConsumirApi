package com.caracoltv.consumirapi.test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * 
 * @author Juan Camilo Arias Moque
 * clase que se encarga de realizar peticion POST al Api The Crowdsignal  e imprimir los resultados de la encuesta solicitada
 *
 */
public class Test {
	
/**
 * 	objeto tipo String que contiene la solicitud formato JSON ( proporciona a la api el partnerGUID,el userCode 
 *  y el id de la encuesta solicitada
 */
	private static String json = "{\r\n" + "    \"pdRequest\": {\r\n"
			+ "        \"partnerGUID\": \"5719aa21-c585-5aa9-42dd-00005547ef78\",\r\n"
			+ "        \"userCode\": \"$P$BzM55aacFGir8JbSGtmXdZ7WGnKXiV1\",\r\n" + "        \"demands\": {\r\n"
			+ "            \"demand\": {\r\n" + "                \"poll\": {\r\n"
			+ "                    \"id\": \"10503173\"\r\n" + "                }, \"id\": \"GetPollResults\"\r\n"
			+ "            }\r\n" + "        }\r\n" + "    }\r\n" + "}";


/**
 * prueba de consumo de la api ejecutando los metodos requestPost y readResults
 */
	public static void main(String[] args) {

		try {

			String response = requestPost(json);
			readResults(response);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
/**
 * metodo que genera la peticion POST al api	
 * @param data parametro tipo String que contiene la carga JSON que se enviara
 * @return String que contiene la respuesta de la api en formato JSON 
 * @throws Exception excepcion generada en caso de fallar la solicitud a la api
 */

	public static String requestPost(String data) throws Exception {

		HttpClient httpClient = HttpClient.newBuilder().build();

		HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://api.crowdsignal.com/v1"))
				.POST(BodyPublishers.ofString(data)).build();

		HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());

		return response.body();
	}
	

/**
 * clase que se encarga de analizar el dato JSON obtenido como respuesta y mostrar por consola los resultados de la encuesta 	
 * @param response String que contiene la respuesta formato JSON devuelta por la api al ejecutar el metodo requestPost
 */

	public static void readResults(String response) {

		JSONObject json = new JSONObject(response);

		JSONArray array = json.getJSONObject("pdResponse").getJSONObject("demands").getJSONArray("demand")
				.getJSONObject(0).getJSONObject("result").getJSONObject("answers").getJSONArray("answer");

		System.out.println("----Resultados----");

		for (int i = 0; i < array.length(); i++) {

			JSONObject temp = new JSONObject();
			temp = array.getJSONObject(i);

			String league = temp.getString("text");
			int id = temp.getInt("id");
			int total = temp.getInt("total");
			int percent = temp.getInt("percent");

			System.out.println(league + " " + "total: " + total + " " + "porcentaje " + percent + "%");

		}

	}

}
