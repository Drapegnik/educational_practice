package by.bsu.up.chat.storage;

import by.bsu.famcs.drapegnik.cleancode.Message;
import by.bsu.up.chat.logging.Logger;
import by.bsu.up.chat.logging.impl.Log;
import by.bsu.famcs.drapegnik.cleancode.LoaderSaver;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class InMemoryMessageStorage implements MessageStorage {

    private static final String DEFAULT_PERSISTENCE_FILE = "messages.srg";
    private static final String HISTORY_FILENAME = "history.json";

    private static final Logger logger = Log.create(InMemoryMessageStorage.class);

    private List<Message> messages = new ArrayList<>();

    @Override
    public synchronized List<Message> getPortion(Portion portion) {
        int from = portion.getFromIndex();
        if (from < 0) {
            throw new IllegalArgumentException(String.format("Portion from index %d can not be less then 0", from));
        }
        int to = portion.getToIndex();
        if (to != -1 && to < portion.getFromIndex()) {
            throw new IllegalArgumentException(String.format("Porting last index %d can not be less then start index %d", to, from));
        }
        to = Math.max(to, messages.size());
        return messages.subList(from, to);
    }

    @Override
    public void addMessage(Message message) {
        messages.add(message);
    }

    @Override
    public boolean updateMessage(Message message) {
        for (Message mes : messages)
            if (mes.getId().equals(message.getId()) && !mes.getStatus().equals("delete")) {
                addMessage(new Message(mes.getId(), mes.getAuthor(), System.currentTimeMillis(), message.getText(), "edit"));
                return true;
            }
        return false;
    }

    @Override
    public synchronized boolean removeMessage(String messageId) {
        for (Message mes : messages)
            if (mes.getId().equals(messageId) && !mes.getStatus().equals("delete")) {
                addMessage(new Message(mes.getId(), mes.getAuthor(), System.currentTimeMillis(), "Message was delete", "delete"));
                return true;
            }
        return false;
    }

    @Override
    public void saveMessages() {
        try {
            LoaderSaver.saveMessage(HISTORY_FILENAME, messages);
        } catch (IOException ex) {
            logger.error("Problem with saving history in file", ex);
        }
    }

    @Override
    public void loadMessages() {
        try {
            messages = LoaderSaver.loadMessage(HISTORY_FILENAME);
        } catch (IOException ex) {
            logger.error("Problem with loading history from file", ex);
        }
    }

    @Override
    public int size() {
        return messages.size();
    }
}
