package com.zhurzh.nodefaqservice.enums;

public enum TextMessage {
    ACTIVATION_BUTTON("FAQ", "FAQ"),
    BUTTON_YES("Yes", "Да"),
    BUTTON_NO("No", "Нет"),
    FAQ_START_MESSAGE("List of questions", "Список вопросов"),
    NEXT_BUTTON("Next question", "Следующий вопрос")

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
