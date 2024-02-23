package com.zhurzh.nodesearemservice.enums;
public enum TextMessage {
    ACTIVATION_BUTTON("Sea Rem", "Sea Rem"),
    SWITCH_LAN("Switch the <b>language</b>", "Выбери язык"),
    WELCOME("Hello! My name is Marzipan! I am the most important assistant of the artist Zhurzh, her right paw, and the most beloved gecko in the world! I can tell you something about the author, help calculate the cost of an illustration, or take a commission that I will personally deliver to Zhurzh a bit later.",

            "Привет! Меня зовут Марципан! \n" +
                    "Я - самый главный помощник художницы Журж, ее правая лапка и самый любимый геккон на свете! Я могу рассказать тебе что-то об авторе, помочь посчитать стоимость иллюстрации или оставить заявку на заказ, которую я отнесу лично Журжу чуть позже. "),
    RULES("1. Zhurzh may reject the request without explaining the reasons\n" +
            "2. I will not show Zhurzh any files or data until the request is confirmed at the very end\n" +
            "3. All payment-related questions are settled personally with Zhurzh, I'll just give a price idea",

            "1. Журж может отказаться от заявки без объяснения причин \n" +
                    "2. Я не буду показывать Журжу никакие файлы и данные, пока заявка не будет подтверждена в самом конце\n" +
                    "3. Все вопросы по оплате решаются лично с Журжем, а я лишь ориентирую по цене"
    ),
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

