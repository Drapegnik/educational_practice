package by.bsu.up.chat.common.models;

import java.io.Serializable;
import java.util.UUID;

public class Message implements Serializable {

    private UUID id;
    private String author;
    private long timestamp;
    private String text;

    public Message() {
        this.author = "none";
        this.timestamp = System.currentTimeMillis();
        this.text = "none";
        this.id = UUID.randomUUID();
    }

    public Message(String author, String text) {
        this.author = author;
        this.timestamp = System.currentTimeMillis();
        this.text = text;
        this.id = UUID.randomUUID();
    }

    public String getId() {
        return id.toString();
    }

    public void setId(String id) {
        this.id = UUID.fromString(id);
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "{" +
                "'id':'" + id + '\'' +
                ", 'author':'" + author + '\'' +
                ", 'timestamp':'" + timestamp + '\'' +
                ", 'text':'" + text + '\'' +
                '}';
    }
}
