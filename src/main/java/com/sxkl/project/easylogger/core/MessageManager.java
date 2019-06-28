package com.sxkl.project.easylogger.core;

public class MessageManager {


    private MessageManager() {}

    private static class Singleton {
        private static final MessageManager MANAGER = new MessageManager();
    }

    public static MessageManager getInstance() {
        return Singleton.MANAGER;
    }
}
