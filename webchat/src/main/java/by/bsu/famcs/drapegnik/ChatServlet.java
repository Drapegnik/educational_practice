package by.bsu.famcs.drapegnik;

import by.bsu.up.lib.*;
import org.json.simple.parser.ParseException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Response;
import java.io.IOException;
import java.util.List;

/**
 * Created by Drapegnik on 28.05.16.
 */
@WebServlet(value = "/chat")
public class ChatServlet extends HttpServlet {
    private MessageStorage messageStorage;

    @Override
    public void init() throws ServletException {
        messageStorage = new InMemoryMessageStorage(getServletContext().getRealPath("/"));
        super.init();
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int index = MessageHelper.parseToken(req.getParameter("token"));
            messageStorage.loadMessages();
            if (index > messageStorage.size()) {
                System.out.println(String.format("Incorrect token in request: %s. Server does not have so many messages", req.getParameter("token")));
                resp.sendError(Constants.RESPONSE_CODE_BAD_REQUEST, String.format("Incorrect token in request: %s. Server does not have so many messages", req.getParameter("token")));
            }
            Portion portion = new Portion(index);
            List<Message> messages = messageStorage.getPortion(portion);
            resp.getWriter().write(MessageHelper.buildServerResponseBody(messages, messageStorage.size()));

        } catch (InvalidTokenException e) {
            System.out.println("Could not parse token\n" + e);
            resp.sendError(Constants.RESPONSE_CODE_BAD_REQUEST, "Could not parse token\n" + e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Message message = MessageHelper.getClientMessage(req.getReader().readLine(), true);
            System.out.println(String.format("Received new message from user: %s", message));
            messageStorage.addMessage(message);
            messageStorage.saveMessages();
            resp.setStatus(Constants.RESPONSE_CODE_OK);
        } catch (ParseException e) {
            System.out.println("Could not parse message.\n" + e);
            resp.sendError(Constants.RESPONSE_CODE_BAD_REQUEST, "Incorrect request body");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Message message = MessageHelper.getClientMessage(req.getReader().readLine(), false);
            if (messageStorage.updateMessage(message)) {
                messageStorage.saveMessages();
                System.out.println(String.format("Editing message by id: %s with text: %s", message.getId(), message.getText()));
                resp.setStatus(Constants.RESPONSE_CODE_OK);
            } else {
                System.out.println(String.format("Problem with editing message by id: %s", message.getId()));
                resp.setStatus(Constants.RESPONSE_CODE_BAD_REQUEST);
            }
        } catch (ParseException e) {
            System.out.println("Could not parse message.\n" + e);
            resp.sendError(Constants.RESPONSE_CODE_BAD_REQUEST, "Incorrect request body");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String id = MessageHelper.getDeleteParam(req.getReader().readLine());
            if (messageStorage.removeMessage(id)) {
                System.out.println(String.format("Removed message by id: %s", id));
                messageStorage.saveMessages();
                resp.setStatus(Constants.RESPONSE_CODE_OK);
            } else {
                System.out.println(String.format("Problem with remove message by id: %s", id));
                resp.sendError(Constants.RESPONSE_CODE_BAD_REQUEST);
            }
        } catch (ParseException e) {
            System.out.println("Could not remove message by id\n" + e);
            resp.sendError(Constants.RESPONSE_CODE_BAD_REQUEST, "Incorrect request body");
        }
    }
}
