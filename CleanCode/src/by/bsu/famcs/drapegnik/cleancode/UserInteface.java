package by.bsu.famcs.drapegnik.cleancode;

import java.io.*;
import java.text.*;
import java.util.*;


/**
 * Created by Drapegnik on 15.02.16.
 */
public class UserInteface {
    private static History history;
    private static FileWriter log;

    public void startApp() {
        try {
            history = new History();
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
                            history.loadMessage(firstParam);
                            break;

                        case "save":
                            history.saveMessage(firstParam);
                            break;

                        case "show": {
                            if (!secondParam.isEmpty()) {
                                history.showMessages(firstParam, secondParam);
                            } else if (!firstParam.isEmpty()) {
                                history.showMessages(firstParam, "");
                            } else {
                                history.showMessages();
                            }
                            break;
                        }

                        case "delete":
                            history.deleteMessage(firstParam);
                            break;

                        case "searchA":
                            history.searchMessagesByAuthor(firstParam);
                            break;

                        case "searchK":
                            history.searchMessagesByKeyWord(firstParam);
                            break;

                        case "searchR":
                            history.searchMessagesByRegExp(firstParam);
                            break;

                        case "add":
                            history.addMessage(firstParam, secondParam);
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
            System.out.println("Some problems with File IO!");
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
        history.loadMessage("input.json");
        history.addMessage("ivan", "lol");
        history.showMessages();
        history.addMessage("ivan", "Are you ok? lol");
        history.searchMessagesByAuthor("User1");
        history.searchMessagesByKeyWord("Are");
        history.searchMessagesByRegExp("^User\\w*");
        history.showMessages("15.02.2015-15:04", "15.02.2016-15:04");
        history.deleteMessage("46f408b2-72cb-4307-b6e6-95a8515eb7c0");
        history.saveMessage("output.json");
    }

    private String[] readCommand(Scanner sc) {
        return sc.nextLine().split("\\s+");
    }

    protected static void write(String message) {
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy-hh:mm:ss");
        try {
            log.write(df.format(new Date(System.currentTimeMillis())) + " - " + message + "\n");
        } catch (IOException ex) {
            System.out.println("Some problems with logfile!");
        }
    }
}
