package com.example.revent.models;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesList {

    private String name,surname, user_id, lastMessage, chatKey;


    private int unseenMessages;

    public MessagesList(String name, String surname, String user_id, String lastMessage, int unseenMessages, String chatKey) {
        this.name = name;
        this.surname = surname;
        this.user_id = user_id;
        this.lastMessage = lastMessage;
        this.unseenMessages = unseenMessages;
        this.chatKey = chatKey;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getLastMessage() {
        return lastMessage;
    }



    public int getUnseenMessages() {
        return unseenMessages;
    }

    public String getChatKey() {
        return chatKey;
    }
}
