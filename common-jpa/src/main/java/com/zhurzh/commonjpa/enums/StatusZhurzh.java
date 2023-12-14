package com.zhurzh.commonjpa.enums;

public enum StatusZhurzh implements Language{

    UNSEEN("Unseen", "Не просмотрен"),
    SEEN("Seen", "Просмотрен"),
    ACCEPTED("Accepted", "Принят"),
    REJECTED("Rejected", "Отменен")

    ;

    private final String englishMessage;
    private final String russianMessage;

    StatusZhurzh(String englishMessage, String russianMessage) {
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
