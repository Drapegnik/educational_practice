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
    private static ArrayList<JsonMessage> data;
    private static FileWriter log;

    public UserInteface() {
        try {
            data = new ArrayList<>();
            log = new FileWriter("logfile.txt");
            write("start app");

            loadMessage("input.json");
            saveMessage("output.json");
            addMessage("ivan", "lol");
            showMessages();
            addMessage("ivan", "Are you ok? lol");
            searchMessagesbyAuthor("User1");
            searchMessagesbyKeyWord("Are");
            searchMessagesbyRegExp("^User\\w*");
            showMessagesbyTerm("15.02.2015-15:04", "15.02.2016-15:04");
            deleteMessage("46f408b2-72cb-4307-b6e6-95a8515eb7c0");

            System.out.println("Hi, you can use:");
            System.out.println("* load/save commands with one sting [filename] param");
            System.out.println("* show/show [from]/show [from] [to]");
            System.out.println("* delete [id]");
            System.out.println("* add [mes] [author]");
            System.out.println("* searchA [author]/searchK [keyword]/searchR [regexp]");
            System.out.println("* q to exit");
            System.out.println("Note: use 15.02.2016-15:04 date format!");
            boolean isContinue = true;
            Scanner sc = new Scanner(System.in);
            do {
                String[] command = sc.nextLine().split("\\s+");
                try {
                    switch (command[0]) {
                        case "load":
                            loadMessage(command[1]);
                            break;
                        case "save":
                            saveMessage(command[1]);
                            break;
                        case "show": {
                            try {
                                showMessagesbyTerm(command[1], command[2]);
                            } catch (ArrayIndexOutOfBoundsException ex1) {
                                try {
                                    showMessagesbyTerm(command[1], "");
                                } catch (ArrayIndexOutOfBoundsException ex2) {
                                    showMessages();
                                }
                            }
                            break;
                        }
                        case "delete":
                            deleteMessage(command[1]);
                            break;
                        case "searchA":
                            searchMessagesbyAuthor(command[1]);
                            break;
                        case "searchK":
                            searchMessagesbyKeyWord(command[1]);
                            break;
                        case "searchR":
                            searchMessagesbyRegExp(command[1]);
                            break;
                        case "q":
                            isContinue = false;
                            break;
                        default:
                            System.out.println("No such commands, try again!");
                            write("unknown command '" + command[0] + "'");
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

    private void write(String message) {
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy-hh:mm:ss");
        try {
            log.write(df.format(new Date(System.currentTimeMillis())) + " - " + message + "\n");
        } catch (IOException ex) {
            System.out.println("Some problems with logfile!");
        }
    }

    public void showMessages() {
        data.forEach((mes) -> System.out.println(mes));
        write("show " + data.size() + " messages");
    }

    public void showMessagesbyTerm(String from, String to) {
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy-hh:mm");
        try {
            int count = 0;
            long fromDate = df.parse(from).getTime();
            long toDate;
            if (!to.isEmpty())
                toDate = df.parse(to).getTime();
            else
                toDate = System.currentTimeMillis();
            for (JsonMessage mes : data)
                if (mes.getTimestamp() >= fromDate && mes.getTimestamp() <= toDate) {
                    System.out.println(mes);
                    count++;
                }
            write("show " + count + " messages by term from " + df.format(fromDate) + " to " + df.format(toDate));
        } catch (ParseException ex) {
            System.out.println("Some problems with parse date, try again!");
            write("Some problems with parse date in showMessagesbyTerm");
        }
    }

    public void loadMessage(String fileaname) throws IOException {
        try (Reader reader = new InputStreamReader(new FileInputStream(fileaname))) {
            Gson gson = new GsonBuilder().create();
            JsonMessage[] temp = gson.fromJson(reader, JsonMessage[].class);
            reader.close();
            data.clear();
            Collections.addAll(data, temp);
        }
        System.out.println("load " + data.size() + " messages from " + fileaname);
        write("load " + data.size() + " messages from " + fileaname);
    }

    public void saveMessage(String filename) throws IOException {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(filename))) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(data, writer);
            writer.close();
        }
        System.out.println("save " + data.size() + " messages to " + filename);
        write("save " + data.size() + " messages to " + filename);
    }

    public void addMessage(String author, String message) {
        data.add(new JsonMessage(author, message));
        write("add '" + message + "' message by " + author);
    }

    public void deleteMessage(String id) {
        for (JsonMessage mes : data)
            if (mes.getId().equals(id)) {
                data.remove(mes);
                break;
            }
        write("delete message by id=" + id);
    }

    public void searchMessagesbyAuthor(String author) {
        int count = 0;
        for (JsonMessage mes : data)
            if (mes.getAuthor().equals(author)) {
                System.out.println(mes);
                count++;
            }
        write("find " + count + " messages by " + author);
    }

    public void searchMessagesbyKeyWord(String keyword) {
        int count = 0;
        for (JsonMessage mes : data)
            if (mes.getMessage().toLowerCase().contains(keyword.toLowerCase())) {
                System.out.println(mes);
                count++;
            }
        write("find " + count + " messages by '" + keyword + "' keyword");
    }

    public void searchMessagesbyRegExp(String regexp) {
        int count = 0;
        for (JsonMessage mes : data)
            if (Pattern.matches(regexp, mes.getAuthor()) || Pattern.matches(regexp, mes.getId()) || Pattern.matches(regexp, mes.getMessage())) {
                System.out.println(mes);
                count++;
            }
        write("find " + count + " messages by '" + regexp + "' regexp");
    }
}
