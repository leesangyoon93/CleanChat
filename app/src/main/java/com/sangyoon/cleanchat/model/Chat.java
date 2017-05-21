package com.sangyoon.cleanchat.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by sangyoon on 2017. 5. 17..
 */

// Data Transfer Object ( Value Object )
class Chat {
    final String message;
    final String timestamp;
    final String imageURL;

    public Chat() {
        this("", "", "");
    }

    public Chat(String message, String timestamp) {
        this(message, timestamp, "");
    }

    public Chat(String message, String timestamp, String imageURL) {
        this.message = message;
        this.timestamp = timestamp;
        this.imageURL = imageURL;
    }

    public static Chat newChat(String message) {
        return new Chat(message, timestamp());
    }

    public static Chat newChatWithImage(String message, String imageURL) {
        return new Chat(message, timestamp(), imageURL);
    }

    private static String timestamp() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("a h:mm", Locale.KOREA);
        return dateFormat.format(date);
    }

}