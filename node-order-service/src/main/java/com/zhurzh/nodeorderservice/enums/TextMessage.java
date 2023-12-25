package com.zhurzh.nodeorderservice.enums;


public enum TextMessage {
    ACTIVATION_BUTTON("Calculate order", "Рассчитать стоимость"),
    TO_MAIN_MENU("To main menu", "На главное меню"),
    CORRECT_ORDER("We need to add some correction to your order",
            "Нам нужно внести несколько изменений в твою заявку"),
    CORRECT_ORDER_FORSED("Firstly we need to fill fully our order",
            "Сначала нам нужно завершить заявку, чтобы можно было узначть цену"),
    START("I'm going to ask you a few questions to find out what kind of illustration you want. Let's agree to answer strictly point by point. I ask the question, you give the answer. Don't worry, any item can be adjusted at the end.",
            "Сейчас я задам тебе несколько вопросов, чтобы узнать, какую иллюстрацию ты хочешь. Только давай договоримся отвечать строго по пунктам. Я - вопрос, ты - ответ. Не волнуйся, любой пункт можно будет исправить в конце.  "),
    START_VERY_BEGIN_BUTTON("Start over", "Начать с самого начала"),
    START_HAS_NOT_FINISHED("You have an unfinished application, what would you like to do next?",
            "У тебя осталась незавершенная заявка, что будем делать?"),
    REFERENCE_START("I need to inform Zhurzh who she will be drawing. You can attach links, files, or simply photos of references below.",
            "Мне нужно сообщить Журжу, кого она будет рисовать. Ты можешь прикрепить ссылки, файлы или просто фотографии референсов ниже. "),
    REFERENCE_END("Super! Next up we have:", "Супер! Дальше у нас: "),
    REFERENCE_DONE_MES("If there are no more materials, let me know by pressing the button below.",
            "Если материалов больше нет, сообщи мне, нажав кнопочку ниже"),
    REFERENCE_DONE_BUTTON("Done", "Я все"),
    FORMAT_ILLUSTRATION_START("What illustration format do you prefer?",
            "Какой формат иллюстрации для тебя предпочтительнее? "),
    FORMAT_ILLUSTRATION_END("Let's move on to the next question: ", "Перейдем к следующему вопросу: "),
    DETALIZATION_START("I've brought some examples of Zhurzh's illustrations so it's easier for you to understand what you want from the future work. You can take a closer look and make your choice.",
            "Я принес примеры иллюстраций Журжа, чтобы тебе было проще понять что ты хочешь от будущей работы. Можешь рассмотреть поближе и сделать свой выбор. "),
    DETALIZATION_END("Just a little bit left:", "Осталось немного: "),
    BACKGROUND_START("How detailed should the background be?", "Насколько детальным должен быть фон? "),
    BACKGROUND_END("Thank you! Just a little bit more:", "Спасибо! Еще чуть-чуть:  "),
    COMMENT_TO_ART_START("Write down your thoughts about the art idea. Emotions, shades, and other details you think are important. This way, Zhurzh can immerse herself in the atmosphere of the work and create a really cool art piece! You can also attach links to art samples or references you like here.\n" +
            "\n" +
            "Just so you know, this comment is optional.",
    "Напиши свои мысли, касаемо идеи арта. Эмоции, оттенки, другие детали, которые ты считаешь важными. Так Журж сможет погрузиться в атмосферу работы и сделать действительно классный арт! Сюда же можешь прикрепить ссылки на понравившиеся  арт-примеры или референсы. \n" +
            "\n" +
            "Если что, этот комментарий необязателен. "),
    COMMENT_TO_ART_END("The final touch is needed:", "Требуется последний штрих: "),
    PRICE_SRART_ROUNDPRICE("The approximate cost of the art = ",
            "Примерная стоимость арта = "),
    PRICE_START("\nZhurzh will be very, very happy if you write below the price you're willing to pay for the order. It might greatly motivate her!\n" +
            "\n" +
            "If you want to make any changes to the application, you'll be able to do so later, and I'll gladly prepare a recalculation.",
    "\nЖурж будет очень-очень рада, если ты напишешь ниже ту стоимость заказа, которую ты будешь готов оплатить. Возможно, это сильно замотивирует ее!  \n" +
            "\n" +
            "Если ты хочешь что-то исправить в заявке, ты сможешь сделать это дальше, а я с радостью подготовлю перерасчет. "),

    PRICE_END("Let's check to make sure:", "Давай проверим, чтоб наверняка: "),
    NAME_START("Please write a short title for your application. This will make it easier for both me and Zhurzh to navigate and find it among others in the list.",
            "Пожалуйста, напиши короткое название своей заявки. Так и мне, и Журжу будет легче сориентироваться и найти ее среди других в списке. "),
    NAME_END("Thank you! We're finished, and now I can provide an approximate cost for this interesting work!",
            "Благодарю! Мы закончили и теперь я могу назвать примерную стоимость этой интересной работы! "),
    FINALIZE_START("Here is your order. Looks nice\n", "Вот как выглядет твой заказ"),
    ACCEPT("Alright, are you sure?", "Хорошо, это точно точно-точно?"),
    BUTTON_YES("Yes", "Да"),
    BUTTON_NO("No", "Нет"),
    FINALIZE_AFTER_NO("Ok, let's check again", "Хорошо, давай проверим еще раз"),
    FINALIZE_AGAIN_CHECK("Ok, let's check again, are you sure?", "Отлично, ты уверен на стопудова?"),
    FINALIZE_FINAL_ORDER("Your order is accepted, wait whan zhurzh will connect with you!",
            "Твой заказ принят, ожидай пока журж с тобой свяжется"),
    COUNT_OF_PERSONS_START("How many characters will be in your artwork?",
            "Сколько персонажей будет на твоем арте? "),
    COUNT_OF_PERSONS_END("Great! Moving on to the next point:", "Хорошо! Следующий пункт: "),
    FINALIZE_ADDITIONAL_MESSANGER("\n\nAlso you can add the link to preferable messanger, if you like",
            "\n\nТы можешь дабавить ссылку на месенджер, в котором ты бы мог удобно связываться с Журжом"),
    ADDITIONAL_MESSANGER_ADD("Please, add link to your account where to Zhurzh can easyly connect with you, if you dont't like telegram",
            "Прикрепи ссылку на свой аккаунт, где Журж может с тобой связаться удобным для тебя способом."),
    ADDITIONAL_MESSANGER_IS_CORRECT("Is it correct?", "Все правильно?"),
    ADDITIONAL_MESSANGER_SAVE("Ok, I will say Zhurzh to contact to you via this link",
            "Хорошо, я сообщю Журжу, чтобы она связывалась через этот выбранный метод."),
    DEADLINE_START("Now, please specify the date until which this order is valid. Keep in mind that after the deadline you set, the application will automatically be deleted if Zhurzh doesn't manage to start the work by then.",
            "А теперь, пожалуйста, укажи до какого числа актуален этот заказ. Имей ввиду, что после указанного тобой дедлайна, заявка  автоматически удалится , если Журж так и не успеет приступить к работе. "),
    DEADLINE_END("Fine", "Отлично"),
    PREV_MO("Prev", "Пред"),
    NEXT_MO("Next", "След")
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
