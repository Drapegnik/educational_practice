package by.bsu.famcs.drapegnik.cleancode;

import by.bsu.up.chat.common.models.Message;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Drapegnik on 02.04.16.
 */
public class LoaderSaver {
    public static List<Message> loadMessage(String filename) throws IOException {
        List<Message> data = new ArrayList<>();
        try (Reader reader = new InputStreamReader(new FileInputStream(filename))) {
            Gson gson = new GsonBuilder().create();
            Message[] temp = gson.fromJson(reader, Message[].class);
            reader.close();
            if (temp != null)
                Collections.addAll(data, temp);
        }
        return data;
    }

    public static void saveMessage(String filename, List<Message> data) throws IOException {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(filename))) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(data, writer);
        }
    }
}
