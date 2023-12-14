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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NonFungible {
    public static void main(String[] args) {
        // URL of the OpenSea blog
        String url = "https://nonfungible.com/news";

        try {
            // Fetch the HTML content from the URL
            Document document = Jsoup.connect(url).get();

            // Select all <a> tags with the specified class
            Elements links = document.select("a.MuiTypography-root.MuiTypography-inherit.MuiLink-root.MuiLink-underlineHover.css-3pyhqk");

            // Store URLs in a Set to avoid duplicates
            Set<String> urlSet = new HashSet<>();

            // Add absolute URLs containing "/news" to the set
            for (Element link : links) {
                String absoluteUrl = link.absUrl("href");
                if (absoluteUrl.contains("/news")) {
                    urlSet.add(absoluteUrl);
                }
            }

            // Create a list to store the extracted data
            List<ExtractedData> extractedDataList = new ArrayList<>();

            // Extract data from each URL
            for (String uniqueUrl : urlSet) {
                try {
                    // Fetch the HTML content from the URL
                    Document urlDocument = Jsoup.connect(uniqueUrl).get();

                    // Extract title, time, content, and hashtags
                    String title = urlDocument.select("h1.MuiTypography-root.MuiTypography-h1.css-3ker6m").text();
                    String time = formatDate(urlDocument.select("span.MuiBox-root.css-k008qs").text());
                    String content = extractContent(urlDocument);

                    // Extract hashtags from <div class="MuiPaper-root MuiPaper-elevation MuiPaper-elevation5 css-1cin1l4">
                    Set<String> hashtags = extractHashtags(urlDocument);

                    // Create an ExtractedData object and add it to the list
                    ExtractedData extractedData = new ExtractedData(uniqueUrl, title, time, content, hashtags);
                    extractedDataList.add(extractedData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // Convert the extracted data list to JSON using Gson
            String jsonString = extractedDataListToJson(extractedDataList);

            // Write JSON data to a file named "nonFungible.json"
            writeJsonToFile(jsonString, ".\\src\\data\\outputData\\blogData\\nonFungible.json");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper method to extract content from <p> and <h2> elements
    private static String extractContent(Document document) {
        Elements contentElements = document.select("p.MuiTypography-root.MuiTypography-body1.css-1r4bxmx, h2.MuiTypography-root.MuiTypography-h2.css-1yqyu40");
        StringBuilder contentBuilder = new StringBuilder();

        for (Element element : contentElements) {
            contentBuilder.append(element.text()).append("\n");
        }

        return contentBuilder.toString();
    }

    // Helper method to extract hashtags from <div class="MuiPaper-root MuiPaper-elevation MuiPaper-elevation5 css-1cin1l4">
    private static Set<String> extractHashtags(Document document) {
        Elements hashtagElements = document.select("div.MuiPaper-root.MuiPaper-elevation.MuiPaper-elevation5.css-1cin1l4");
        Set<String> hashtags = new HashSet<>();

        for (Element element : hashtagElements) {
            hashtags.add(element.text());
        }

        return hashtags;
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

    // Helper method to format the date
    private static String formatDate(String rawTime) {
        // Extract the date part from the raw time string
        String datePart = rawTime.split("\\s+")[1];

        // Parse and format the date
        try {
            java.util.Date date = new java.text.SimpleDateFormat("MM/dd/yy").parse(datePart);
            return new java.text.SimpleDateFormat("MMMM dd, yyyy").format(date);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            return rawTime;
        }
    }

    // Data class to represent the extracted data
    static class ExtractedData {
        private final String url;
        private final String title;
        private final String time;
        private final String content;
        private final Set<String> hashtags;

        public ExtractedData(String url, String title, String time, String content, Set<String> hashtags) {
            this.url = url;
            this.title = title;
            this.time = time;
            this.content = content;
            this.hashtags = hashtags;
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

        public Set<String> getHashtags() {
            return hashtags;
        }
    }
}
