var tableContextMenu = null;

const webSocket = window.location.protocol === 'http:' ?
    new WebSocket("ws://" + location.hostname + ":" + location.port + "/chatapp") :
    new WebSocket("wss://" + "chatapp-final-team-k.herokuapp.com/chatapp");

let userName = "";
let userId = "";
let pass = "";

window.onload = function () {
    hideMsgButton();
    userName = JSON.parse(sessionStorage.getItem("user")).user;
    userId = JSON.parse(sessionStorage.getItem("userId")).userId;
    pass = JSON.parse(sessionStorage.getItem("pass")).pass;

    webSocket.onclose = () => console.log("WebSocket connection closed");

    webSocket.onmessage = (msg) => eventCheck(msg);

    let msgJson = {eventName: "checkRefresh"};
    webSocket.send(JSON.stringify(msgJson));

    let userObj = {userName: userName, userId: userId, pwd: pass, eventName: "login"};
    console.log(userObj);
    webSocket.send(JSON.stringify(userObj));
}

function addEmoji(p) {
    let old = $('#inputMsg').val();
    $("#inputMsg").val(old+p)

}
function createRoom() {
    let publicOrPrivate = $("input[name=publicOrPrivate]:checked").val();
    let capacity = $("#capacity").val();
    console.log(capacity);

    $.post("/createRoom", {publicOrPrivate: publicOrPrivate, capacity: capacity}, function (data) {
        console.log(data);
    }, "json");
}

function getRoomUserList() {
    let currentRoomId = $("#currentRoom").text().split("ROOM-")[1];
    console.log(currentRoomId);
    let div = $("#banUserDiv");
    let ul = $('<ul/>')
        .attr('id', 'banUserList')
        .appendTo(div);
    $.post("/getRoomUserList", {roomID: currentRoomId}, function (data) {
        let members = data.members;
        let index = 0;
        members.forEach(user => {
            let li = $('<li/>').appendTo(ul);
            let inputRadio = $('<input/>')
                .attr('type', 'radio')
                .attr('id', 'user' + index)
                .attr('name', 'userList')
                .val(user.name)
                .appendTo(li);
            inputRadio.after(' ');
            let label = $('<label/>')
                .attr('for', 'user' + index)
                .text("User: " + user.name)
                .appendTo(li)
            index += 1;
        })

    }, "json");
}

