package com.zhurzh.nodestartservice.enums;
public enum TextMessage {
    HELLO("Switch the <b>language</b>", "Выбери язык"),
    WELCOME("Hello, My name is Mrarzypan and...\nBefore we start learn more about rules",
            "Привет я Марципан, тополя ля ля, почитай правила."),
    RULES("This is a rule message",
            "<b>Жирный текст</b>, <i>курсив</i>, <u>подчеркнутый текст</u>, <s>зачеркнутый текст</s>, <code>моноширинный текст</code>, <pre>моноширинный блок</pre>, <a href=\"https://www.furaffinity.net/view/48332248\">ссылка</a>"),
    RULES_BUTTON("Rules", "Правила"),
    MAIN_MENU_TEXT("Got it?", "Понял принял?"),
    MAIN_MENU_BUTTON("Got it!", "Ага");

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

