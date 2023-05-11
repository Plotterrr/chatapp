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

public class QuitRoomCmd implements ICmd {
    private final Lobby lobby;

    public QuitRoomCmd() {
        lobby = Lobby.makeLobby();
    }

    @Override
    public void execute(Session session, Map<String, String> body) {
        User curUser = lobby.getSessionUserMap().get(session);
        RoomAbs room = lobby.getRoomMap().get(curUser.getCurRoom());

        try {
            lobby.quitRoom(curUser.getUserId(), room.getId());
            MsgToClientSender.quiteRoomEventRes(session, body.get("eventName"), room, curUser);
        } catch (Exception e) {
            e.printStackTrace();
            MsgToClientSender.errorMessageRes(session, "Event Failed");
        }
    }
}
