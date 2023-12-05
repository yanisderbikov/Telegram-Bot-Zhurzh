package com.zhurzh.node.bot.branches.mainmenu;

enum TextMessage {
    MENU("menu", "меню"),
    NO_SERVICE_AVAILABLE("No one service is runs, sorry. Try later", "Не один из сервисов не запущен. Попробуйте позже"),
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
