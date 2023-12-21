package com.zhurzh.commonjpa.enums;

public enum BackgroundOfIllustration implements Language{
    BLURRED("Blurred", "Размытый"),// бесплатныц
    SIMPLE_WITH_ELEMENTS_OF_BLUR("Simple", "Простой"), // +10 от стоимости
    DETAILED("Detailed", "Детализированный"); //

    private final String englishMessage;
    private final String russianMessage;

    BackgroundOfIllustration(String englishMessage, String russianMessage) {
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
