package by.bsu.famcs.drapegnik.cleancode;

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
    public static List<JsonMessage> loadMessage(String filename) throws IOException {
        List<JsonMessage> data = new ArrayList<>();
        try (Reader reader = new InputStreamReader(new FileInputStream(filename))) {
            Gson gson = new GsonBuilder().create();
            JsonMessage[] temp = gson.fromJson(reader, JsonMessage[].class);
            reader.close();
            Collections.addAll(data, temp);
        }
        return data;
    }

    public static void saveMessage(String filename, List<JsonMessage> data) throws IOException {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(filename))) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(data, writer);
        }
    }
}
