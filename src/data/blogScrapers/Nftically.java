package data.blogScrapers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Nftically {
    public static void main(String[] args) {
        // Base URL of the blog pages
        String baseUrl = "https://www.nftically.com/blog/page/";

        // Set to store extracted data
        Set<ExtractedData> extractedDataList = new HashSet<>();

        // Iterate over page numbers from 1 to 3
        for (int page = 1; page <= 3; page++) {
            String pageUrl = baseUrl + page;

            try {
                // Fetch the HTML content from the blog page
                Document document = Jsoup.connect(pageUrl).get();

                // Select all <a> tags within div with class "blog-info"
                Elements links = document.select("div.blog-info a");

                // Extract information from each URL
                for (Element link : links) {
                    String url = link.absUrl("href");
                    if (!url.contains("/category") && !url.contains("/author")) {
                        ExtractedData extractedData = extractDataFromUrl(url);
                        extractedDataList.add(extractedData);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Convert the extracted data set to JSON using Gson
        String jsonString = extractedDataListToJson(extractedDataList);

        // Write JSON data to a file named "nftically.json"
        writeJsonToFile(jsonString, ".\\src\\data\\outputData\\blogData\\nftically.json");
    }

    // Helper method to extract data from a URL, including keywords and URL
    private static ExtractedData extractDataFromUrl(String url) {
        try {
            // Fetch the HTML content from the URL
            Document urlDocument = Jsoup.connect(url).get();

            // Extract title, time, and content
            String title = urlDocument.select("h1.page-title").text();
            String time = extractTime(urlDocument.select("div.blog-date li").get(1));
            String content = extractContent(urlDocument);

            // Extract keywords from <span class="saspot-label"> elements
            Set<String> keywords = extractKeywords(urlDocument);

            return new ExtractedData(url, title, time, content, keywords);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Helper method to extract keywords from <span class="saspot-label"> elements
    private static Set<String> extractKeywords(Document document) {
        Elements keywordElements = document.select("span.saspot-label");
        Set<String> keywords = new HashSet<>();

        for (Element element : keywordElements) {
            keywords.add(element.text());
        }

        return keywords;
    }

    // Helper method to extract time from a li element
    private static String extractTime(Element timeElement) {
        return (timeElement != null) ? timeElement.text() : "";
    }

    // Helper method to extract content from <p> and <h2> elements
    private static String extractContent(Document document) {
        Elements contentElements = document.select("div.blog-detail-wrap p, h2.wp-block-heading");
        StringBuilder contentBuilder = new StringBuilder();

        for (Element element : contentElements) {
            contentBuilder.append(element.text()).append("\n");
        }

        return contentBuilder.toString();
    }

    // Helper method to convert extracted data set to JSON using Gson
    private static String extractedDataListToJson(Set<ExtractedData> extractedDataList) {
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
        private final String time;
        private final String content;
        private final Set<String> keywords;

        public ExtractedData(String url, String title, String time, String content, Set<String> keywords) {
            this.url = url;
            this.title = title;
            this.time = time;
            this.content = content;
            this.keywords = keywords;
        }

        public String getUrl() {
            return url;
        }

        public String getTitle() {
            return title;
        }

        public String getTime() {
            return time;
        }

        public String getContent() {
            return content;
        }

        public Set<String> getKeywords() {
            return keywords;
        }
    }
}
