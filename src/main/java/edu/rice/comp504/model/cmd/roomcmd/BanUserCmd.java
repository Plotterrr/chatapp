package edu.rice.comp504.model.cmd.roomcmd;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import edu.rice.comp504.model.Lobby;
import edu.rice.comp504.model.MsgToClientSender;
import edu.rice.comp504.model.cmd.ICmd;
import edu.rice.comp504.model.room.RoomAbs;
import edu.rice.comp504.model.user.User;
import org.eclipse.jetty.websocket.api.Session;

import java.util.Map;

import static j2html.TagCreator.p;

public class BanUserCmd implements ICmd {
    private final Lobby lobby;

    public BanUserCmd() {
        lobby = Lobby.makeLobby();
    }

    @Override
    public void execute(Session session, Map<String, String> body) {
        User curUser = lobby.getSessionUserMap().get(session);
        User banUser = lobby.getUserMap().get(Integer.parseInt(body.get("banUserId")));
        RoomAbs room = lobby.getRoomMap().get(Integer.parseInt(body.get("currentRoomId")));
        try {
            if (curUser.getUserId() == room.getAdmin().getUserId()) {
                room.addBannedUser(banUser);
                MsgToClientSender.banUserEventRes(body.get("eventName"), room, curUser, banUser);
            } else {
                MsgToClientSender.errorMessageRes(session, curUser.getName() + " is not Admin of the Room");
            }
        } catch (Exception e) {
            e.printStackTrace();
            MsgToClientSender.errorMessageRes(session, "Event Failed");
        }
    }
}
