package edu.rice.comp504.model.cmd.usercmd;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import edu.rice.comp504.model.Lobby;
import edu.rice.comp504.model.MsgToClientSender;
import edu.rice.comp504.model.cmd.ICmd;
import edu.rice.comp504.model.user.User;
import edu.rice.comp504.model.user.UserFactory;
import org.eclipse.jetty.websocket.api.Session;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class LoginCmd implements ICmd {
    private final Lobby lobby;

    public LoginCmd() {
        lobby = Lobby.makeLobby();
    }


    @Override
    public void execute(Session session, Map<String, String> body) {
        Gson gson = new Gson();
        try {
          /*  String userName = body.get("userName");
            System.out.println(userName);

            User newUser = lobby.createUser(userName, "", 21, "Rice", Arrays.asList("a", "b"));
            lobby.addSessionUser(session, newUser);
            System.out.println(body.get("eventName") + "CMD" + "; userId: " + newUser.getUserId());
            MsgToClientSender.loginEventRes(session, newUser); */

            String userName = body.get("userName");
            String pwd = body.get("pwd");
            User user = UserFactory.getOnly().make(0, null, null, 0, null, null);
            boolean userInList = false;

            for (User u : lobby.getUserMap().values()) {
                if (Objects.equals(u.getName(), userName) && Objects.equals(u.getPwd(), pwd)) {
                    user = u;
                    userInList = true;
                    break;
                }
            }
            if (userInList == true) {
                lobby.addSessionUser(session, user);
                MsgToClientSender.loginEventRes(session, user);
            } else {
                MsgToClientSender.errorMessageRes(session, "No Account Detected");
            }

        } catch (Exception e) {
            e.printStackTrace();
            MsgToClientSender.errorMessageRes(session, "Event Failed");
        }

    }
}
