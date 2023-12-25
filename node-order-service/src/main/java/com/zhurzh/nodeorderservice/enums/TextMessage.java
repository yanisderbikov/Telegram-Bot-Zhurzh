package com.zhurzh.nodeorderservice.enums;


public enum TextMessage {
    ACTIVATION_BUTTON("Calculate order", "Рассчитать стоимость"),
    TO_MAIN_MENU("To main menu", "На главное меню"),
    CORRECT_ORDER("We need to add some correction to your order", "Нам нужно внести несколько изменений в твою заявку"),
    CORRECT_ORDER_FORSED("Firstly we need to fill fully our order", "Сначала нам нужно завершить заявку, чтобы можно было узначть цену"),
    START("Welcome message", "Сейчас я задам тебе несколько вопросов, чтобы узнать, какую картину от Журжа ты хочешь. Только давай договоримся - строго по пунктам!\n" +
                  "Я - вопрос, а ты - ответ.\n"),
    START_VERY_BEGIN_BUTTON("Start over", "Начать с самого начала"),
    START_HAS_NOT_FINISHED("You have not finished order.", "У тебя есть незавершенная заяка"),
    REFERENCE_START("Great, attach one ore more links bellow to your reference. You can send photos, files, or links.",
            "Отлично, прикрепи ссылки на референсы. Ты можешь прикреплять файлы, сслыки, просто фотографии"),
    REFERENCE_END("Okey", "Отлично"),
    REFERENCE_DONE_MES("Press button bellow if you are done, or you can send more files",
            "Нажми кнопку ниже если твои референсы закончились, либо продолжай отправлять"),
    REFERENCE_DONE_BUTTON("Done", "Все"),
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
    PRICE_START("So ok. Fine. I calculated your order. But I'd be so glad if you will write the price that you are redy to pay. Maybe Zhurzh will be more motivated if you pay more. But i'm not sure :)" +
            "\n\nWrite down how much you could pay USD \nP.S. write down just a number",
    "Отлично, я подсчитал стоимость твоего заказа. \nПопрошу тебя написать сколько ты готов заплатить за заказ, может быть Журж будет более мотивирована выполнить твой заказ, но это не точно)\n" +
            "\nНапиши пожалуйста сколько бы ты был бы готов заплатить?"),
    PRICE_PAYMENTS("\n\nAcceptable paymant:\nPayPal : yanisderbikov.bali@gmail.com\nPatreon : patreon.com",
            "\n\nТы можешь оплатить заказ удобным тебе способом\nкарта сбера : xxxx xxxx xxxx xxxx\nСБП на сбер по номеру +7999 ---- --- ---" +
                    "\n\nНапиши пожалуйста цену, которую ты готов заплатить"),
    PRICE_END("Oh yeah, good price", "Супер, хорошая цена"),
    COMMENT_TO_ART_END("I wrote it down", "записал"),
    NAME_START("Write down a shirt name of your order", "Напиши короткое название для своей заявки"),
    NAME_END("Nice name", "Классно внатуре"),
    FINALIZE_START("Here is your order. Looks nice\n", "Вот как выглядет твой заказ"),
    ACCEPT("Accept", "Подтвердить"),
    BUTTON_YES("Yes", "Да"),
    BUTTON_NO("No", "Нет"),
    FINALIZE_AFTER_NO("Ok, let's check again", "Хорошо, давай проверим еще раз"),
    FINALIZE_AGAIN_CHECK("Ok, let's check again, are you sure?", "Отлично, ты уверен на стопудова?"),
    FINALIZE_FINAL_ORDER("Your order is accepted, wait whan zhurzh will connect with you!",
            "Твой заказ принят, ожидай пока журж с тобой свяжется"),
    COUNT_OF_PERSONS_START("Could you say how many persons will be in you art?",
            "Подскажи, сколько персонажей будет на твоем арте?"),
    COUNT_OF_PERSONS_END("Fine", "Отлично"),
    FINALIZE_ADDITIONAL_MESSANGER("\n\nAlso you can add the link to preferable messanger, if you like",
            "\n\nТы можешь дабавить ссылку на месенджер, в котором ты бы мог удобно связываться с Журжом"),
    ADDITIONAL_MESSANGER_ADD("Please, add link to your account where to Zhurzh can easyly connect with you, if you dont't like telegram",
            "Прикрепи ссылку на свой аккаунт, где Журж может с тобой связаться удобным для тебя способом."),
    ADDITIONAL_MESSANGER_IS_CORRECT("Is it correct?", "Все правильно?"),
    ADDITIONAL_MESSANGER_SAVE("Ok, I will say Zhurzh to contact to you via this link",
            "Хорошо, я сообщю Журжу, чтобы она связывалась через этот выбранный метод.")
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
