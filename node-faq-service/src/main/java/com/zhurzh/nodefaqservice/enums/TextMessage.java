package com.zhurzh.nodefaqservice.enums;

public enum TextMessage {
    ACTIVATION_BUTTON("FAQ", "FAQ"),
    QUESTION_TITLE("<b>Question:</b> ", "<b>Вопрос: </b>"),
    ANSWER_TITLE("<b>Answer:</b> ", "<b>Ответ: </b>"),

    BUTTON_YES("Yes", "Да"),
    BUTTON_NO("No", "Нет"),
    BUTTON_CANCEL("Cancel","Отмена"),
    BUTTON_BACK_TO_LIST_QUESTIONS("Back to list","Обратно к списку"),

    FAQ_START_MESSAGE("List of questions", "Список вопросов"),
    NEXT_BUTTON("Next question", "Следующий вопрос"),
    ADD_NEW_QUESTION_START("Write down your question", "Напиши свой вопрос"),
    ADD_NEW_QUESTION_CONFIRM("Correct question?" , "Ты хочешь добавить этот вопрос?"),
    ADD_NEW_QUESTION_END("Thank you for your interest. You will get an notification when you will get an answer on your question.\nThank you again for your question",
            "Спасибо за оставленный вопрос. Когда Журж ответит на него, ты получишь уведомление")
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