function sendEvent(eventName) {
    let msgJson = {};
    let roomId = undefined;
    let currentRoomId = $("#currentRoom").text().split("ROOM-")[1];
    switch (eventName) {
        case "createRoom":
            console.log("createRoom");
            let publicOrPrivate = $("input[name=publicOrPrivate]:checked").val();
            let capacity = $("#capacity").val();
            let interest = $("#interest").val();
            msgJson = {publicOrPrivate: publicOrPrivate, capacity: capacity, interest: interest, eventName: eventName}
            break;
        case "joinRoom":
            console.log("joinRoom");
            roomId = $("input[name=joinRoomList]:checked").val();
            msgJson = {roomId: roomId, eventName: eventName};
            break;
        case "joinRoomList":
            console.log("joinRoomList");
            msgJson = {eventName: eventName};
            break;
        case "getProfile":
            console.log("joinRoomList");
            msgJson = {eventName: eventName};
            break;
        case "switchRoom":
            console.log("switchRoom");
            roomId = $("input[name=switchRoomList]:checked").val();
            msgJson = {roomId : roomId, eventName: eventName};
            break;
        case "switchRoomList":
            console.log("switchRoomList");
            msgJson = {eventName: eventName};
            break;
        case "invite":
            console.log("invite");
            let inviteUserId = $("input[name=inviteUserList]:checked").val();
            console.log("joinRoomList " + inviteUserId);
            msgJson = {inviteUserId: inviteUserId, eventName: eventName};
            break;
        case "inviteUserList":
            console.log("inviteUserList");
            msgJson = {eventName: eventName};
            break;
        case "accept":
            console.log("accept");
            let invitationRoomId = $("#invitationRoom").val();
            console.log("accept Room " + invitationRoomId);
            msgJson = {invitationRoomId: invitationRoomId, eventName: eventName}
            $("#invitationRoom").remove();
            $("#invitationMessage").remove();
            break;
        case "banUser":
            console.log("invite");
            let banUserId = $("input[name=banUserList]:checked").val();
            console.log("banUserId " + banUserId);
            msgJson = {banUserId: banUserId, currentRoomId: currentRoomId, eventName: eventName};
            break;
        case "banUserList":
            console.log("banUser");
            msgJson = {currentRoomId: currentRoomId, eventName: eventName};
            break;
        case "reportUser":
            console.log("reportUser");
            let reportedUserId = $("input[name=reportUserList]:checked").val();
            msgJson = {reportedUserId: reportedUserId, eventName: eventName};
            break;
        case "acceptReport":
            console.log("acceptReport");
            let reportedUserId1 = $("#reportedUser").val();
            console.log("accept Report to  " + reportedUserId1);
            $("#reportedUser").remove();
            $("#reportMessage").remove();
            msgJson = {banUserId: reportedUserId1, currentRoomId: currentRoomId, eventName: "banUser"}
            break;
        case "reportUserList":
            console.log("banUser");
            msgJson = {currentRoomId: currentRoomId, eventName: eventName};
            break;
        case "quitRoom":
            console.log("quitRoom");
            msgJson = {eventName: eventName};
            break;
        case "sendPublicMsg":
            console.log("sendPublicMsg");
            let publicContent = $("#inputMsg").val();
            if (publicContent == null || publicContent === "") {
                console.log("Get empty content from text box");
                return ;
            }
            let publicMessageType = "public";
            console.log("content:"+publicContent);
            $("#inputMsg").val("");
            msgJson = {
                privateReceiver: "",
                currentRoomId: currentRoomId,
                content: publicContent,
                messageType: publicMessageType,
                eventName: eventName,
            };
            break;
        case "sendMsg":
            console.log("sendPublicMsg");
            let content = $("#inputMsg").val();
            if (content == null || content === "") {
                console.log("Get empty content from text box");
                return ;
            }
            let sendTo = $("#usersInRoom").val();
            console.log(sendTo);
            let messageType = "public";
            let currMsgEvenName = "sendPublicMsg";
            let privateReceiver = sendTo;
            if (sendTo === "public") {
                messageType = "public";
                currMsgEvenName = "sendPublicMsg";
            } else {
                messageType = "private";
                currMsgEvenName = "sendPrivateMsg";
            }
            console.log("content:"+content);
            $("#inputMsg").val("");
            msgJson = {
                privateReceiver: privateReceiver,
                currentRoomId: currentRoomId,
                content: content,
                messageType: messageType,
                eventName: currMsgEvenName,
            };
            break;
        case "sendPrivateMsgList":
            console.log("sendPrivateMsgList");
            msgJson = {currentRoomId: currentRoomId, eventName: eventName};
            break;
        case "signout":
            console.log("signout");
            msgJson = {eventName: eventName};
            break;
        default:
            break;
    }
    webSocket.send(JSON.stringify(msgJson));
}



function closeScreen() {
    $("#joinRoomList").remove();
    $("#inviteUserList").remove();
    $("#banUserList").remove();
    $("#reportUserList").remove();
    $("#privateUserList").remove();

    $("#errorMessage").remove();
}

