/**
 * Created by Drapegnik on 09.02.16.
 */

public class JsonMessage {
    private String id;
    private String author;
    private String timestamp;
    private String message;

    public JsonMessage() {
        this.author = "none";
        this.timestamp = Long.toString(System.currentTimeMillis());
        this.message = "none";
        this.id = (timestamp+author+message).hashCode()+"";
    }

    public JsonMessage(String author, String message) {
        this.author = author;
        this.timestamp = Long.toString(System.currentTimeMillis());
        this.message = message;
        this.id = "#"+(timestamp+author+message).hashCode();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "{" +
                "'id':'" + id + '\'' +
                ", 'author':'" + author + '\'' +
                ", 'timestamp':'" + timestamp +
                ", 'message':'" + message + '\'' +
                '}';
    }
}
