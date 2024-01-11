package com.zhurzh.nodeorderservice.controller;

/**
 * Тут название кнопок и пути к конкретному состоянию/классу.
 * Каждая имплементация Command имеет свой собственный UserState
 *
 * Порядок это путь по которому идет юзер
 *
 */
public enum UserState {
    ADDITIONAL_MESSANGER("Add", "Добавить", "/add_additional_messanger"),
    CORRECT_ORDER("Correction", "Исправить", "/correct_order_command"),
    START("Start over", "Начать с начала", "/orderservice"),
    COUNT_PERSONS("Count of persons", "Количество персонажей", "/count_persons_command"),
    REFERENCES("Reference", "Референс", "/reference_command"),
    FORMAT_ILLUSTRATION("Format", "Вид иллюстрации", "/format_illustration_command"),
    DETALIZATION("Detalization", "Детализация", "detalization_command"),
    BACKGROUND("Background", "Фон", "/background_command"),
    COMMENT_TO_ART("Comment", "Комментарий", "/comment_to_art_command"),
    NAME("Name", "Имя", "/name_command"),
    DEADLINE("Deadline", "Дедлайн", "/deadline_command"),
    PRICE("Price", "Цена", "/price_command"),
    FINALIZE("Review order", "Проверить заказ", "/review_order_command");

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