function eventCheck(msg) {
    closeScreen();
    console.log("EventCheck")
    let data = JSON.parse(msg.data);
    let currentRoom = $("#currentRoom");
    let chatBoxOuterDiv = $("#currentRoomChatBox");
    let chatBox = $("<ul/>")
        .addClass("m-b-0")
        .attr('id', 'currentRoomChatBoxUl')
        .appendTo(chatBoxOuterDiv);
    let chatBoxLi = $('<li/>')
        .addClass('clearfix');
    let chatBoxDiv = $('<div/>')
        .addClass('message-data text-right notification')
        .appendTo(chatBoxLi);
    let chatBoxSpan = $('<span/>');
    switch (data.resType) {
        case "createRoom":
            chatBox = clearHistoryMsg(chatBox, chatBoxOuterDiv);
            checkAdmin(JSON.parse(data.room), JSON.parse(data.user));
            let createRoom = JSON.parse(data.room);
            currentRoom.text("ROOM-" + createRoom.id);
            generatePList(createRoom);
            chatBoxLi.appendTo(chatBox);
            chatBoxSpan.text(data.notification).appendTo(chatBoxDiv);

            showMsgButton();
            break;
        case "joinRoom":
            chatBox = clearHistoryMsg(chatBox, chatBoxOuterDiv);
            console.log("users in room: ", data.userList);
            checkAdmin(JSON.parse(data.room), JSON.parse(data.user));
            let joinRoom = JSON.parse(data.room);
            currentRoom.text("ROOM-" + joinRoom.id);
            generatePList(joinRoom);
            $("#joinRoomList").remove();
            console.log(data.notification);
            chatBoxLi.appendTo(chatBox);
            chatBoxSpan.text(data.notification).appendTo(chatBoxDiv);

            showMsgButton();
            break;
        case "joinRoomList":
            let joinRoomList = JSON.parse(data.joinRoomList);
            console.log(joinRoomList);
            let joinRoomIndex = 0;
            let joinRoomDiv = $("#joinRoomDiv");
            let joinRoomUl = $('<ul/>')
                .attr('id', 'joinRoomList')
                .appendTo(joinRoomDiv);
            joinRoomList.forEach(room => {
                let li = $('<li/>').appendTo(joinRoomUl);
                let inputRadio = $('<input/>')
                    .attr('type', 'radio')
                    .attr('id', 'joinRoom' + joinRoomIndex)
                    .attr('name', 'joinRoomList')
                    .val(room.id)
                    .appendTo(li);
                inputRadio.after(' ');
                let label = $('<label/>')
                    .attr('for', 'joinRoom' + joinRoomIndex)
                    .text("RoomID: " + room.id + " with Interest: " + room.interest)
                    .appendTo(li)
                joinRoomIndex += 1;
            });
            break;
        case "switchRoom":
            chatBox = clearHistoryMsg(chatBox, chatBoxOuterDiv);
            console.log("users in room: ", data.userList);
            checkAdmin(JSON.parse(data.room), JSON.parse(data.user));
            let switchRoom = JSON.parse(data.room);
            currentRoom.text("ROOM-" + switchRoom.id);
            generatePList(switchRoom);
            $("#switchRoomList").remove();
            console.log(data.notification);
            chatBoxLi.appendTo(chatBox);
            chatBoxSpan.text(data.notification).appendTo(chatBoxDiv);

            showMsgButton();
            break;
        case "switchRoomList":
            let switchRoomList = JSON.parse(data.switchRoomList);
            console.log(switchRoomList);
            let switchRoomIndex = 0;
            let switchRoomDiv = $("#switchRoomDiv");
            let switchRoomUl = $('<ul/>')
                .attr('id', 'joinRoomList')
                .appendTo(switchRoomDiv);
            switchRoomList.forEach(room => {
                let li = $('<li/>').appendTo(switchRoomUl);
                let inputRadio = $('<input/>')
                    .attr('type', 'radio')
                    .attr('id', 'switchRoom'+switchRoomIndex)
                    .attr('name', 'switchRoomList')
                    .val(room.id)
                    .appendTo(li);
                inputRadio.after(' ');
                let label = $('<label/>')
                    .attr('for', 'switchRoom'+switchRoomIndex)
                    .text("Room: " + room.id)
                    .appendTo(li)
                switchRoomIndex += 1;
            });
            break;
        case "invite":
            console.log("users in room: ", data.userList);
            $("#inviteUserList").remove();
            $("#invitationMessage").remove();
            let invitation = data.invitation;
            let room = JSON.parse(data.room);
            console.log(invitation);
            let invitationDiv = $("#invitationDiv");
            let invitationMessage = $('<h/>')
                .attr('id', 'invitationMessage')
                .text(invitation)
                .appendTo(invitationDiv);
            let invitationRoom = $('<a/>')
                .attr('id', 'invitationRoom')
                .val(room.id)
                .text(room.id)
                .appendTo(invitationDiv)
            $("#invitationModal").modal('show');
            break;
        case "inviteUserList":
            let inviteUserList = JSON.parse(data.inviteUserList);
            console.log(inviteUserList)
            let inviteUserIndex = 0;
            let inviteUserDiv = $("#inviteUserDiv");
            let inviteUserUl = $('<ul/>')
                .attr('id', 'inviteUserList')
                .appendTo(inviteUserDiv);
            inviteUserList.forEach(user => {
                let li = $('<li/>').appendTo(inviteUserUl);
                let inputRadio = $('<input/>')
                    .attr('type', 'radio')
                    .attr('id', 'inviteUser' + inviteUserIndex)
                    .attr('name', 'inviteUserList')
                    .val(user.userId)
                    .appendTo(li);
                inputRadio.after(' ');
                let label = $('<label/>')
                    .attr('for', 'inviteUser' + inviteUserIndex)
                    .text("User: " + user.name)
                    .appendTo(li)
                inviteUserIndex += 1;
            });
            break;
        case "getProfile":
            console.log("get profile");
            let user = JSON.parse(data.user);
            $("#username").html(user.name)
            $("#age").html(user.age)
            $("#password").html(user.pwd)
            $("#school").html(user.school)
            $("#interest1").html(user.interest.join('; '))
            break;
        case "accept":
            console.log("users in room: ", data.userList);
            checkAdmin(JSON.parse(data.room), JSON.parse(data.user));
            let acceptedInvitationRoom = JSON.parse(data.room);
            currentRoom.text("ROOM-" + acceptedInvitationRoom.id);
            generatePList(acceptedInvitationRoom);
            $("#joinRoomList").remove();
            console.log(data.notification);
            let acceptedUser = JSON.parse(data.user);
            chatBoxLi.appendTo(chatBox);
            chatBoxSpan.text("Admin invite " + acceptedUser.name + " to the room").appendTo(chatBoxDiv);

            showMsgButton();
            break;
        case "login":
            console.log("user log in: ", data.user);
            break;
        case "banUser":
            console.log("ban a user");
            $("#banUserList").remove();
            console.log(data.notification);
            generatePList(JSON.parse(data.room));
            chatBoxLi.appendTo(chatBox);
            chatBoxSpan.text(data.notification).appendTo(chatBoxDiv);
            break;
        case "banned":
            console.log("you are banned");
            console.log(data.notification);
            chatBoxLi.appendTo(chatBox);
            chatBoxSpan.text(data.notification).appendTo(chatBoxDiv);
            break;
        case "banUserList":
            let banUserList = JSON.parse(data.banUserList);
            console.log(banUserList)
            let banIndex = 0;
            let banDiv = $("#banUserDiv");
            let banUl = $('<ul/>')
                .attr('id', 'banUserList')
                .appendTo(banDiv);
            banUserList.forEach(user => {
                let li = $('<li/>').appendTo(banUl);
                let inputRadio = $('<input/>')
                    .attr('type', 'radio')
                    .attr('id', 'banUser' + banIndex)
                    .attr('name', 'banUserList')
                    .val(user.userId)
                    .appendTo(li);
                inputRadio.after(' ');
                let label = $('<label/>')
                    .attr('for', 'banUser' + banIndex)
                    .text("User: " + user.name)
                    .appendTo(li)
                banIndex += 1;
            });
            break;
        case "reportUser":
            console.log("report a user");
            $("#reportUserList").remove();

            console.log(data.reportedUser);
            let report = data.report;
            let reportedUser = JSON.parse(data.reportedUser);
            console.log(report);
            let accReportDiv = $("#reportDiv");
            let reportMessage = $('<h/>')
                .attr('id', 'reportMessage')
                .text(report)
                .appendTo(accReportDiv);
            let reportedUserDiv = $('<a/>')
                .attr('id', 'reportedUser')
                .val(reportedUser.userId)
                .appendTo(accReportDiv)
            $("#reportModal").modal('show');

            break;
        case "reportUserList":
            let reportUserList = JSON.parse(data.reportUserList);
            console.log(reportUserList)
            let reportIndex = 0;
            let reportDiv = $("#reportUserDiv");
            let reportUl = $('<ul/>')
                .attr('id', 'reportUserList')
                .appendTo(reportDiv);
            reportUserList.forEach(user => {
                let li = $('<li/>').appendTo(reportUl);
                let inputRadio = $('<input/>')
                    .attr('type', 'radio')
                    .attr('id', 'reportUser' + reportIndex)
                    .attr('name', 'reportUserList')
                    .val(user.userId)
                    .appendTo(li);
                inputRadio.after(' ');
                let label = $('<label/>')
                    .attr('for', 'reportUser' + reportIndex)
                    .text("User: " + user.name)
                    .appendTo(li)
                reportIndex += 1;
            });
            break;
        case "quitRoom":
            chatBox = clearHistoryMsg(chatBox, chatBoxOuterDiv);
            console.log(data.notification);
            console.log(data.userList);
            let quitRoom = JSON.parse(data.room);
            generatePList(quitRoom);
            $("#joinRoomList").remove();
            chatBoxLi.appendTo(chatBox);
            chatBoxSpan.text(data.notification).appendTo(chatBoxDiv);
            break;
        case "quiter":
            chatBox = clearHistoryMsg(chatBox, chatBoxOuterDiv);
            console.log(data.notification);
            currentRoom.text("");
            hideMsgButton();
            $("#peopleInRoom").remove();
            chatBoxLi.appendTo(chatBox);
            chatBoxSpan.text(data.notification).appendTo(chatBoxDiv);
            break;
        case "failed":
            let notification = data.notification;
            console.log(notification);
            let errorDiv = $("#errorMessageDiv");
            let message = $('<h/>')
                .attr('id', 'errorMessage')
                .text(notification)
                .appendTo(errorDiv);
            $("#errorModal").modal('show');
            break;

        case "sendPublicMsg":
            console.log("sendPublicMsg");
            let curRomPublic = data.currentRoomId;
            let publicCurUser = JSON.parse(data.curUser);
            let publicCurRoom = JSON.parse(data.curRoom);
            let publicMsgList = JSON.parse(data.MsgList);
            let publicLOR = data.LOR;
            let publicCurMsg = JSON.parse(data.curMsg);
            generatePublicMsg(publicCurMsg, publicLOR, publicCurUser);
            break;

        case "sendPrivateMsg":
            console.log("sendPrivateMsg");
            let curRomPrivate = data.currentRoomId;
            let privateCurUser = JSON.parse(data.curUser);
            let privateCurRoom = JSON.parse(data.curRoom);
            let privateMsgList = JSON.parse(data.MsgList);
            let privateReceiver = JSON.parse(data.privateReceiver);
            let privateLOR = data.LOR;
            let privateCurMsg = JSON.parse(data.curMsg);
            generatePrivateMsg(privateCurMsg, privateLOR, privateCurUser);
            break;
        case "sendPrivateMsgList":
            let sendPrivateMsgList = JSON.parse(data.sendPrivateMsgList);
            console.log(sendPrivateMsgList)
            let sendPrivateMsgIndex = 0;
            let sendPrivateMsgDiv = $("#privateUserListDiv");
            let sendPrivateMsgUl = $('<ul/>')
                .attr('id', 'inviteUserList')
                .appendTo(sendPrivateMsgDiv);
            sendPrivateMsgList.forEach(user => {
                let li = $('<li/>').appendTo(sendPrivateMsgUl);
                let inputRadio = $('<input/>')
                    .attr('type', 'radio')
                    .attr('id', 'inviteUser' + sendPrivateMsgIndex)
                    .attr('name', 'inviteUserList')
                    .val(user.userId)
                    .appendTo(li);
                inputRadio.after(' ');
                let label = $('<label/>')
                    .attr('for', 'inviteUser' + sendPrivateMsgIndex)
                    .text("User: " + user.name)
                    .appendTo(li)
                sendPrivateMsgIndex += 1;
            });
            break;
        case "msgDelete":
            let deletedMsgId = JSON.parse(data.deletedMsgId);
            $("#li-"+deletedMsgId).remove();
            chatBoxLi.appendTo(chatBox);
            chatBoxSpan.text(data.notification).appendTo(chatBoxDiv);
            break;
        case "msgRecall":
            let reacallMsgId = JSON.parse(data.recallMsgId);
            $("#li-"+reacallMsgId).remove();
            chatBoxLi.appendTo(chatBox);
            chatBoxSpan.text(data.notification).appendTo(chatBoxDiv);
            break;
        case "msgEditPre":
            let msgEditPreId = JSON.parse(data.msgId);
            let msgEditPreMessage = JSON.parse(data.message);
            $("#msgEditIdSpan").val(msgEditPreId);
            $("#editInput").val(msgEditPreMessage.content);
            break;

        case "msgEdit":
            let msgEditId = JSON.parse(data.editMsgId);
            let msgEditMessage = JSON.parse(data.message);
            let msgContent = data.content;
            let editLOR = data.LOR;

            changEditMsg(msgEditId, msgContent, editLOR);
            chatBoxLi.appendTo(chatBox);
            chatBoxSpan.text(data.notification).appendTo(chatBoxDiv);
            break;
        default:
            break;
    }
}

