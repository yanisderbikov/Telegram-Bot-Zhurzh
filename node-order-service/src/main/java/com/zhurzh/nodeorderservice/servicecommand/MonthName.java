package com.zhurzh.nodeorderservice.servicecommand;

import java.util.Calendar;

public enum MonthName {

    JANUARY("January", "Январь"),
    FEBRUARY("February", "Февраль"),
    MARCH("March", "Март"),
    APRIL("April", "Апрель"),
    MAY("May", "Май"),
    JUNE("June", "Июнь"),
    JULY("July", "Июль"),
    AUGUST("August", "Август"),
    SEPTEMBER("September", "Сентябрь"),
    OCTOBER("October", "Октябрь"),
    NOVEMBER("November", "Ноябрь"),
    DECEMBER("December", "Декабрь"),
            ;

    private final String englishMessage;
    private final String russianMessage;

    MonthName(String englishMessage, String russianMessage) {
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
