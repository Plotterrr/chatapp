package edu.rice.comp504.adapter;

import com.google.gson.Gson;
import edu.rice.comp504.model.Lobby;
import edu.rice.comp504.model.MsgToClientSender;
import edu.rice.comp504.model.UserDB;
import edu.rice.comp504.model.cmd.CmdFactory;
import edu.rice.comp504.model.cmd.ICmd;
import edu.rice.comp504.model.cmd.ICmdFac;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.util.HashMap;
import java.util.Set;

/**
 * Create a web socket for the server.
 */
@WebSocket
public class WebSocketAdapter {
    private ICmdFac cmdFac;

    /**
     * Open user's session.
     *
     * @param session The user whose session is opened.
     */
    @OnWebSocketConnect
    public void onConnect(Session session) {
        // Lobby.makeLobby().addSessionUser(session, null);
    }

    /**
     * Close the user's session.
     *
     * @param session The use whose session is closed.
     */
    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        Lobby.makeLobby().removeSession(session);
    }

    /**
     * Send a message.
     *
     * @param session The session user sending the message.
     * @param message The message to be sent.
     */
    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        HashMap<String, String> body = new Gson().fromJson(message, HashMap.class);
        ICmd cmd = CmdFactory.getInstance().make(body.get("eventName"));
        System.out.println("onMessage JSON: " + message);
        cmd.execute(session, body);


        //MsgToClientSender.broadcastMessage(UserDB.getUser(session), message);
    }

    public static ICmdFac getCmdFactory() {
        return CmdFactory.getInstance();
    }
}
