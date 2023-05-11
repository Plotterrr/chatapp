package edu.rice.comp504.model.cmd.usercmd;

import edu.rice.comp504.model.Lobby;
import edu.rice.comp504.model.MsgToClientSender;
import edu.rice.comp504.model.cmd.ICmd;
import edu.rice.comp504.model.user.User;
import org.eclipse.jetty.websocket.api.Session;

import java.util.Map;
import java.util.Objects;

public class RegisterCmd implements ICmd {
    private final Lobby lobby;

    public RegisterCmd() {
        lobby = Lobby.makeLobby();
    }

    @Override
    public void execute(Session session, Map<String, String> body) {
        // for test
        // User newUser = lobby.createUser( "", "", 1, "", null);
        boolean userInList = false;
        try {
            for (User u : lobby.getUserMap().values()) {
                if (Objects.equals(u.getName(), body.get("userName"))) {
                    userInList = true;
                    MsgToClientSender.errorMessageRes(session, "Account Already Created");
                    break;
                }
            }
            if (userInList == false) {
                User newUser = lobby.createUser(body.get("userName"), body.get("pwd"), Integer.parseInt(body.get("age")), body.get("school"), null);
                lobby.addSessionUser(session, newUser);
            }
        } catch (Exception e) {
            e.printStackTrace();
            MsgToClientSender.errorMessageRes(session, "Event Failed");
        }


    }
}
