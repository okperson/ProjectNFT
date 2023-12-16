package data.twitterScraper;

import java.util.List;

public class TwitterModel {
    private String desc;
    private String author;
    private String date;
    private List<String> relatedTags;

    public TwitterModel(String desc, String author, String date, List<String> relatedTags) {
        this.desc = desc;
        this.author = author;
        this.date = date;
        this.relatedTags = relatedTags;
    }

    // Add getters and setters as needed
    public String getDesc() {
        return desc;
    }

    @Override
    public String toString() {
        return "TwitterModel{" +
                "desc='" + desc + '\'' +
                ", author='" + author + '\'' +
                ", date='" + date + '\'' +
                ", relatedTags=" + relatedTags +
                '}';
    }
}
