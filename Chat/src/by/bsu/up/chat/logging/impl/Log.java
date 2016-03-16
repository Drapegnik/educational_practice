package by.bsu.up.chat.logging.impl;

import by.bsu.up.chat.client.Client;
import by.bsu.up.chat.logging.Logger;
import by.bsu.up.chat.server.ServerHandler;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log implements Logger {

    private static final String TEMPLATE = "[%s] %s";
    private static final String SERVER_LOG_FILE_NAME = "serverlog.txt";
    private static final String CLIENT_LOG_FILE_NAME = "clientlog.txt";

    private String tag;
    private String fileName;

    private Log(Class<?> cls) {
        tag = String.format(TEMPLATE, cls.getName(), "%s");
        if (cls.getName().equals(ServerHandler.class.getName()) || cls.getName().equals(Client.class.getName()))
            fileName = cls.getSimpleName().toLowerCase().substring(0, 6) + "log.txt";
    }

    private void write(String message) {
        if (fileName != null) {
            try (Writer writer = new OutputStreamWriter(new FileOutputStream(fileName, true))) {
                writer.write((new SimpleDateFormat("dd.MM.yyyy-hh:mm:ss")).format(new Date(System.currentTimeMillis())) + " - " + message + "\n");
            } catch (IOException ex) {
                System.out.println("Some problems with logfile IO!");
            }
        }
    }

    @Override
    public void info(String message) {
        System.out.println(String.format(tag, message));
        write(message);
    }

    @Override
    public void error(String message, Throwable e) {
        System.err.println(String.format(tag, message));
        write(message);
        e.printStackTrace(System.err);
    }

    public static Log create(Class<?> cls) {
        return new Log(cls);
    }
}
