package Siryakov.com;


import Siryakov.com.Config.BotConfig;
import lombok.AllArgsConstructor;


import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
@Component
@AllArgsConstructor
public class NNGUbot   extends TelegramLongPollingBot {
    private final BotConfig botConfig;

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    private static final String START = "/start";
    private static final String DAY = "/day";
    private static final String DAY2 = "/day2";
    private static final String DAY7 = "/day7";
    private static final String HELP = "/help";



    public void onUpdateReceived(Update update) {
        if(!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }
        var message = update.getMessage().getText();//сохраняем сообщение пользователя в переменную
        var chatId = update.getMessage().getChatId(); // идентификатор чата, что бы отправить ответ именно тому пользователю
        //проверка, какую команду прислал пользователь
        switch (message){ //switch сравнивает значения
            case START -> {
                String userName = update.getMessage().getChat().getUserName();
                startCommand(chatId,userName);
            }
            case DAY -> {
                ScheduleParser scheduleParser = new ScheduleParser();

                // Получаем JSON-данные для сегодняшней даты
                String jsonData = scheduleParser.getScheduleForToday();
                if (jsonData != null) {
                    // Парсим JSON и получаем список ScheduleItem
                    List<ScheduleItem> scheduleItems = scheduleParser.parseScheduleData(jsonData);

                    // Выводим данные на экран
                    for (ScheduleItem item : scheduleItems) {
                        sendMessage(chatId,item.toString());
                    }
                } else {
                    sendMessage(chatId,"Не удалось получить JSON-данные.");
                }
            }

            case HELP -> helpCommand(chatId);
            default -> unknownCommand(chatId);
        }
    }

    private void startCommand(Long chatId, String userName){
        var text = """ 
        Добро пожаловать бот, %s!
        Здесь вы сможете узнать официальные курсы валют на сегодня, установленные ЦБ РФ.
        Для этого воспользуйтесь командами:
        /usd
        /eur
        Дополнительные команды:
        /help
    """;
        var formattedText = String.format(text, userName);
        sendMessage(chatId,formattedText);
    }

    private void helpCommand(Long chatId){
        var text= """
                Справочная информация по боту
                
                Для получения текущих курсов валют воспользуйтесь командами:
                /usd - курс доллара
                /eur - курс евро
                """;
        sendMessage(chatId,text);
    }

    private void unknownCommand(Long chatId){
        var text = "Не удалось распознать команду!";
        sendMessage(chatId,text);
    }

    private void startCommandReceived(Long chatId, String name) {
        String answer = "Hi, " + name + ", nice to meet you!" + "\n" +
                "Enter the currency whose official exchange rate" + "\n" +
                "you want to know in relation to BYN." + "\n" +
                "For example: USD";
        sendMessage(chatId, answer);
    }

    private void sendMessage(Long chatId, String textToSend){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {

        }
    }
}