package com.zhurzh.nodepricelist.enums;


public enum TextMessage {
    ACTIVATION_BUTTON("Zhurzh's links", "Связаться с Журжем"),

    PRICE_LIST_MESSAGE("I am always happy to help! But if necessary, you can directly contact Zhurzh in a way that's convenient for you",

            "Я всегда буду рад помочь! Но если это нужно, ты можешь напрямую связаться с Журжем удобным для тебя способом"),

    TO_MAIN_MENU("To main menu", "На главное меню"),

    ;

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
