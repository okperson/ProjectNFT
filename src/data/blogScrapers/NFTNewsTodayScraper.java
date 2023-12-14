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
import java.util.HashSet;
import java.util.Set;

public class NFTNewsTodayScraper {
    public static void main(String[] args) {
        // URL of the website
        String url = "https://nftnewstoday.com/";

        try {
            // Fetch the HTML content from the URL
            Document document = Jsoup.connect(url).get();

            // Select all relevant <h4>, <h2> tags with specified classes
            Set<String> urls = extractUrls(document, "h4.elementor-post__title a", "h2.elementor-post__title a", "h2.premium-blog-entry-title a");

            // Create a list to store the extracted data
            Set<ExtractedData> extractedDataList = new HashSet<>();

            // Extract data from each URL
            for (String link : urls) {
                // Fetch the HTML content from the URL
                Document urlDocument = Jsoup.connect(link).get();

                // Extract title, content, time, and keywords
                String title = urlDocument.select("h1.elementor-heading-title").text();
                String time = urlDocument.select("span.elementor-icon-list-text.elementor-post-info__item.elementor-post-info__item--type-date").text();
                String content = extractContent(urlDocument);
                Set<String> keywords = extractKeywords(urlDocument, "span.elementor-post-info__terms-list-item");

                // Create an ExtractedData object and add it to the list
                ExtractedData extractedData = new ExtractedData(link, title, content, time, keywords);
                extractedDataList.add(extractedData);
            }

            // Convert the extracted data list to JSON using Gson
            String jsonString = extractedDataListToJson(extractedDataList);

            // Write JSON data to a file named "nftnewstoday.json"
            writeJsonToFile(jsonString, ".\\src\\data\\outputData\\blogData\\nftnewstoday.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper method to extract URLs from specified <h4>, <h2> tags
    private static Set<String> extractUrls(Document document, String... selectors) {
        Set<String> urls = new HashSet<>();

        for (String selector : selectors) {
            Elements links = document.select(selector);
            for (Element link : links) {
                urls.add(link.absUrl("href"));
            }
        }

        return urls;
    }

    // Helper method to extract content from <div class="elementor-widget-container">
    private static String extractContent(Document document) {
        Elements contentElements = document.select("div.elementor-widget-container");
        StringBuilder contentBuilder = new StringBuilder();

        for (Element element : contentElements) {
            // Exclude content inside <ul class="pp-multiple-authors-boxes-ul">
            element.select("ul.pp-multiple-authors-boxes-ul").remove();
            contentBuilder.append(element.text()).append("\n");
        }

        return contentBuilder.toString();
    }

    // Helper method to extract keywords from <span class="elementor-post-info__terms-list-item">
    private static Set<String> extractKeywords(Document document, String selector) {
        Elements keywordElements = document.select(selector);
        Set<String> keywords = new HashSet<>();

        for (Element element : keywordElements) {
            keywords.add(element.text());
        }

        return keywords;
    }

    // Helper method to convert extracted data list to JSON using Gson
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
