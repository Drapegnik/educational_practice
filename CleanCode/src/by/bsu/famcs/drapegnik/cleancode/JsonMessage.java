package by.bsu.famcs.drapegnik.cleancode;

import java.util.UUID;

/**
 * Created by Drapegnik on 09.02.16.
 */

public class JsonMessage {
    private UUID id;
    private String author;
    private long timestamp;
    private String message;

    public JsonMessage() {
        this.author = "none";
        this.timestamp = System.currentTimeMillis();
        this.message = "none";
        this.id = UUID.randomUUID();
    }

    public JsonMessage(String author, String message) {
        this.author = author;
        this.timestamp = System.currentTimeMillis();
        this.message = message;
        this.id = UUID.randomUUID();
    }

    public String getId() {
        return id.toString();
    }


    public String getAuthor() {
        return author;
    }


    public long getTimestamp() {
        return timestamp;
    }


    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "{" +
                "'id':'" + id + '\'' +
                ", 'author':'" + author + '\'' +
                ", 'timestamp':'" + timestamp + '\'' +
                ", 'message':'" + message + '\'' +
                '}';
    }
}
