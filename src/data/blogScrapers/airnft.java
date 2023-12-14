package data.blogScrapers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class airnft {
    public static void main(String[] args) {
        // URL of the website
        String url = "https://www.airnfts.com/blog";

        try {
            // Fetch the HTML content from the URL
            Document document = Jsoup.connect(url).get();

            // Select all <a> tags with the specified class
            Elements links = document.select("a.blog-card.w-inline-block");

            // Create a list to store the extracted data
            List<ExtractedData> extractedDataList = new ArrayList<>();

            // Extract data from each link
            for (Element link : links) {
                String absoluteUrl = link.absUrl("href");

                // Fetch the HTML content from the absolute URL
                Document urlDocument = Jsoup.connect(absoluteUrl).get();

                // Extract title, content, time, and keywords
                String title = urlDocument.select("h1.blog-header-h1").text();
                String time = urlDocument.select("p.blog-detail-date").text();
                String content = extractContent(urlDocument);
                Set<String> keywords = extractKeywords(urlDocument, "p:not(.blog-detail-date)");

                // Create an ExtractedData object and add it to the list
                ExtractedData extractedData = new ExtractedData(absoluteUrl, title, content, time, keywords);
                extractedDataList.add(extractedData);
            }

            // Convert the extracted data list to JSON using Gson
            String jsonString = extractedDataListToJson(extractedDataList);

            // Write JSON data to a file named "airnft.json"
            writeJsonToFile(jsonString, ".\\src\\data\\outputData\\blogData\\airnft.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper method to extract content from h2, h3, and p elements
    private static String extractContent(Document document) {
        Elements contentElements = document.select("h2, h3, p:not(.blog-detail-date)");
        StringBuilder contentBuilder = new StringBuilder();

        for (Element element : contentElements) {
            contentBuilder.append(element.text()).append("\n");
        }

        return contentBuilder.toString();
    }

    // Helper method to extract keywords from <a> tags inside <p> elements
    private static Set<String> extractKeywords(Document document, String selector) {
        Elements keywordElements = document.select(selector + " a");
        Set<String> keywords = new HashSet<>();

        for (Element element : keywordElements) {
            keywords.add(element.text());
        }

        return keywords;
    }

    // Helper method to convert extracted data list to JSON using Gson
    private static String extractedDataListToJson(List<ExtractedData> extractedDataList) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(extractedDataList);
    }

    // Helper method to write JSON data to a file
    private static void writeJsonToFile(String json, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Data class to represent the extracted data
    static class ExtractedData {
        private final String url;
        private final String title;
        private final String content;
        private final String time;
        private final Set<String> keywords;

        public ExtractedData(String url, String title, String content, String time, Set<String> keywords) {
            this.url = url;
            this.title = title;
            this.content = content;
            this.time = time;
            this.keywords = keywords;
        }

        public String getUrl() {
            return url;
        }

        public String getTitle() {
            return title;
        }

        public String getContent() {
            return content;
        }

        public String getTime() {
            return time;
        }

        public Set<String> getKeywords() {
            return keywords;
        }
    }
}
