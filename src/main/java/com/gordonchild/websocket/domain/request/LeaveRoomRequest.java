package com.gordonchild.websocket.domain.request;

import com.gordonchild.websocket.domain.session.ChatSession;

public class LeaveRoomRequest extends RoomRequest {

    public LeaveRoomRequest(ChatSession chatSession) {
        super(chatSession);
    }

}
