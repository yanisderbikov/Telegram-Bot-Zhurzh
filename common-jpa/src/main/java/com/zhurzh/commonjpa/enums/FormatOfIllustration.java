package com.zhurzh.commonjpa.enums;

public enum FormatOfIllustration implements Language{
    PORTRAIT("Portrait", "Портрет"),
    HALF_BODY("Half Body", "По пояс"),
    FULL_BODY("Full Body", "Все тело");
//    DOESNT_MATTER("Doesn't matter", "Не важно");

    private final String englishMessage;
    private final String russianMessage;

    FormatOfIllustration(String englishMessage, String russianMessage) {
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
