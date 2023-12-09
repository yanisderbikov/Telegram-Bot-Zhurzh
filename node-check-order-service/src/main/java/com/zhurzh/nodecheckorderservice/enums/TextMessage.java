package com.zhurzh.nodecheckorderservice.enums;

public enum TextMessage {
    ACTIVATION_BUTTON("Check my orders", "Узнасть статус заказов"),
    HI("HI", "привет"),
    BYE("Goodbye", "пока"),
    GREETING("Greetings", "приветствия");

    private final String englishMessage;
    private final String russianMessage;

    TextMessage(String englishMessage, String russianMessage) {
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
