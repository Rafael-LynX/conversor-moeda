package com.conversor;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;
import java.net.URI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.conversor.moeda.MoedaAPI;
import com.conversor.moeda.Moedas;
public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .create();

        String ApiKey = "860a0c35d9403cdf08b2e7da";
        URI minhaUri = URI.create("https://v6.exchangerate-api.com/v6/860a0c35d9403cdf08b2e7da/latest/USD");

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(minhaUri)
                .header("Content-Type", "application/json")
                .build();
        
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String json = response.body();
        System.out.println(json);

        MoedaAPI minhaMoedaAPI = gson.fromJson(json, MoedaAPI.class);
        System.out.println(minhaMoedaAPI);
        
        Moedas minhaMoeda = new Moedas(minhaMoedaAPI);
        System.out.println("convertendo");
        System.out.println(minhaMoeda);
        
    
    }
}