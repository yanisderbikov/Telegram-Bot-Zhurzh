package com.zhurzh.nodepricelist.enums;


public enum TextMessage {
    ACTIVATION_BUTTON("Price list", "Прайс лист"),

    PRICE_LIST_MESSAGE("Here are all the basic prices for Jurge's illustrations. Since she is a great artist, she draws a lot of things! " +
            "Art, stickers, banners. Even dakimakura! Therefore, if you want to get acquainted in more detail, then I leave\n" +
            "couple of links:",

            "Вот все основные расценки на иллюстрации Журжа. Так как она классная" +
            "художница, она рисует много всякого! Арты, стикеры, баннеры. Даже" +
            "дакимакуры! Поэтому, если хочешь ознакомиться подробнее, то я оставляю" +
            "пару ссылок:"),

    TO_MAIN_MENU("To main menu", "На главное меню"),

    ;

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
