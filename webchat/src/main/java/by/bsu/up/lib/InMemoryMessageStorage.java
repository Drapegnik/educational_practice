package by.bsu.up.lib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class InMemoryMessageStorage implements MessageStorage {

    private static final String DEFAULT_PERSISTENCE_FILE = "messages.srg";
    private static final String HISTORY_FILENAME = "history.json";
    private List<Message> messages;
    private static String path;

    public InMemoryMessageStorage(String absPath) {
        messages = new ArrayList<>();
        path = absPath.substring(0, absPath.length() - 15) + HISTORY_FILENAME;
    }

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
            LoaderSaver.saveMessage(path, messages);
        } catch (IOException ex) {
            System.out.println("Problem with saving history in file" + ex);
        }
    }

    @Override
    public void loadMessages() {
        try {
            messages = LoaderSaver.loadMessage(path);
        } catch (IOException ex) {
            System.out.println("Problem with loading history from file" + ex);
        }
    }

    @Override
    public int size() {
        return messages.size();
    }
}
