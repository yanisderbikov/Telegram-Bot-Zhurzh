package com.zhurzh.nodecheckorderservice.enums;

public enum TextMessage {
    ACTIVATION_BUTTON("Check status", "Проверить статус"),
    CHOOSE_ORDER_START("Choose what order do you want to check",
            "Выбери какой заказ ты хочешь проверить "),
    FAIL_FIND_ORDER("I think you do not have any order now. You can do it",
            "У тебя нет сейчас заказов, но ты можешь его сделать"),
    CHOOSE_ORDER_END("Ok, Let's check", "Отлично, давай проверим твой заказ");

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
