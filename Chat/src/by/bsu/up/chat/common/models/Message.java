package by.bsu.up.chat.common.models;

import java.io.Serializable;
import java.util.UUID;

public class Message implements Serializable {

    private String id;
    private String author;
    private long timestamp;
    private String text;
    private String status;

    public Message() {
        this.author = "none";
        this.timestamp = System.currentTimeMillis();
        this.text = "none";
        this.id = UUID.randomUUID().toString();
        this.status = "default";
    }

    public Message(String author, String text) {
        this.author = author;
        this.timestamp = System.currentTimeMillis();
        this.text = text;
        this.id = UUID.randomUUID().toString();
        this.status = "default";
    }

    public Message(String id, String author, long timestamp, String text, String status) {
        this.id = id;
        this.author = author;
        this.timestamp = timestamp;
        this.text = text;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setId() {
        this.id = UUID.randomUUID().toString();
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "{" +
                "'id':'" + id + '\'' +
                ", 'author':'" + author + '\'' +
                ", 'timestamp':'" + timestamp + '\'' +
                ", 'text':'" + text + '\'' +
                ", 'status':'" + status + '\'' +
                '}';
    }
}
