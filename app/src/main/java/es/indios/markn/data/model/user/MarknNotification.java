package es.indios.markn.data.model.user;

import java.util.Date;

/**
 * Created by imasdetres on 17/04/18.
 */

public class MarknNotification {

    private String author;
    private String title;
    private String body;
    private Date date;


    public MarknNotification(String author, String title, String body, Date date) {
        this.author = author;
        this.title = title;
        this.body = body;
        this.date = date;
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

    public Date getDate() {
        return date;
    }
}
