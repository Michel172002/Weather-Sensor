package com.sensor;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WeatherThread implements Runnable{
    private String urlGet;
    private Long idCity;
    private final String URL_POST = "http://localhost:8080/weather";

    public WeatherThread(String urlGet, Long idCity){
        this.urlGet = urlGet;
        this.idCity = idCity;
    }

    @Override
    public void run() {
        try{
            HttpClient client = HttpClient.newHttpClient();
            ObjectMapper objectMapper = new ObjectMapper();

            while (true) {
                HttpRequest requestGet = HttpRequest.newBuilder()
                    .GET()
                    .timeout(Duration.ofSeconds(10))
                    .uri(URI.create(urlGet))
                    .build();

                HttpResponse<String> response = client.send(requestGet, HttpResponse.BodyHandlers.ofString());

                Map<String, Object> jsonMap = objectMapper.readValue(response.body(), Map.class);

                if(response.statusCode() == 200){
                    Map<String, Object> newJsonMap = new HashMap<>();
                    newJsonMap.put("idCity", idCity);
                    newJsonMap.put("temp", jsonMap.get("temp"));
                    newJsonMap.put("date", jsonMap.get("date"));
                    newJsonMap.put("description", jsonMap.get("description"));
                    newJsonMap.put("currently", jsonMap.get("currently"));
                    newJsonMap.put("humidity", jsonMap.get("humidity"));
                    newJsonMap.put("rain", jsonMap.get("rain"));
                    newJsonMap.put("sunrise", jsonMap.get("sunrise"));
                    newJsonMap.put("sunset", jsonMap.get("sunset"));
                    newJsonMap.put("moon_phase", jsonMap.get("moon_phase"));
                    String jsonString = objectMapper.writeValueAsString(newJsonMap);

                    HttpRequest requestPost = HttpRequest.newBuilder()
                        .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                        .timeout(Duration.ofSeconds(10))
                        .uri(URI.create(URL_POST))
                        .header("Content-Type", "application/json")
                        .build();

                    HttpResponse<String> postResponse;
                    postResponse = client.send(
                        requestPost, HttpResponse.BodyHandlers.ofString());

                    System.out.println("Resposta do servidor: " + postResponse.body());
                    jsonMap.clear();
                    Thread.sleep(10000);
                }
            }
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }   
    }

    public Long getIdCity() {
        return idCity;
    }

    public void setIdCity(Long idCity) {
        this.idCity = idCity;
    }

    public String getUrlGet() {
        return urlGet;
    }

    public void setUrlGet(String urlGet) {
        this.urlGet = urlGet;
    }   
}
