package edu.rice.comp504.model.cmd.roomcmd;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import edu.rice.comp504.model.Lobby;
import edu.rice.comp504.model.MsgToClientSender;
import edu.rice.comp504.model.cmd.ICmd;
import edu.rice.comp504.model.room.RoomAbs;
import edu.rice.comp504.model.room.RoomType;
import edu.rice.comp504.model.user.User;
import org.eclipse.jetty.websocket.api.Session;

import static j2html.TagCreator.p;

import java.util.Map;

public class JoinRoomCmd implements ICmd {
    private final Lobby lobby;

    public JoinRoomCmd() {
        lobby = Lobby.makeLobby();
    }

    @Override
    public void execute(Session session, Map<String, String> body) {
        try {
            RoomAbs room = lobby.getRoomMap().get(Integer.parseInt(body.get("roomId")));
            User curUser = lobby.getSessionUserMap().get(session);
            System.out.println("Executing " + body.get("eventName") + "CMD" + "; userId: " + curUser.getUserId() + ", roomId: " + room.getId());
            if (room.getType() != RoomType.PUBLIC && room.getAdmin().getUserId() != curUser.getUserId()) {
                MsgToClientSender.errorMessageRes(session, "Your cannot join in a room that is not Public");
            } else if (!room.getInterest().equals("No special interest") && !curUser.getInterest().contains(room.getInterest())) {
                MsgToClientSender.errorMessageRes(session, "Your Interest is not match the room's special interest");
            } else if (lobby.joinRoom(curUser.getUserId(), room.getId())) {
                MsgToClientSender.joinRoomEventRes(body.get("eventName"), room, curUser);
            } else {
                MsgToClientSender.errorMessageRes(session, "You are banned from this room or Room is full or Your are in the room Already");
            }

        } catch (Exception e) {
            e.printStackTrace();
            MsgToClientSender.errorMessageRes(session, "Event Failed");
        }
    }
}
