package com.zhurzh.nodestartservice.enums;
public enum TextMessage {
    HELLO("Switch the language", "Выбери язык"),
    WELCOME("Hello, My name is Mrarzypan and...\nBefore we start learn more about rules",
            "Привет я Марципан, тополя ля ля, почитай правила."),
    RULES("This is a rule message", "Тут текст правил"),
    RULES_BUTTON("Rules", "Правила"),
    MAIN_MENU_BUTTON("Got it!", "Все понятно");

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

