package com.example.task8_1c;

public class MessageRequest {
    private String userMessage;
    private String[] chatHistory;

    public MessageRequest(String userMessage) {
        this.userMessage = userMessage;
        this.chatHistory = new String[0];
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

    public String[] getChatHistory() {
        return chatHistory;
    }

    public void setChatHistory(String[] chatHistory) {
        this.chatHistory = chatHistory;
    }
}
