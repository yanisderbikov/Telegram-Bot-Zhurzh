package com.zhurzh.app.commonjpa.enums;

public enum AdditionsOptionsEnum implements Language {

    DETAIL("Detail", "Детальная"),
    NSFW("NSFW", "NSFW"),
    BRIGHT("Bright colors", "Яркие цвета");

    private final String englishMessage;
    private final String russianMessage;

    AdditionsOptionsEnum(String englishMessage, String russianMessage) {
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
