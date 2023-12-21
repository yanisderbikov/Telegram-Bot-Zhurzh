package com.zhurzh.nodefaqservice.controller;

/**
 * Тут название кнопок и пути к конкретному состоянию/классу.
 * Каждая имплементация Command имеет свой собственный UserState
 *
 * Порядок это путь по которому идет юзер
 *
 */
public enum UserState {
    FAQ("FAQ", "FAQ", "/faqservice"),
    ADD_NEW_QUESTION("Add question", "Добавить вопрос", "/add_own_question_command"),

    MAIN_MENU("Menu", "Меню", "/menu");

    private final String englishMessage;
    private final String russianMessage;
    private final String uniqPath;

    UserState(String englishMessage, String russianMessage, String uniqPath) {
        this.englishMessage = englishMessage;
        this.russianMessage = russianMessage;
        this.uniqPath = uniqPath;
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
    public String getPath(){
        return uniqPath;
    }
}
