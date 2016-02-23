package by.bsu.famcs.drapegnik.cleancode;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.text.*;
import java.util.*;
import java.util.regex.Pattern;


/**
 * Created by Drapegnik on 15.02.16.
 */
public class UserInteface {
    private static List<JsonMessage> history;
    private static FileWriter log;

    public void startApp() {
        try {
            history = new ArrayList<>();
            log = new FileWriter("logfile.txt");
            write("start app");

            test();

            greeting();

            boolean isContinue = true;
            Scanner sc = new Scanner(System.in);
            do {
                String[] commandList = readCommand(sc);
                String command = commandList[0];
                String firstParam = "";
                String secondParam = "";
                try {
                    firstParam = commandList[1];
                    secondParam = commandList[2];
                } catch (ArrayIndexOutOfBoundsException ex1) {
                }

                try {
                    switch (command) {
                        case "load":
                            loadMessage(firstParam);
                            break;

                        case "save":
                            saveMessage(firstParam);
                            break;

                        case "show": {
                            if (!secondParam.isEmpty()) {
                                showMessages(firstParam, secondParam);
                            } else if (!firstParam.isEmpty()) {
                                showMessages(firstParam, "");
                            } else {
                                showMessages();
                            }
                            break;
                        }

                        case "delete":
                            deleteMessage(firstParam);
                            break;

                        case "searchA":
                            searchMessagesByAuthor(firstParam);
                            break;

                        case "searchK":
                            searchMessagesByKeyWord(firstParam);
                            break;

                        case "searchR":
                            searchMessagesByRegExp(firstParam);
                            break;

                        case "q":
                            isContinue = false;
                            break;

                        default:
                            System.out.println("No such commands, try again!");
                            write("unknown command '" + command + "'");
                            break;
                    }
                } catch (ArrayIndexOutOfBoundsException ex) {
                    System.out.println("Enter correct command!");
                }
            } while (isContinue);

            System.out.println("Bye!");
            write("finish app");
            log.close();
        } catch (IOException ex) {
            write("Some problems with File IO!");
        }
    }

    private void greeting() {
        System.out.println("Hi, you can use:");
        System.out.println("* load/save commands with one sting [filename] param");
        System.out.println("* show/show [from]/show [from] [to]");
        System.out.println("* delete [id]");
        System.out.println("* add [mes] [author]");
        System.out.println("* searchA [author]/searchK [keyword]/searchR [regexp]");
        System.out.println("* q to exit");
        System.out.println("Note: use 15.02.2016-15:04 date format!");
    }

    private void test() throws IOException {
        loadMessage("input.json");
        saveMessage("output.json");
        addMessage("ivan", "lol");
        showMessages();
        addMessage("ivan", "Are you ok? lol");
        searchMessagesByAuthor("User1");
        searchMessagesByKeyWord("Are");
        searchMessagesByRegExp("^User\\w*");
        showMessages("15.02.2015-15:04", "15.02.2016-15:04");
        deleteMessage("46f408b2-72cb-4307-b6e6-95a8515eb7c0");
    }

    private String[] readCommand(Scanner sc) {
        return sc.nextLine().split("\\s+");
    }

    private void write(String message) {
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy-hh:mm:ss");
        try {
            log.write(df.format(new Date(System.currentTimeMillis())) + " - " + message + "\n");
        } catch (IOException ex) {
            System.out.println("Some problems with logfile!");
        }
    }

    public void showMessages() {
        history.forEach((mes) -> System.out.println(mes));
        write("show " + history.size() + " messages");
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

            for (JsonMessage mes : history)
                if (mes.getTimestamp() >= from && mes.getTimestamp() <= to) {
                    System.out.println(mes);
                    count++;
                }

            write("show " + count + " messages by period from " + df.format(from) + " to " + df.format(to));
        } catch (ParseException ex) {
            System.out.println("Some problems with parse date, try again!");
            write("Some problems with parse date in showMessages by period");
        }
    }

    public void loadMessage(String fileaname) throws IOException {
        try (Reader reader = new InputStreamReader(new FileInputStream(fileaname))) {
            Gson gson = new GsonBuilder().create();
            JsonMessage[] temp = gson.fromJson(reader, JsonMessage[].class);
            reader.close();
            history.clear();
            Collections.addAll(history, temp);
        }

        System.out.println("load " + history.size() + " messages from " + fileaname);
        write("load " + history.size() + " messages from " + fileaname);
    }

    public void saveMessage(String filename) throws IOException {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(filename))) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(history, writer);

            writer.close();
        }

        System.out.println("save " + history.size() + " messages to " + filename);
        write("save " + history.size() + " messages to " + filename);
    }

    public void addMessage(String author, String message) {
        history.add(new JsonMessage(author, message));
        write("add '" + message + "' message by " + author);
    }

    public void deleteMessage(String id) {
        for (JsonMessage mes : history) {
            if (mes.getId().equals(id)) {
                history.remove(mes);
                break;
            }
        }

        write("delete message by id=" + id);
    }

    public void searchMessagesByAuthor(String author) {
        int count = 0;
        for (JsonMessage mes : history) {
            if (mes.getAuthor().equals(author)) {
                System.out.println(mes);
                count++;
            }
        }

        write("find " + count + " messages by " + author);
    }

    public void searchMessagesByKeyWord(String keyword) {
        int count = 0;
        for (JsonMessage mes : history) {
            if (mes.getMessage().toLowerCase().contains(keyword.toLowerCase())) {
                System.out.println(mes);
                count++;
            }
        }

        write("find " + count + " messages by '" + keyword + "' keyword");
    }

    public void searchMessagesByRegExp(String regexp) {
        int count = 0;
        for (JsonMessage mes : history) {
            if (Pattern.matches(regexp, mes.getAuthor()) || Pattern.matches(regexp, mes.getId()) || Pattern.matches(regexp, mes.getMessage())) {
                System.out.println(mes);
                count++;
            }
        }

        write("find " + count + " messages by '" + regexp + "' regexp");
    }
}
