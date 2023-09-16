package Siryakov.com;

import Siryakov.com.Config.BotConfig;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
@AllArgsConstructor
public class NNGUbot extends TelegramLongPollingBot {
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
    private static final String DAY = "Расписание на день";
    private static final String DAY2 = "Расписание на два дня";
    private static final String DAY7 = "Расписание на неделю";
    private static final String HELP = "Помощь";

    public void onUpdateReceived(Update update) {
        // Получаем текущую дату
        Calendar calendar = Calendar.getInstance();
        // Форматируем даты в строки
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        String formattedDate = dateFormat.format(new Date());

        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }
        var message = update.getMessage().getText(); // сохраняем сообщение пользователя в переменную
        var chatId = update.getMessage().getChatId(); // идентификатор чата, что бы отправить ответ именно тому пользователю

        // проверка, какую команду прислал пользователь
        switch (message) {
            case START -> {
                String userName = update.getMessage().getChat().getUserName();
                startCommand(chatId, userName);
                sendKeyboardMessage(chatId);
            }
            case DAY -> {
                // Прибавляем 0 дней к текущей дате
                calendar.add(Calendar.DAY_OF_MONTH, 0);
                String formattedEndDate = dateFormat.format(calendar.getTime());

                ScheduleParser scheduleParser = new ScheduleParser();

                // Получаем JSON-данные для сегодняшней даты
                String jsonData = scheduleParser.getScheduleForToday(formattedDate, formattedEndDate);
                if (jsonData != null) {
                    // Парсим JSON и получаем список ScheduleItem
                    List<ScheduleItem> scheduleItems = scheduleParser.parseScheduleData(jsonData);

                    // Выводим данные на экран
                    for (ScheduleItem item : scheduleItems) {
                        sendMessage(chatId, item.toString());
                    }
                } else {
                    sendMessage(chatId, "Не удалось получить JSON-данные.");
                }
            }
            case DAY2 -> {
                // Прибавляем 1 дней к текущей дате
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                String formattedEndDate = dateFormat.format(calendar.getTime());

                ScheduleParser scheduleParser = new ScheduleParser();

                // Получаем JSON-данные для сегодняшней даты
                String jsonData = scheduleParser.getScheduleForToday(formattedDate, formattedEndDate);
                if (jsonData != null) {
                    // Парсим JSON и получаем список ScheduleItem
                    List<ScheduleItem> scheduleItems = scheduleParser.parseScheduleData(jsonData);

                    // Выводим данные на экран
                    for (ScheduleItem item : scheduleItems) {
                        sendMessage(chatId, item.toString());
                    }
                } else {
                    sendMessage(chatId, "Не удалось получить JSON-данные.");
                }
            }
            case DAY7 -> {
                // Прибавляем 7 дней к текущей дате
                calendar.add(Calendar.DAY_OF_MONTH, 7);
                String formattedEndDate = dateFormat.format(calendar.getTime());

                ScheduleParser scheduleParser = new ScheduleParser();

                // Получаем JSON-данные для сегодняшней даты
                String jsonData = scheduleParser.getScheduleForToday(formattedDate, formattedEndDate);
                if (jsonData != null) {
                    // Парсим JSON и получаем список ScheduleItem
                    List<ScheduleItem> scheduleItems = scheduleParser.parseScheduleData(jsonData);

                    // Выводим данные на экран
                    for (ScheduleItem item : scheduleItems) {
                        sendMessage(chatId, item.toString());
                    }
                } else {
                    sendMessage(chatId, "Не удалось получить JSON-данные.");
                }
            }
            case HELP -> helpCommand(chatId);
            default -> unknownCommand(chatId);
        }
    }
    private void startCommand(Long chatId, String userName) {
        var text = """
        Добро пожаловать,сладкая булочка, %s!
        Здесь вы сможете узнать рассписание группы 382007-3.
        Для этого воспользуйтесь командами:
          Расписание на день
          Расписание на два дня
          Расписание на неделю
                
        Дополнительные команды:
        Помощь
    """;
        var formattedText = String.format(text, userName);
        sendMessage(chatId, formattedText);
    }

    private void helpCommand(Long chatId) {
        var text = """
                Справочная информация по боту
                
                Для получения расписания воспользуйтесь командами:
                Расписание на день
                Расписание на два дня
                Расписание на неделю
                Помощь
                
                """;
        sendMessage(chatId, text);
    }

    private void unknownCommand(Long chatId) {
        var text = "Не удалось распознать команду!";
        sendMessage(chatId, text);
    }

    private void startCommandReceived(Long chatId, String name) {
        String answer = "Hi, " + name + ", nice to meet you!" + "\n" +
                "Enter the currency whose official exchange rate" + "\n" +
                "you want to know in relation to BYN." + "\n" +
                "For example: USD";
        sendMessage(chatId, answer);
    }

    private void sendMessage(Long chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private SendMessage createKeyboardMessage(Long chatId) {
        SendMessage message = new SendMessage();

        // Устанавливаем чат ID
        message.setChatId(chatId);

        // Устанавливаем текст сообщения
        message.setText("Выберите действие:");

        // Создаем клавиатуру
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        // Создаем первую строку клавиатуры
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add("Расписание на день");
        keyboardFirstRow.add("Расписание на два дня");

        // Создаем вторую строку клавиатуры
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add("Расписание на неделю");
        keyboardSecondRow.add("Помощь");

        // Добавляем строки клавиатуры
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);

        // Устанавливаем клавиатуру в сообщении
        keyboardMarkup.setKeyboard(keyboard);

        // Устанавливаем клавиатуру в сообщении
        message.setReplyMarkup(keyboardMarkup);

        return message;
    }

    // Метод для отправки сообщения  кнопками
    private void sendKeyboardMessage(Long chatId) {
        SendMessage message = createKeyboardMessage(chatId);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}