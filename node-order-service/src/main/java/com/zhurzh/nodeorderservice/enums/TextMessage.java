package com.zhurzh.nodeorderservice.enums;


public enum TextMessage {
    TO_MAIN_MENU("To main menu", "На главное меню"),
    CORRECT_ORDER("What do you want to change on your order", "Что ты хочешь изменить в своей заявке?"),
    START("Welcome message", "Сейчас я задам тебе несколько вопросов, чтобы узнать, какую картину от Журжа ты хочешь. Только давай договоримся - строго по пунктам!\n" +
                  "Я - вопрос, а ты - ответ.\n" +
                  "Итак, начнем с количества персонажей.\n" +
                  "Сколько их будет на арте?"),
    START_HAS_NOT_FINISHED("You have not finished order.", "У тебя есть незавершенная заяка"),
    ACTIVATION_BUTTON("Make an order", "Оформить заявку на заказ"),
    REFERENCE_START("Great, attach one ore more links bellow to your reference",
            "Отлично, прикрепи ссылки на референсы"),
    REFERENCE_END("Okey", "Отлично"),
    FORMAT_ILLUSTRATION_START("What about style of illustration? I'll show you a picture for more understands",
            "А что на счет стиля и детализации? Я покажу тебе картинку"),
    FORMAT_ILLUSTRATION_END("Cool, you chosed ", "Отлично ты выбрал"),
    DETALIZATION_START("What about detailization and style of the art?",
            "Что на счет детализации и стиля арта?"),
    DETALIZATION_END("Great, chosen. Next step", "Отлично, следующий шаг"),
    BACKGROUND_START("What about backfround. Is it hard?", "На сколько сложный фон ты хочешь?"),
    BACKGROUND_END("Ok, Done", "Окей"),
    COMMENT_TO_ART_START("Leave a your personal comment to art. I will be really glad to hear from as much as possible",
    "Напиши ниже свой комментарий о своей работе, чтобы Журж могла лучше погрузиться в атмосферу твоего арта"),
    PRICE_START("So ok. Fine. I calculated your order. But I'd be so glad if you will write the price that you are redy to pay. Maybe Zhurzh will be more motivated if you pay more. But i'm not sure :)",
    "Отлично, я подсчитал стоимость твоего заказа. Попрошу тебя написать сколько ты готов заплатить за заказ, может быть журж будет более мотивирована выполнить твой заказ, но это не точно)"),
    PRICE_PAYMENTS("\n\nAcceptable paymant:\nPayPal : yanisderbikov.bali@gmail.com\nPatreon : patreon.com",
            "\n\nТы можешь оплатить заказ удобным тебе способом\nкарта сбера : xxxx xxxx xxxx xxxx\nСБП на сбер по номеру +7999 ---- --- ---"),
    PRICE_END("Oh yeah, good price", "Супер, хорошая цена"),
    COMMENT_TO_ART_END("I wrote it down", "записал"),
    NAME_START("Write down a shirt name of your order", "Напиши короткое название для своей заявки"),
    NAME_END("Nice name", "Классно внатуре"),
    FINALIZE_START("Here is your order. Looks nice\n", "Вот как выглядет твой заказ"),
    ACCEPT("Accept", "Подтвердить"),
    FINALIZE_FINAL_ORDER("Your order is accepted, wait whan zhurzh will connect with you!",
            "Твой заказ принят, ожидай пока журж с тобой свяжется"),
    COUNT_OF_PERSONS_START("Could you say how many persons will be in you art?",
            "Подскажи, сколько персонажей будет на твоем арте?"),
    COUNT_OF_PERSONS_END("Fine", "Отлично")
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