function generatePList(room) {
    let index = 0;
    let div = $("#plist");
    let dropdownListDiv = $("#userInRoomListDiv");
    $("#peopleInRoom").remove();
    $('#usersInRoom').remove();
    let selectList = $('<select/>')
        .attr('name', "userInRoom")
        .attr('id', "usersInRoom")
        .attr('style', 'margin-left: 0.5em;')
        .appendTo(dropdownListDiv);
    let defaultOption = $('<option/>')
        .attr('value', "public")
        .text("public")
        .appendTo(selectList);
    let ul = $('<ul/>')
        .addClass("list-unstyled chat-list mt-2 mb-0 people-in-room")
        .attr('id', 'peopleInRoom')
        .appendTo(div);
    room.members.forEach(user => {
        let li = $('<li/>')
            .addClass("clearfix")
            .appendTo(ul);
        let img = $('<img/>')
            .attr('src', "https://bootdey.com/img/Content/avatar/avatar7.png")
            .attr('alt', "avatar")
            .appendTo(li);
        let innerDiv = $('<div/>')
            .addClass("about")
            .appendTo(li);
        let nameDiv = $('<div/>')
            .addClass('name')
            .text(user.name)
            .appendTo(innerDiv);

        let option = $('<option/>')
            .attr('value', user.userId)
            .text(user.name)
            .appendTo(selectList);

    })
}

