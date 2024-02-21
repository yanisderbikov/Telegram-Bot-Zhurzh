package com.zhurzh.nodecheckorderservice.enums;

public enum TextMessage {
    ACTIVATION_BUTTON("Check status", "Проверить статус"),
    CHOOSE_ORDER_START("Choose the commission you want to check.",
            "Выбери заявку, которую хочешь проверить"),
    FAIL_FIND_ORDER("I think you do not have any commission now, but you can do it",
            "У тебя нет сейчас заказов, но ты можешь его сделать"),
    CHOOSE_ORDER_END("Ok, Let's check", "Отлично, давай проверим твой заказ"),

    DELETE_ORDER_START("Choose the commission that you want to delete",
            "Выбери заказ, который хочешь удалить"),
    DELETE_ORDER_CHECK("Are you sure that you want to continue?",
            "Ты уверен, что хочешь продолжить?"),
    DELETE_ORDER_DELETED("Your commission is deleted", "Твой заказ был удален"),
    DELETE_ORDER_NOT_DELETED("Usshh, I was a little bit terrified",
            "Уфф, я даже немного испугался"),
    BUTTON_BACK("Back", "Вернуться"),
    BUTTON_YES("Yes", "Да"),
    BUTTON_NO("No", "Нет")

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
