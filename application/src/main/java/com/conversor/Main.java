package com.conversor;

// Import Https
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import java.net.URI;

// Import exceções
import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

// Import Security
import io.github.cdimascio.dotenv.Dotenv;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.conversor.utils.HandlerParaLogger;

// Import classes
import com.conversor.moeda.MoedaAPI;
import com.conversor.moeda.Moedas;
public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        String busca = "";
        // Configuração do logger
        logger.setUseParentHandlers(false);
        Handler handler = new HandlerParaLogger();
        handler.setLevel(Level.ALL);
        logger.addHandler(handler);

        // voltar aqui
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .create();

        // Segurança da API
        Dotenv dotenv = Dotenv.load();
        String apiKey = dotenv.get("API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            logger.severe("API_KEY não encontrada no arquivo .env");
            return;
        }
        String baseURL = "https://v6.exchangerate-api.com/v6/";
        URI minhaUri = URI.create(baseURL + apiKey + "/latest/" + busca);

        // Configuração do cliente HTTP
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(minhaUri)
                .header("Content-Type", "application/json")
                .build();
        
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String json = response.body();
        if (logger.isLoggable(Level.INFO)) {
            logger.info(String.format("Resposta da API: %s", json));
        }

        MoedaAPI minhaMoedaAPI = gson.fromJson(json, MoedaAPI.class);
        if (logger.isLoggable(Level.INFO)) {
            logger.info(String.format("Resposta da API convertida: %s", minhaMoedaAPI));
        }
        
        Moedas minhaMoeda = new Moedas(minhaMoedaAPI);
        if (logger.isLoggable(Level.INFO)) {
            logger.info(String.format("Moeda convertida: %s", minhaMoeda));
        }

        logger.info("Digita o valor a ser convertido: ");
        double valor = scanner.nextDouble();
        
        while (true) {
            logger.info("Bem-vindo ao conversor de moedas!");
            logger.info("1. Converter de Real para Dolar");
            logger.info("2. Converter de Dolar para Real");
            logger.info("3. Converter de Real para Euro");
            logger.info("4. Converter de Euro para Real");
            logger.info("5. Converter de Dolar para Euro");
            logger.info("6. Converter de Euro para Dolar");
            logger.info("7. Sair");
            logger.info("Escolha uma opcao: ");

            int opcao = scanner.nextInt();
            scanner.nextLine(); // Limpar o buffer

            if (opcao == 7) {
                logger.info("Saindo do programa...");
                break;
            }
            switch (opcao) {
                case 1:
                    logger.info("Você escolheu converter de Real para Dólar.");
                    if (logger.isLoggable(Level.INFO)) {
                        busca = "USD";
                        logger.info(String.format("Valor em Dólar: %.2f"));
                    }
                    logger.info(String.format("Valor em Dólar: %.2f"));
                    break;
                case 2:
                    if (logger.isLoggable(Level.INFO)) {
                        logger.info(String.format("Valor em Real: %.2f"));
                    }
                    logger.info("Você escolheu converter de Dólar para Real.");
                    double valorReal = valor * minhaMoeda.getValor();
                    logger.info(String.format("Valor em Real: %.2f", valorReal));
                    break;
                case 3:
                    logger.info("Você escolheu converter de Real para Euro.");
                    // Implementar conversão de Real para Euro
                    break;
                case 4:
                    logger.info("Você escolheu converter de Euro para Real.");
                    // Implementar conversão de Euro para Real
                    break;
                case 5:
                    logger.info("Você escolheu converter de Dólar para Euro.");
                    // Implementar conversão de Dólar para Euro
                case 6:
                    logger.info("Você escolheu converter de Euro para Dólar.");
                    // Implementar conversão de Euro para Dólar
                    break;
                default:
                    logger.warning("Opção inválida. Tente novamente.");
            }
            
        }
    
    }
}