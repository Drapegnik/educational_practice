package by.bsu.famcs.drapegnik.cleancode;

import by.bsu.up.chat.common.models.Message;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by Drapegnik on 23.02.16.
 */
public class History {
    private List<Message> history;

    public History() {
        history = new ArrayList<>();
    }

    public void showMessages() {
        history.forEach((mes) -> System.out.println(mes));
        UserInteface.write("show " + history.size() + " messages");
    }

    public void showMessages(String dateFrom, String dateTo) {
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy-hh:mm");
        try {
            int count = 0;
            long from = df.parse(dateFrom).getTime();
            long to;

            if (!dateTo.isEmpty())
                to = df.parse(dateTo).getTime();
            else
                to = System.currentTimeMillis();

            for (Message mes : history)
                if (mes.getTimestamp() >= from && mes.getTimestamp() <= to) {
                    System.out.println(mes);
                    count++;
                }

            UserInteface.write("show " + count + " messages by period from " + df.format(from) + " to " + df.format(to));
        } catch (ParseException ex) {
            System.out.println("Some problems with parse date, try again!");
            UserInteface.write("Some problems with parse date in showMessages by period");
        }
    }

    public void loadMessage(String filename) throws IOException {
        history = LoaderSaver.loadMessage(filename);
        System.out.println("load " + history.size() + " messages from " + filename);
        UserInteface.write("load " + history.size() + " messages from " + filename);
    }

    public void saveMessage(String filename) throws IOException {
        LoaderSaver.saveMessage(filename, history);
        System.out.println("save " + history.size() + " messages to " + filename);
        UserInteface.write("save " + history.size() + " messages to " + filename);
    }

    public void addMessage(String author, String message) {
        history.add(new Message(author, message));
        UserInteface.write("add '" + message + "' message by " + author);
    }

    public void deleteMessage(String id) {
        for (Message mes : history) {
            if (mes.getId().equals(id)) {
                history.remove(mes);
                break;
            }
        }

        UserInteface.write("delete message by id=" + id);
    }

    public void searchMessagesByAuthor(String author) {
        int count = 0;
        for (Message mes : history) {
            if (mes.getAuthor().equals(author)) {
                System.out.println(mes);
                count++;
            }
        }

        UserInteface.write("find " + count + " messages by " + author);
    }

    public void searchMessagesByKeyWord(String keyword) {
        int count = 0;
        for (Message mes : history) {
            if (mes.getText().toLowerCase().contains(keyword.toLowerCase())) {
                System.out.println(mes);
                count++;
            }
        }

        UserInteface.write("find " + count + " messages by '" + keyword + "' keyword");
    }

    public void searchMessagesByRegExp(String regexp) {
        int count = 0;
        for (Message mes : history) {
            try {
                if (Pattern.matches(regexp, mes.getAuthor()) || Pattern.matches(regexp, mes.getId()) || Pattern.matches(regexp, mes.getText())) {
                    System.out.println(mes);
                    count++;
                }
            } catch (PatternSyntaxException ex) {
                System.out.println("Incorrect regexp! Try again.");
                UserInteface.write("Incorrect regexp '" + regexp + "'");
                break;
            }
        }

        UserInteface.write("find " + count + " messages by '" + regexp + "' regexp");
    }
}
