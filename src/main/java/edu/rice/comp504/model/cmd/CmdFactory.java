package edu.rice.comp504.model.cmd;

import edu.rice.comp504.model.cmd.messagecmd.*;
import edu.rice.comp504.model.cmd.roomcmd.*;
import edu.rice.comp504.model.cmd.usercmd.*;

public class CmdFactory implements ICmdFac {
    private static CmdFactory INSTANCE;

    /**
     * Constructor.
     */
    private CmdFactory() {

    }

    /**
     * Get the singleton CmdFactory.
     *
     * @return CmdFactory
     */
    public static CmdFactory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CmdFactory();
        }
        return INSTANCE;
    }

    @Override
    public ICmd make(String type) {
        ICmd cmd = null;
        switch (type) {
            case "sendMsg":
                cmd = new SendMsgCmd();
                break;
            case "register":
                cmd = new RegisterCmd();
                break;
            case "login":
                cmd = new LoginCmd();
                break;
            case "invite":
                cmd = new InviteCmd();
                break;
            case "inviteUserList":
                cmd = new InviteUserListCmd();
                break;
            case "accept":
                cmd = new AcceptInvitationCmd();
                break;
            case "createRoom":
                cmd = new CreateRoomCmd();
                break;
            case "joinRoom":
                cmd = new JoinRoomCmd();
                break;
            case "joinRoomList":
                cmd = new JoinRoomListCmd();
                break;
            case "switchRoom":
                cmd = new SwitchRoomCmd();
                break;
            case "switchRoomList":
                cmd = new SwitchRoomListCmd();
                break;
            case "quitRoom":
                cmd = new QuitRoomCmd();
                break;
            case "banUser":
                cmd = new BanUserCmd();
                break;
            case "banUserList":
                cmd = new BanUserListCmd();
                break;
            case "reportUser":
                cmd = new ReportUserCmd();
                break;
            case "reportUserList":
                cmd = new ReportUserListCmd();
                break;
            case "getProfile":
                cmd = new GetProfileCmd();
                break;
            case "signout":
                cmd = new SignoutEvent();
                break;

            case "sendPublicMsg":
                cmd = new SendPublicMsgCmd();
                break;
            case "sendPrivateMsg":
                cmd = new SendPrivateMsgCmd();
                break;
            case "sendPrivateMsgList":
                cmd = new SendPrivateMsgListCmd();
                break;

            case "msgDelete":
                cmd = new DeleteMsgCmd();
                break;

            case "msgRecall":
                cmd = new RecallMsgCmd();
                break;

            case "msgEditPre":
                cmd = new EditMsgPreCmd();
                break;
            case "msgEdit":
                cmd = new EditMsgCmd();
                break;

            case "checkRefresh":
                cmd = new NullCmd();
                break;
            default:
                cmd = new NullCmd();
                break;
        }
        return cmd;
    }
}
