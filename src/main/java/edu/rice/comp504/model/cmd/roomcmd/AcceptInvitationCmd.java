package edu.rice.comp504.model.cmd.roomcmd;

import edu.rice.comp504.model.Lobby;
import edu.rice.comp504.model.MsgToClientSender;
import edu.rice.comp504.model.cmd.ICmd;
import edu.rice.comp504.model.room.RoomAbs;
import edu.rice.comp504.model.room.RoomType;
import edu.rice.comp504.model.user.User;
import org.eclipse.jetty.websocket.api.Session;

import java.util.Map;

public class AcceptInvitationCmd implements ICmd {
    private final Lobby lobby;

    public AcceptInvitationCmd() {
        lobby = Lobby.makeLobby();
    }

    @Override
    public void execute(Session session, Map<String, String> body) {
        try {
            RoomAbs room = lobby.getRoomMap().get(Integer.parseInt(body.get("invitationRoomId")));
            User curUser = lobby.getSessionUserMap().get(session);
            System.out.println("Executing " + body.get("eventName") + "CMD" + "; userId: " + curUser.getUserId() + ", roomId: " + room.getId());
            if (lobby.joinRoom(curUser.getUserId(), room.getId())) {
                curUser.setCurRoom(room.getId());
                MsgToClientSender.joinRoomEventRes(body.get("eventName"), room, curUser);
            } else {
                MsgToClientSender.errorMessageRes(session, "Room is full or Your are in the room Already");
            }

        } catch (Exception e) {
            e.printStackTrace();
            MsgToClientSender.errorMessageRes(session, "Event Failed");
        }
    }
}