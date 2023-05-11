package edu.rice.comp504.model.cmd.usercmd;

import com.google.gson.Gson;
import edu.rice.comp504.model.Lobby;
import edu.rice.comp504.model.MsgToClientSender;
import edu.rice.comp504.model.cmd.ICmd;
import edu.rice.comp504.model.user.User;
import org.eclipse.jetty.websocket.api.Session;

import java.util.Arrays;
import java.util.Map;

public class GetProfileCmd implements ICmd {
    private final Lobby lobby;

    public GetProfileCmd() {
        lobby = Lobby.makeLobby();
    }


    @Override
    public void execute(Session session, Map<String, String> body) {
        Gson gson = new Gson();
        try {
            User user = lobby.getSessionUserMap().get(session);
            System.out.println("ddd" + user.getName());
            MsgToClientSender.getProfileRes(body.get("eventName"), user, session);
        } catch (Exception e) {
            e.printStackTrace();
            MsgToClientSender.errorMessageRes(session, "Event Failed");
        }

    }
}
