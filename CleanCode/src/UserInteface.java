import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.*;

/**
 * Created by Drapegnik on 15.02.16.
 */
public class UserInteface {
    private static ArrayList<JsonMessage> data = new ArrayList<>();

    public static void showMessages() {
        for (JsonMessage mes : data)
            System.out.println(mes);
    }

    public static void loadMessage(String fileaname) throws IOException {
        try (Reader reader = new InputStreamReader(new FileInputStream(fileaname))) {
            Gson gson = new GsonBuilder().create();
            JsonMessage[] temp = gson.fromJson(reader, JsonMessage[].class);
            reader.close();
            data.clear();
            Collections.addAll(data, temp);
        }
    }

    public static void saveMessage(String filename) throws IOException {
        try(Writer writer = new OutputStreamWriter(new FileOutputStream(filename))){
            Gson gson = new GsonBuilder().create();
            gson.toJson(data, writer);
            writer.close();
        }
    }

    public static void addMessage(String author, String message) {
        data.add(new JsonMessage(author,message));
    }

    public static void deleteMessage(String id) {
        for (JsonMessage mes : data)
            if (mes.getId().equals(id)) {
                data.remove(mes);
                break;
            }
    }


    public static void main(String[] args) throws IOException {
        UserInteface.loadMessage("input.json");
        UserInteface.saveMessage("output.json");
        UserInteface.addMessage("ivan","lol");
        UserInteface.showMessages();
        UserInteface.deleteMessage("92dff7ee-00d7-41e5-a3db-e7189963ee3e");
        UserInteface.showMessages();
    }
}