function changEditMsg(msgEditId, msgContent, LOR) {
    let chatBoxDiv = $("#msg-"+msgEditId);
    chatBoxDiv.text(msgContent);

    if (LOR === "RIGHT") {
        let msgRecallDeleteDiv = $('<div/>').appendTo(chatBoxDiv);
        let msgA = $('<a/>').appendTo(msgRecallDeleteDiv);
        let msgI = $('<i/>').addClass('bi bi-arrow-90deg-left')
            .attr('id', "msgRecall-" + msgEditId)
            .click(function () {
                messageRD('msgRecall', msgEditId)
            })
            .appendTo(msgA);

        let msgDA = $('<a/>')
            .attr('data-bs-toggle',"modal")
            .attr('data-bs-target', "#editModal")
            .appendTo(msgRecallDeleteDiv);
        let msgDI = $('<i/>').addClass('bi bi-pencil')
            .attr('id', "msgEdit-" + msgEditId)
            .click(function (){
                messageEditPre('msgEditPre', msgEditId);
            })
            .appendTo(msgDA);
    } else {
        let msgRecallDeleteDiv = $('<div/>').appendTo(chatBoxDiv);
        let msgDI = $('<i/>')
            .addClass('bi bi-trash')
            .attr('id', "msgDelete-" + msgEditId)
            .click(function (){
                messageRD('msgDelete', msgEditId)
            })
            .appendTo(msgRecallDeleteDiv);

    }
}

