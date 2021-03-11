package com.thelogicmaster.backend_server_example;

import org.jetbrains.annotations.NotNull;

public class Message {
    public final String user;
    public final String message;

    public Message(String user, String message) {
        this.user = user;
        this.message = message;
    }

    @NotNull
    @Override
    public String toString() {
        return message;
    }
}
