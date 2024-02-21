package com.zhurzh.nodeorderservice.enums;


public enum TextMessage {
    ACTIVATION_BUTTON("Calculate commission", "Рассчитать стоимость"),
    TO_MAIN_MENU("To main menu", "На главное меню"),
    CORRECT_ORDER("We need to add some correction to your commission",
            "Нам нужно внести несколько изменений в твою заявку"),
    CORRECT_ORDER_FORSED("Firstly we need to fill fully our commission to know the price",
            "Сначала нам нужно завершить заявку, чтобы можно было узнать цену"),
    START("I'm going to ask you a few questions to find out what kind of illustration you want. Let's agree to answer strictly point by point. I ask the question, you give the answer. Don't worry, any item can be adjusted at the end.",
            "Сейчас я задам тебе несколько вопросов, чтобы узнать, какую иллюстрацию ты хочешь. Только давай договоримся отвечать строго по пунктам. Я - вопрос, ты - ответ. Не волнуйся, любой пункт можно будет изменить в конце. " +
                    "\nНе бойся общаться со мной, я не отправлю заявку Журжу, <b>пока ты не подтвердишь ее</b> ;)"),
    START_VERY_BEGIN_BUTTON("Start over", "Начать с самого начала"),
    START_HAS_NOT_FINISHED("You have an unfinished request, what would you like to do next?",
            "У тебя осталась незавершенная заявка, что будем делать?"),
    REFERENCE_START("I need to inform Zhurzh what a character she will be drawing. You can attach <b>links</b>, <b>files</b>, or simply <b>photos</b> of references below." +
            "\nRemind that you can change it in the end",
            "Мне нужно сообщить Журжу, кого она будет рисовать. Ты можешь прикрепить <b>ссылки</b>, <b>файлы</b> или просто <b>фотографии</b> референсов ниже." +
                    "\nНапоминаю, что ты сможешь изменить файлы в конце"),
    REFERENCE_END("Super! Next up we have:", "Супер! Дальше у нас: "),
    REFERENCE_DONE_MES("If there are no more files, let me know by pressing the button below.",
            "Если материалов больше нет, сообщи мне, нажав кнопочку ниже"),
    REFERENCE_DONE_BUTTON("Done", "Я все"),
    FORMAT_ILLUSTRATION_START("What illustration format do you prefer?",
            "Какой формат иллюстрации для тебя предпочтительнее? "),
    FORMAT_ILLUSTRATION_END("Let's move on to the next question: ", "Перейдем к следующему вопросу: "),
    DETALIZATION_START("I've brought some examples of Zhurzh's illustrations so it's easier for you to understand what you want from the future artwork. You can take a closer look and make your choice.",
            "Я принес примеры иллюстраций Журжа, чтобы тебе было проще понять что ты хочешь от будущей работы. Можешь рассмотреть поближе и сделать свой выбор"),
    DETALIZATION_END("Just a little bit left:", "Осталось немного: "),
    BACKGROUND_START("How detailed should the background be?", "Насколько детальным должен быть фон? "),
    BACKGROUND_END("Thank you! Just a little bit more:", "Спасибо! Еще чуть-чуть:  "),
    COMMENT_TO_ART_START("Write down your thoughts about the art idea. Emotions, shades, and other details you think are important. This way, Zhurzh can immerse herself in the atmosphere of the work and create a really cool art piece! You can also attach links to art samples or references you like here.\n" +
            "\n" +
            "Just so you know, this comment is optional.",
    "Напиши свои мысли, касаемо идеи арта. Эмоции, оттенки, другие детали, которые ты считаешь важными. Так Журж сможет погрузиться в атмосферу работы и сделать действительно классный арт! Сюда же можешь прикрепить ссылки на понравившиеся арт-примеры или референсы. \n" +
            "\n" +
            "Если что, этот комментарий необязателен"),
    COMMENT_TO_ART_ERROR("Write in text, please.\n" +
            "If you want to send photos, attach them to the <b>references</b> item. You can do this after completing the form.",
            "Напиши текстом, пожалуйста.\nЕсли хочешь отправить фотографии, прикрепи их к пункту <b>референсы</b>. Если что ты сможешь это сделать по завершению заявки"),
    COMMENT_TO_ART_END("Ok next one", "Хорошо, далее"),
    PRICE_START("\nZhurzh will be very, very happy if you write below the price you're willing to pay for the commission.\n" +
            "\n" +
            "If you want to make any changes to the request, you'll be able to do it later, and I'll gladly prepare a recalculation.",
    "\nЖурж будет очень-очень рада, если ты напишешь ниже ту стоимость заказа, которую ты будешь готов оплатить.\n" +
            "\n" +
            "Если ты захочешь что-то исправить в заявке, ты сможешь сделать это дальше, а я с радостью подготовлю перерасчет"),
    PRICE_INVALID_NUM("I can't understand. Write down just a simple number. It should be more than zero",
            "Я не очень понял. Напиши пожалуйста просто цифру больше нуля"),
    PRICE_END("Let's check to make sure:", "Давай проверим, чтоб наверняка: "),
    NAME_START("Please write a short title for your request. This will make it easier for both me and Zhurzh to navigate and find it among others in the list",
            "Пожалуйста, напиши короткое название своей заявки. Так и мне, и Журжу будет легче сориентироваться и найти ее среди других в списке"),
    NAME_END("Fine",
            "Хорошо"),
    FINALIZE_START("Here is your commission. Looks nice", "Вот как выглядет твой заказ"),
    ACCEPT("Alright", "Хорошо"),
    BUTTON_YES("Yes", "Да"),
    BUTTON_NO("No", "Нет"),
    FINALIZE_AFTER_NO("Ok, let's check again", "Хорошо, давай проверим еще раз"),
    FINALIZE_AGAIN_CHECK("Ok, let's check again, are you sure?", "Отлично, ты уверен на сто процентов?"),
    FINALIZE_FINAL_ORDER("Thank you so much!\n" +
            "Your commission is accepted, wait when Zhurzh will contact with you!",
            "Спасибо!\n" +
                    "Твой заказ принят, ожидай пока журж с тобой свяжется"),
    COUNT_OF_PERSONS_START("How many characters will be in your artwork?",
            "Сколько персонажей будет на твоем арте? "),
    COUNT_OF_PERSONS_END("Great! Moving on to the next point:", "Хорошо! Следующий пункт: "),
    FINALIZE_ADDITIONAL_MESSANGER("\n\nAlso you can add the link to preferable messenger, if Telegram is not preferable",
            "\n\nТы можешь добавить ссылку на месенджер, в котором тебе будет удобно общаться с Журжем"),
    ADDITIONAL_MESSANGER_ADD("Please, add link to your account where to Zhurzh can contact with you",
            "Прикрепи ссылку на свой аккаунт, где Журж сможет с тобой связаться"),
    ADDITIONAL_MESSANGER_IS_CORRECT("Сorrect?", "Все правильно?"),
    ADDITIONAL_MESSANGER_SAVE("Ok, I will say Zhurzh",
            "Хорошо, я сообщу Журжу"),
    DEADLINE_START("Now we need to set a deadline for which your commission will be relevant. A week before the deadline, I will write to you is it relevant for you or not.",
            "Сейчас нам нужно установить дедлайн, до которого твоя завка будет актуальна. За неделю до указанного дедлайна я тебе напишу, чтобы узнать все еще она актуальна или нет. \nИмей ввиду, что после указанного тобой дедлайна, заявка  автоматически удалится, если Журж так и не приступит к работе"),
    DEADLINE_END("Thank you! We're finished, and now I can provide an approximate cost for this artwork!",
            "Благодарю! Мы закончили и теперь я могу назвать ориентировочную стоимость этой работы! "),
    PREV_MO("Prev", "Пред"),
    NEXT_MO("Next", "След"),
    DOESNT_MATTER("Doesn't matter", "Не важно"),
    DEADLINE_CONFIRM_1("You've chosen ", "Ты выбрал "),
    DEADLINE_CONFIRM_2("\nCorrect?", "\nВсе правильно?"),
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
