package edu.rice.comp504.model.cmd.messagecmd;

import edu.rice.comp504.model.Lobby;
import edu.rice.comp504.model.MsgToClientSender;
import edu.rice.comp504.model.cmd.ICmd;
import edu.rice.comp504.model.message.Message;
import edu.rice.comp504.model.room.RoomAbs;
import edu.rice.comp504.model.user.User;
import org.eclipse.jetty.websocket.api.Session;

import java.util.List;
import java.util.Map;

public class RecallMsgCmd implements ICmd {
    private final Lobby lobby;

    public RecallMsgCmd() {
        lobby = Lobby.makeLobby();
    }

    /**
     * Execute the command to recall a message.
     *
     * @param body Map with keys: RoomId, UserId, MessageId
     */
    @Override
    public void execute(Session session, Map<String, String> body) {
        RoomAbs room = lobby.getRoomMap().get(Integer.parseInt(body.get("currentRoomId")));
        User user = lobby.getSessionUserMap().get(session);
        int messageId = Integer.parseInt(body.get("msgId"));

        try {
            Message message = room.getMessageFromList(messageId);
            if (room.isInRoom(user)) {
                room.recallMessage(message);

                MsgToClientSender.recallMsgEventRes(body.get("eventName"), room, message, user);
            } else {
                MsgToClientSender.errorMessageRes(session, "User is not in the room");
            }
        } catch (Exception e) {
            e.printStackTrace();
            MsgToClientSender.errorMessageRes(session, "Event failed");
        }

    }
}
