package com.conversor;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import java.net.URI;
import java.io.IOException;
import com.google.gson.Gson;

import io.github.cdimascio.dotenv.Dotenv;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.conversor.utils.HandlerParaLogger;
import com.conversor.moeda.MoedaAPI;
import com.conversor.moeda.Moedas;


public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Configuração do logger
        logger.setUseParentHandlers(false);
        Handler handler = new HandlerParaLogger();
        handler.setLevel(Level.ALL);
        logger.addHandler(handler);

        // Segurança da API
        Dotenv dotenv = Dotenv.load();
        String apiKey = dotenv.get("API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            logger.severe("API_KEY não encontrada no arquivo .env");
            return;
        }
        logger.info("Bem-vindo ao conversor de moedas!");
        while (true) {
            logger.info("1. Converter de Real para Dolar");
            logger.info("2. Converter de Dolar para Real");
            logger.info("3. Converter de Real para Euro");
            logger.info("4. Converter de Euro para Real");
            logger.info("5. Converter de Dolar para Euro");
            logger.info("6. Converter de Euro para Dolar");
            logger.info("7. Sair");
            logger.info("Escolha uma opcao: ");

            int opcao = scanner.nextInt();

            if (opcao == 7) {
                logger.info("Saindo do programa...");
                break;
            }

            logger.info("Digita o valor a ser convertido: ");
            double valor = scanner.nextDouble();

            String buscaPrimeiraMoeda = "";
            String buscaSegundaMoeda = "";

            switch (opcao) {
                case 1:
                    buscaPrimeiraMoeda = "BRL";
                    buscaSegundaMoeda = "USD";     
                    break;
                case 2:
                    buscaPrimeiraMoeda = "USD";
                    buscaSegundaMoeda = "BRL";
                    break;
                case 3:
                    buscaPrimeiraMoeda = "BRL";
                    buscaSegundaMoeda = "EUR";
                    break;
                case 4:
                    buscaPrimeiraMoeda = "EUR";
                    buscaSegundaMoeda = "BRL";
                    break;
                case 5:
                    buscaPrimeiraMoeda = "USD";
                    buscaSegundaMoeda = "EUR";
                    break;
                case 6:
                    buscaPrimeiraMoeda = "EUR";
                    buscaSegundaMoeda = "USD";
                    break;
                default:
                    logger.warning("Opção inválida. Tente novamente.");
                    continue;
            }
            Moedas resultado = converterTaxaDeCambio(apiKey, buscaPrimeiraMoeda, buscaSegundaMoeda, valor);
            if (resultado != null) {
                if (logger.isLoggable(Level.INFO)) {
                    logger.info("----------------------------------------" );
                    logger.info(String.format("Resultado da conversao: %.2f %s = %.2f %s", valor, buscaPrimeiraMoeda, resultado.getValor(), buscaSegundaMoeda));
                    logger.info("----------------------------------------" );
                }
            } else {
                logger.severe("Erro: Resultado da conversão é nulo.");
            }
            }
        }  
    

    private static Moedas converterTaxaDeCambio(String apiKey, String buscaPrimeiraMoeda, String buscaSegundaMoeda, double valor) {
            try {
                String baseURL = "https://v6.exchangerate-api.com/v6/";
                URI minhaUri = URI.create(baseURL + apiKey + "/latest/" + buscaPrimeiraMoeda);
    
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(minhaUri)
                        .header("Content-Type", "application/json")
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    
                if (response.statusCode() == 200) {
                    Gson gson = new Gson();
                    MoedaAPI moedaAPI = gson.fromJson(response.body(), MoedaAPI.class);
                    
                    Double taxa = moedaAPI.conversion_rates().get(buscaSegundaMoeda);
                    if (taxa == null) {
                        if (logger.isLoggable(Level.SEVERE)) {
                            logger.severe(String.format("Taxa de cambio nao encontrada para %s", buscaSegundaMoeda));          
                        }
                        return null;
                    }

                    double valorConvertido = valor * taxa;
                    Moedas moedaConvertida = new Moedas("Moedas Convertida", buscaSegundaMoeda, valorConvertido);
                    if (logger.isLoggable(Level.INFO)) {
                        logger.info(String.format("Taxa de cambio obtida: 1 %s = %.2f %s", buscaPrimeiraMoeda, taxa, buscaSegundaMoeda));
                    }
                    return moedaConvertida;
                } else {
                    logger.severe(() -> String.format("Erro na API: Código de status %d", response.statusCode()));
                }
            } catch (IOException e) {
                logger.severe("Erro ao acessar a API: " + e.getMessage());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.severe("Thread interrompida: " + e.getMessage());
            } catch (Exception e) {
                logger.severe("Erro inesperado: " + e.getMessage());
            }
            return null; // Valor padrão em caso de erro
        }
    }
    
         

    

