package edu.rice.comp504.model.cmd.messagecmd;

import edu.rice.comp504.model.Lobby;
import edu.rice.comp504.model.MsgToClientSender;
import edu.rice.comp504.model.cmd.ICmd;
import edu.rice.comp504.model.message.Message;
import edu.rice.comp504.model.message.MessageFactory;
import edu.rice.comp504.model.message.MessageType;
import edu.rice.comp504.model.room.RoomAbs;
import edu.rice.comp504.model.user.User;
import org.eclipse.jetty.websocket.api.Session;

import java.util.Map;

public class EditMsgCmd implements ICmd {
    private final Lobby lobby;

    public EditMsgCmd() {
        lobby = Lobby.makeLobby();
    }

    /**
     * Execute the command to edit a message.
     *
     * @param body Map with keys: RoomId, UserId, MessageId, and NewContent
     */
    @Override
    public void execute(Session session, Map<String, String> body) {
        RoomAbs room = lobby.getRoomMap().get(Integer.parseInt(body.get("currentRoomId")));
        User user = lobby.getSessionUserMap().get(session);
        int messageId = Integer.parseInt(body.get("msgId"));
        String content = body.get("content");
        try {
            Message message = room.getMessageFromList(messageId);
            message.setContent(content);
            User sender = message.getSentFrom();
            if (room.isInRoom(user)) {
                MsgToClientSender.editMsgEventRes(body.get("eventName"), session, room, sender, message, messageId, content);
            } else {
                MsgToClientSender.errorMessageRes(session, "User is not in the room");
            }
        } catch (Exception e) {
            MsgToClientSender.errorMessageRes(session, "Event failed");
        }
    }
}
