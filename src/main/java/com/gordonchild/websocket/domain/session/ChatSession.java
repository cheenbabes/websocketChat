package com.gordonchild.websocket.domain.session;

import com.gordonchild.websocket.domain.request.UserVoteRequest;

public abstract class ChatSession implements Session {

    private String username;
    private String roomName;
    private UserVoteRequest.VoteType currentVote = UserVoteRequest.VoteType.UNVOTE;
    private boolean voteHidden = true;

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRoomName() {
        return this.roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public UserVoteRequest.VoteType getCurrentVote() {
        return this.currentVote;
    }

    public void setCurrentVote(UserVoteRequest.VoteType currentVote) {
        this.currentVote = currentVote;
    }

    public boolean isVoteHidden() {
        return this.voteHidden;
    }

    public void setVoteHidden(boolean voteHidden) {
        this.voteHidden = voteHidden;
    }
}
