package Functions;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Function1 {

    public static void main(String[] args) {
    
    }
    
    
    //Read JSON files
    public static String readJsonFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        return new String(Files.readAllBytes(path));
    }

    //HASHTAGS RELATED FILES
    
    //Display hashtag for both twitter and nonFungible file
    private static void displayUniqueHashtags(String text) {
        TreeSet<String> uniqueHashtags = new TreeSet<>();
        Pattern pattern = Pattern.compile("#\\w+");
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            uniqueHashtags.add(matcher.group());
        }

        System.out.print("Sorted Unique Hashtags: ");
        uniqueHashtags.forEach(hashtag -> System.out.print(hashtag + "\n"));
        System.out.println();
    }
    
    //Show twitter posts based on hashtags
    public static void displayTwitterFilteredPosts(String jsonFilePath, String hashtagToFilter) {
        try {
            String jsonString = readJsonFile(jsonFilePath);
            JsonArray jsonArray = new Gson().fromJson(jsonString, JsonArray.class);

            if (jsonArray == null) {
                System.out.println("Invalid JSON array format.");
                return;
            }

            for (JsonElement element : jsonArray) {
                if (!element.isJsonObject()) {
                    continue;
                }

                JsonObject jsonObject = element.getAsJsonObject();
                JsonElement descElement = jsonObject.get("desc");

                if (descElement == null || !descElement.isJsonPrimitive()) {
                    continue;
                }

                String desc = descElement.getAsString();

                if (containsExactCaseSensitiveText(desc, "#" + hashtagToFilter)) {
                    System.out.println("Post contains #" + hashtagToFilter + ": " + desc);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error processing JSON file: " + e.getMessage());
        }
    }

    //Check method
    private static boolean containsExactCaseSensitiveText(String text, String hashtag) {
        Pattern pattern = Pattern.compile(Pattern.quote(hashtag) + "\\b");
        Matcher matcher = pattern.matcher(text);
        return matcher.find();
    }
    
    //Show blog posts based on hashtags
    private static void displayFilteredHashtagData(String jsonString, String targetHashtag) {
        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(jsonString, JsonArray.class);

        if (jsonArray == null) {
            System.out.println("Invalid JSON array format.");
            return;
        }

        for (JsonElement element : jsonArray) {
            if (!element.isJsonObject()) {
                continue;
            }

            JsonObject jsonObject = element.getAsJsonObject();
            JsonArray hashtagArray = jsonObject.getAsJsonArray("hashtags");

            if (containsHashtag(hashtagArray, targetHashtag)) {
                System.out.println("URL: " + jsonObject.get("url").getAsString());
                System.out.println("Title: " + jsonObject.get("title").getAsString());
                System.out.println("Time: " + jsonObject.get("time").getAsString());
                System.out.println("Hashtags: " + hashtagArray);
                System.out.println("\n");
            }
        }
    }

    //Check method
    private static boolean containsHashtag(JsonArray hashtagArray, String targetHashtag) {
        for (JsonElement hashtagElement : hashtagArray) {
            if (hashtagElement.getAsString().equalsIgnoreCase(targetHashtag)) {
                return true;
            }
        }
        return false;
    }

    //KEYWORDS RELATED FILES
    
    //Display keywords
    private static void displayUniqueKeywords(String jsonString) {
        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(jsonString, JsonArray.class);

        if (jsonArray == null) {
            System.out.println("Invalid JSON array format.");
            return;
        }

        Set<String> uniqueKeywords = new HashSet<>();

        for (JsonElement element : jsonArray) {
            if (!element.isJsonObject()) {
                continue;
            }

            JsonObject jsonObject = element.getAsJsonObject();
            JsonArray keywordsArray = jsonObject.getAsJsonArray("keywords");

            for (JsonElement keywordElement : keywordsArray) {
                uniqueKeywords.add(keywordElement.getAsString());
            }
        }

        System.out.println("Unique Keywords: " + uniqueKeywords);
    }

    
    //Show posts based on keywords
    private static void displayFilteredKeywordData(String jsonString, String targetKeyword) {
        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(jsonString, JsonArray.class);

        if (jsonArray == null) {
            System.out.println("Invalid JSON array format.");
            return;
        }

        for (JsonElement element : jsonArray) {
            if (!element.isJsonObject()) {
                continue;
            }

            JsonObject jsonObject = element.getAsJsonObject();
            JsonArray keywordsArray = jsonObject.getAsJsonArray("keywords");

            if (containsKeyword(keywordsArray, targetKeyword)) {
                System.out.println("URL: " + jsonObject.get("url").getAsString());
                System.out.println("Title: " + jsonObject.get("title").getAsString());
                System.out.println("Content: " + jsonObject.get("content").getAsString());
                System.out.println("Time: " + jsonObject.get("time").getAsString());
                System.out.println("Keywords: " + keywordsArray);
                System.out.println("\n");
            }
        }
    }

    //Check method
    private static boolean containsKeyword(JsonArray keywordsArray, String targetKeyword) {
        for (JsonElement keywordElement : keywordsArray) {
            if (keywordElement.getAsString().equals(targetKeyword)) {
                return true;
            }
        }
        return false;
    }

    

    
    public static void displayJsonArray(String jsonArrayString) {
        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(jsonArrayString, JsonArray.class);

        if (jsonArray != null) {
            for (JsonElement element : jsonArray) {
                if (element.isJsonObject()) {
                    JsonObject jsonObject = element.getAsJsonObject();
                    JsonElement descElement = jsonObject.get("desc");
                    if (descElement != null && descElement.isJsonPrimitive()) {
                        String desc = descElement.getAsString();
                        System.out.println("Description: " + desc);
                    }
                }
            }
        } else {
            System.out.println("Invalid JSON array format.");
        }
    }
}
