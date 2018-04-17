package es.indios.markn.data.model.user;

/**
 * Created by imasdetres on 17/04/18.
 */

public class MarknNotification {

    private String author;
    private String title;
    private String body;


    public MarknNotification(String author, String title, String body) {
        this.author = author;
        this.title = title;
        this.body = body;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }
}
