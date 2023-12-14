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

public class rarible {
    public static void main(String[] args) throws IOException, InterruptedException {
        // Rarible API URL
        String apiUrl = "https://api.rarible.org/v0.1/data/rankings/collections/volume?limit=100";

        // Rarible API key
        String apiKey = "93cbede4-72c2-47b0-aa0a-c9224707c21d";

        // Create the HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("accept", "application/json")
                .header("X-API-KEY", apiKey)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        // Send the HTTP request and get the response
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        // Format and print the JSON response
        String formattedJson = formatJson(response.body());

        // Write the JSON response to a file named "rarible.json"
        writeJsonToFile(formattedJson, ".\\src\\data\\outputData\\apiData\\rarible.json");
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
