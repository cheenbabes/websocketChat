package com.gordonchild.websocket.domain;

import com.gordonchild.websocket.domain.event.UserEvent;

public class ChatMessage extends UserEvent {

    private String message;

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
