package data.apiFetcher;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class opensea {
    public static void main(String[] args) throws IOException, InterruptedException {
        // OpenSea API URL
        String apiUrl = "https://api.opensea.io/api/v2/collections?chain_identifier=ethereum&include_hidden=false&limit=100";

        // OpenSea API key
        String apiKey = "2a06e62f0c094e81a5ead560e45433ad";

        // Create the HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("accept", "application/json")
                .header("x-api-key", apiKey)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        // Send the HTTP request and get the response
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        // Format and print the JSON response
        String formattedJson = formatJson(response.body());

        // Write the JSON response to a file named "opensea.json"
        writeJsonToFile(formattedJson, ".\\src\\data\\outputData\\apiData\\opensea.json");
    }

    private static String formatJson(String jsonString) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
    }

    private static void writeJsonToFile(String json, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
