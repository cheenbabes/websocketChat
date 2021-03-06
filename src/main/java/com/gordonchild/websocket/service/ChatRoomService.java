package com.gordonchild.websocket.service;

import com.gordonchild.websocket.domain.request.ClearVotingRequest;
import com.gordonchild.websocket.domain.request.JoinRoomRequest;
import com.gordonchild.websocket.domain.request.LeaveRoomRequest;
import com.gordonchild.websocket.domain.request.RevealVoteRequest;
import com.gordonchild.websocket.domain.request.SendMessageRequest;
import com.gordonchild.websocket.domain.request.UserAwayRequest;
import com.gordonchild.websocket.domain.request.UserTypingRequest;
import com.gordonchild.websocket.domain.request.UserVoteRequest;
import com.gordonchild.websocket.domain.session.ChatSession;

public interface ChatRoomService {

    void sendMessage(SendMessageRequest request);
    void userJoin(JoinRoomRequest userId);
    void userLeave(LeaveRoomRequest user);
    void userAway(UserAwayRequest userAwayRequest);
    void userTyping(UserTypingRequest user);
    void userVote(UserVoteRequest request);
    void revealVotes(RevealVoteRequest request);
    void clearVoting(ClearVotingRequest request);

    ChatSession getSocketSession(String sessionId);
}
