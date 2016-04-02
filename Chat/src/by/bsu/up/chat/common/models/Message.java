package by.bsu.up.chat.common.models;

import java.io.Serializable;
import java.util.UUID;

public class Message implements Serializable {

    private UUID id;
    private String author;
    private long timestamp;
    private String text;
    private boolean isDelete;
    private boolean isEdit;

    public Message() {
        this.author = "none";
        this.timestamp = System.currentTimeMillis();
        this.text = "none";
        this.id = UUID.randomUUID();
        this.isDelete = false;
        this.isEdit = false;
    }

    public Message(String author, String text) {
        this.author = author;
        this.timestamp = System.currentTimeMillis();
        this.text = text;
        this.id = UUID.randomUUID();
        this.isDelete = false;
        this.isEdit = false;
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

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    @Override
    public String toString() {
        return "{" +
                "'id':'" + id + '\'' +
                ", 'author':'" + author + '\'' +
                ", 'timestamp':'" + timestamp + '\'' +
                ", 'text':'" + text + '\'' +
                ", 'isDelete':'" + isDelete + '\'' +
                ", 'isEdit':'" + isEdit + '\'' +
                '}';
    }
}
