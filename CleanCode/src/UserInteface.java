import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;


/**
 * Created by Drapegnik on 15.02.16.
 */
public class UserInteface {
    private static ArrayList<JsonMessage> data;

    public UserInteface() throws IOException {
        data = new ArrayList<>();
        loadMessage("input.json");
        saveMessage("output.json");
        addMessage("ivan", "lol");
        showMessages();
        System.out.println("!");
        addMessage("ivan", "Are you ok? lol");
        searchMessagesbyKeyWord("Are");
        System.out.println("!");
        searchMessagesbyRegExp("^User \\w*");
        System.out.println("!");

        showMessagesbyTerm("15.02.15 15:04", "15.02.16 15:04");
    }

    public static void showMessages() {
        for (JsonMessage mes : data)
            System.out.println(mes);
    }

    public static void showMessagesbyTerm(String from, String to) {
//        DateFormat df = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
//        try {
//            Date fromDate = df.parse(from);
//            Date toDate;
//            if (!to.isEmpty())
//                toDate = df.parse(to);
//            else
//                toDate = new Date(System.currentTimeMillis());
//            System.out.println(fromDate.getTime());
//            System.out.println(toDate.getTime());
//        } catch (ParseException ex) {
//            System.out.println(ex);
//        }
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
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(filename))) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(data, writer);
            writer.close();
        }
    }

    public static void addMessage(String author, String message) {
        data.add(new JsonMessage(author, message));
    }

    public static void deleteMessage(String id) {
        for (JsonMessage mes : data)
            if (mes.getId().equals(id)) {
                data.remove(mes);
                break;
            }
    }

    public static void searchMessagesbyAuthor(String author) {
        for (JsonMessage mes : data)
            if (mes.getAuthor().equals(author))
                System.out.println(mes);
    }

    public static void searchMessagesbyKeyWord(String keyword) {
        for (JsonMessage mes : data)
            if (mes.getMessage().toLowerCase().contains(keyword.toLowerCase()))
                System.out.println(mes);
    }

    public static void searchMessagesbyRegExp(String regexp) {
        for (JsonMessage mes : data)
            if (Pattern.matches(regexp, mes.getAuthor()) || Pattern.matches(regexp, mes.getId()) || Pattern.matches(regexp, mes.getMessage()))
                System.out.println(mes);
    }

    public static void main(String[] args) throws IOException {
        new UserInteface();

    }
}
