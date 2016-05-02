package by.bsu.famcs.drapegnik;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Drapegnik on 02.05.16.
 */
public class UserHandler {
    private static List<User> userList;
    private static String filename;

    public UserHandler(String filename) {
        userList = new ArrayList<>();
        this.filename = filename;
    }

    public static void loadUsers(String filename) throws IOException {
        try (Reader reader = new InputStreamReader(new FileInputStream(filename))) {
            Gson gson = new GsonBuilder().create();
            User[] temp = gson.fromJson(reader, User[].class);
            reader.close();
            if (temp != null)
                Collections.addAll(userList, temp);
        }

    }

    public static void saveUsers(String filename) throws IOException {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(filename))) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(userList, writer);
        }
    }

    public static boolean checkUser(User obj) {
        try {
            loadUsers(filename);
        } catch (IOException ex) {
            System.out.println("some problems with file IO");
        }
        return userList.contains(obj);
    }

    public static void main(String[] args) {
        new UserHandler("db.json");
        userList.add(new User("admin", "secretpassword"));
        userList.add(new User("Drapegnik", "ololo123"));
        userList.add(new User("User", "1234"));
        try {
            saveUsers("db.json");
        } catch (IOException ex) {
            System.out.println("some problems with file IO");
        }
        System.out.println(userList);
    }
}
