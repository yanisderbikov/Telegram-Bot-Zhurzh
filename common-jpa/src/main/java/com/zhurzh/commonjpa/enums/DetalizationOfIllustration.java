package com.zhurzh.commonjpa.enums;

public enum DetalizationOfIllustration implements Language{
    DETAILED("Detailed", "Детальная"),
    CLASSICAL("Classical", "Классическая"),
    BLACK_AND_WHITE_SKETCH("B&W sketch", "Чернобелый эскиз"),
    LINE_ART("Line art", "Лайнарт"),
    LINE_ART_SHADING("LA + shading", "Лайн + Щейд");

    private final String englishMessage;
    private final String russianMessage;

    DetalizationOfIllustration(String englishMessage, String russianMessage) {
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