function generateMsg(msg, LOR, user) {
    let chatBoxOuterDiv = $("#currentRoomChatBox");
    let chatBox = $('<ul/>').appendTo(chatBoxOuterDiv);
    let chatBoxLi = $('<li/>')
        .attr('id', 'li-' + msg.id)
        .addClass('clearfix');
    let chatBoxMsgUser = $('<div/>');
    if (LOR === "RIGHT") {
        chatBoxMsgUser.addClass('message-data float-right')
    } else {
        chatBoxMsgUser.addClass('message-data');
        chatBoxMsgUser.appendTo(chatBoxLi);
        let chatBoxSpan = $('<span/>')
            .addClass("message-data-time")
            .text(user.name)
            .appendTo(chatBoxMsgUser);
    }

    let chatBoxDiv = $('<div/>');
    if (LOR === "RIGHT") {
        chatBoxDiv.addClass('message other-message float-right');
    } else {
        chatBoxDiv.addClass('message my-message');
    }
    chatBoxDiv.text(msg.content)
        .attr('id', "msg-" + msg.id)
        .appendTo(chatBoxLi);

    if (LOR === "RIGHT") {
        let msgRecallDeleteDiv = $('<div/>').appendTo(chatBoxDiv);
        let msgA = $('<a/>').appendTo(msgRecallDeleteDiv);
        let msgI = $('<i/>').addClass('bi bi-arrow-90deg-left')
            .attr('id', "msgRecall-" + msg.id)
            .click(function () {
                messageRD('msgRecall', msg.id)
            })
            .appendTo(msgA);

        let msgDA = $('<a/>')
            .attr('data-bs-toggle',"modal")
            .attr('data-bs-target', "#editModal")
            .appendTo(msgRecallDeleteDiv);
        let msgDI = $('<i/>').addClass('bi bi-pencil')
            .attr('id', "msgEdit-" + msg.id)
            .click(function (){
                messageEditPre('msgEditPre', msg.id);
            })
            .appendTo(msgDA);
    } else {
        let msgRecallDeleteDiv = $('<div/>').appendTo(chatBoxDiv);
        let msgDI = $('<i/>')
            .addClass('bi bi-trash')
            .attr('id', "msgDelete-" + msg.id)
            .click(function (){
                messageRD('msgDelete', msg.id)
            })
            .appendTo(msgRecallDeleteDiv);

    }

    chatBoxLi.appendTo(chatBox);
}

