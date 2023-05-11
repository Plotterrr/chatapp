package edu.rice.comp504.model.cmd.roomcmd;

import edu.rice.comp504.model.Lobby;
import edu.rice.comp504.model.MsgToClientSender;
import edu.rice.comp504.model.cmd.ICmd;
import edu.rice.comp504.model.user.User;
import org.eclipse.jetty.websocket.api.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SendPrivateMsgListCmd implements ICmd {
    private final Lobby lobby;

    public SendPrivateMsgListCmd() {
        lobby = Lobby.makeLobby();
    }

    @Override
    public void execute(Session session, Map<String, String> body) {
        User currUser = lobby.getSessionUserMap().get(session);
        int currUserId = currUser.getUserId();
        int currRoomId = Integer.parseInt(body.get("currentRoomId"));
        List<User> users = new ArrayList<>(lobby.getRoomMap().get(currRoomId).getMembers());
        users.removeIf(i -> i.getUserId() == currUserId);
        MsgToClientSender.privateUserListEventRes(body.get("eventName"), session, lobby, users);
    }
}
