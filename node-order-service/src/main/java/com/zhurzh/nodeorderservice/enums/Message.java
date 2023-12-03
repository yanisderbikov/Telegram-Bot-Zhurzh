package com.zhurzh.nodeorderservice.enums;


public enum Message {
    HI("HI", "привет"),
    BYE("Goodbye", "пока"),
    GREETING("Greetings", "приветствия");

    private final String englishMessage;
    private final String russianMessage;

    Message(String englishMessage, String russianMessage) {
        this.englishMessage = englishMessage;
        this.russianMessage = russianMessage;
    }

    public String getMessage(String languageCode) {
        switch (languageCode.toLowerCase()) {
            case "eng":
                return englishMessage;
            case "ru":
                return russianMessage;
            default:
                return "Неизвестный язык";
        }
    }
}