function generatePublicMsg(msg, LOR, user) {
    generateMsg(msg, LOR, user);
}

function generatePrivateMsg(msg, LOR, user) {
    generateMsg(msg, LOR, user);
}

function messageRD(eventName, value) {
    console.log(eventName);
    console.log(eventName);
    console.log(value);
    let msgJson = {};
    let currentRoomId = $("#currentRoom").text().split("ROOM-")[1];

    msgJson = {eventName: eventName,
        currentRoomId: currentRoomId,
        msgId: value+""};

    webSocket.send(JSON.stringify(msgJson));
}

function messageEditPre(eventName, value) {
    let msgJson = {};
    let currentRoomId = $("#currentRoom").text().split("ROOM-")[1];
    msgJson = {eventName: eventName,
        currentRoomId: currentRoomId,
        msgId: value+""};

    webSocket.send(JSON.stringify(msgJson));
}

function messageEditGo() {
    let msgJson = {};
    let currentRoomId = $("#currentRoom").text().split("ROOM-")[1];
    let msgId = $("#msgEditIdSpan").val();
    let input = $("#editInput").val();
    if (input == null || input === "") {
        messageRD("msgDelete", msgId)
    } else {
        msgJson = {eventName: "msgEdit",
            currentRoomId: currentRoomId,
            content: input,
            msgId: msgId+""};
        webSocket.send(JSON.stringify(msgJson));
    }

}

function clearHistoryMsg(chatBoxOld, chatBoxOuterDiv) {
    chatBoxOld.remove();
    let chatBox = $("<ul/>")
        .addClass("m-b-0")
        .attr('id', 'currentRoomChatBoxUl')
        .appendTo(chatBoxOuterDiv);

    return chatBox;
}

function checkAdmin(room, user) {
    if (room == null && user == null) {
        return ;
    }
    console.log("CHECKADMIN")
    console.log(room)
    console.log(user)
    let admin = room.admin;
    let chatRoomBanBtn = $("#chatRoomBanBtn");
    let chatRoomReportBtn = $("#chatRoomReportBtn");
    let chatRoomInviteBtn = $("#chatRoomInviteBtn");
    if (admin.userId === user.userId) {
        console.log("Admin");
        chatRoomBanBtn.show();
        chatRoomReportBtn.hide();
        chatRoomInviteBtn.show();
    } else {
        console.log("Normal");
        chatRoomBanBtn.hide();
        chatRoomReportBtn.show();
        chatRoomInviteBtn.hide();
    }
}

function hideMsgButton() {

    $("#emojiBtn").hide();
    $("#publicMsgSendBtn").hide();
    $("#privateMsgSendBtn").hide();
    $("#inputMsg").hide();
    $("#userInRoomListDiv").hide();
    $("#userInRoom").hide()
}
function showMsgButton() {
    $("#emojiBtn").show();
    $("#publicMsgSendBtn").show();
    $("#privateMsgSendBtn").show();
    $("#inputMsg").show();
    $("#userInRoomListDiv").show();
    $("#userInRoom").show();
}

