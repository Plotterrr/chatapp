package edu.rice.comp504.model.cmd.roomcmd;

import edu.rice.comp504.model.Lobby;
import edu.rice.comp504.model.MsgToClientSender;
import edu.rice.comp504.model.cmd.ICmd;
import edu.rice.comp504.model.room.RoomAbs;
import edu.rice.comp504.model.room.RoomType;
import edu.rice.comp504.model.user.User;
import org.eclipse.jetty.websocket.api.Session;

import java.util.Map;

public class SwitchRoomCmd implements ICmd {
    private final Lobby lobby;

    public SwitchRoomCmd() {
        lobby = Lobby.makeLobby();
    }

    @Override
    public void execute(Session session, Map<String, String> body) {
        try {
            RoomAbs room = lobby.getRoomMap().get(Integer.parseInt(body.get("roomId")));
            User curUser = lobby.getSessionUserMap().get(session);
            System.out.println("Executing " + body.get("eventName") + "CMD" + "; userId: " + curUser.getUserId() + ", roomId: " + room.getId());
            if (lobby.switchRoom(curUser.getUserId(), room.getId())) {
                curUser.setCurRoom(room.getId());
                MsgToClientSender.switchRoomEventRes(body.get("eventName"), room, curUser);
            } else {
                MsgToClientSender.errorMessageRes(session, "You are in the room Already or You are Banned from this room");
            }

        } catch (Exception e) {
            e.printStackTrace();
            MsgToClientSender.errorMessageRes(session, "Event Failed");
        }
    }
}
