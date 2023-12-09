package com.zhurzh.commonjpa.enums;

public enum CountOfPersons implements Language{
    ONE("One", "Один"),
    TWO("Two", "Два"),
    MORE_THAN_TWO("More than two", "Больше двух");

    private final String englishMessage;
    private final String russianMessage;

    CountOfPersons(String englishMessage, String russianMessage) {
        this.englishMessage = englishMessage;
        this.russianMessage = russianMessage;
    }

    @Override
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
