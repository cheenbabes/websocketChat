package com.gordonchild.websocket.chat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.gordonchild.websocket.domain.ChatMessage;
import com.gordonchild.websocket.domain.ChatSession;
import com.gordonchild.websocket.domain.StartChatRequest;
import com.gordonchild.websocket.domain.event.JoinEvent;
import com.gordonchild.websocket.domain.event.LeaveEvent;
import com.gordonchild.websocket.domain.event.UserData;
import com.gordonchild.websocket.domain.request.JoinRoomRequest;
import com.gordonchild.websocket.domain.request.LeaveRoomRequest;
import com.gordonchild.websocket.domain.request.RoomRequest;
import com.gordonchild.websocket.domain.request.SendMessageRequest;
import com.gordonchild.websocket.domain.server.RoomInfo;

@Service("chatRoomService")
public class ChatRoomServiceImpl extends AbstractSessionService<StartChatRequest,ChatSession> implements ChatRoomService {

    private static final Logger LOG = LoggerFactory.getLogger(ChatRoomService.class);

    private static final String CHAT_TOPIC = "/topic/chats-";

    private Map<String,RoomInfo> rooms = new ConcurrentHashMap<>();

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public ChatRoomServiceImpl() {
        super(ChatSession.class);
    }

    @Override
    public void sendMessage(SendMessageRequest request) {
        ChatSession session = this.getSession(request.getSessionId());
        ChatMessage chatMessage = this.createChatEvent(session, ChatMessage.class);
        chatMessage.setMessage(request.getMessage());
        this.simpMessagingTemplate.convertAndSend(CHAT_TOPIC + request.getRoomName(), chatMessage);
    }

    @Override
    public void userJoin(JoinRoomRequest request) {
        ChatSession session = this.getSession(request.getSessionId());
        RoomInfo roomInfo = this.getRoom(request);
        roomInfo.addUser(session);
        session.setRoomName(request.getRoomName());
        JoinEvent joinEvent = this.createChatEvent(session, JoinEvent.class);
        List<UserData> users = new ArrayList<>();
        roomInfo.getUsers().forEach(user->{
            UserData userData = new UserData();
            userData.setUsername(user.getUsername());
            userData.setPublicId(user.getPublicId());
            userData.setTime(null);
            users.add(userData);
        });
        joinEvent.setAllUsers(users);
        this.simpMessagingTemplate.convertAndSend(CHAT_TOPIC + request.getRoomName(), joinEvent);
    }

    @Override
    public void userLeave(LeaveRoomRequest request) {
        ChatSession session = this.getSession(request.getSessionId());
        RoomInfo roomInfo = this.getRoom(request);
        roomInfo.removeUser(session);
        this.removeSession(session.getSessionId());
        LeaveEvent leaveEvent = this.createChatEvent(session, LeaveEvent.class);
        this.simpMessagingTemplate.convertAndSend(CHAT_TOPIC + request.getRoomName(), leaveEvent);
    }

    private RoomInfo getRoom(RoomRequest room) {
        return this.getRoom(room.getRoomName());
    }
    private RoomInfo getRoom(String roomName) {
        RoomInfo roomInfo = this.rooms.get(roomName);

        if(roomInfo == null) {
            roomInfo = new RoomInfo(roomName);
            this.rooms.put(roomName, roomInfo);
        }
        return roomInfo;
    }

    private <T extends UserData> T createChatEvent(ChatSession session, Class<T> clazz) {
        T obj = null;
        try {
            obj = clazz.newInstance();
            obj.setUsername(session.getUsername());
            obj.setPublicId(session.getPublicId());
        } catch(ReflectiveOperationException ex) {
            LOG.error("error instantiating chat event", ex);
        }
        return obj;
    }

    @Override
    protected void populateSession(ChatSession session, StartChatRequest sessionRequest) {
        session.setUsername(sessionRequest.getUsername());
    }
}
