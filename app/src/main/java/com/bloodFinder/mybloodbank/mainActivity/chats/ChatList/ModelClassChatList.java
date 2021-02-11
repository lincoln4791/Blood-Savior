package com.bloodFinder.mybloodbank.mainActivity.chats.ChatList;

public class ModelClassChatList {
    private String userName;
    private String userID;
    private String photoName;
    private String unreadMessageCount;
    private String lastMessage;
    private String lastMessageTime;

    public ModelClassChatList(String userName, String userID, String photoName, String unreadMessageCount, String lastMessage, String lastMessageTime) {
        this.userName = userName;
        this.userID = userID;
        this.photoName = photoName;
        this.unreadMessageCount = unreadMessageCount;
        this.lastMessage = lastMessage;
        this.lastMessageTime = lastMessageTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    public String getUnreadMessageCount() {
        return unreadMessageCount;
    }

    public void setUnreadMessageCount(String unreadMessageCount) {
        this.unreadMessageCount = unreadMessageCount;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(String lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }
}
