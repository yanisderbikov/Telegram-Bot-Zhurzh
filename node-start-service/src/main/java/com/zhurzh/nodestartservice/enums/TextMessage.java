package com.zhurzh.nodestartservice.enums;
public enum TextMessage {
    SWITCH_LAN("Switch the <b>language</b>", "Выбери язык"),
    WELCOME("Hello! My name is Marzipan! I am the most important assistant of the artist Zhurzh, her right paw, and the most beloved gecko in the world! I can tell you something about the author, help calculate the cost of an illustration, or take an order that I will personally deliver to Zhurzh a bit later.",

            "Привет! Меня зовут Марципан! \n" +
                    "Я - самый главный помощник художницы Журж, ее правая лапка и самый любимый геккон на свете! Я могу рассказать тебе что-то об авторе, помочь посчитать стоимость иллюстрации или оставить заявку на заказ, которую я отнесу лично Журжу чуть позже. "),
    RULES("1. Zhurzh may refuse an application without explaining the reasons.\n" +
            "2. Marzipan doesn't want to quarrel, so don't call him small.\n" +
            "3. Smile.",
            "1. Журж может отказаться от заявки без объяснения причин \n" +
                    "2. Марципан не хочет ругаться, поэтому не обзывайте его маленьким \n" +
                    "3. Улыбайтесь "),
    RULES_BUTTON("Rules", "Правила"),
    MAIN_MENU_TEXT("Got it?", "Хорошо?"),
    MAIN_MENU_BUTTON("Got it!", "Да, все понятно");

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

