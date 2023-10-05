package Siryakov.com;

import Siryakov.com.Config.BotConfig;
import Siryakov.com.Parser.GroupParser;
import Siryakov.com.Parser.ScheduleItem;
import Siryakov.com.Parser.ScheduleParser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@AllArgsConstructor
public class NNGUbot extends TelegramLongPollingBot {

    GroupParser groupParser = new GroupParser();
    private Map<Long, Boolean> isWaitingForGroupInput = new HashMap<>();
    private final Map<Long, String> userGroups = new HashMap<>(); // Для хранения номера группы
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
    private static final String DAY2 = "/twoday";
    private static final String DAY7 = "/week";
    private static final String GROUP = "/group";
    private static final String HELP = "/help";
    private final ScheduleParser scheduleParser = new ScheduleParser();

    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }

        var message = update.getMessage().getText();
        var chatId = update.getMessage().getChatId();

        switch (message) {
            case START -> {
                String userName = update.getMessage().getChat().getUserName();
                startCommand(chatId, userName);
                sendKeyboardMessage(chatId);
            }
            case DAY, DAY2, DAY7 -> {
                int daysToAdd = message.equals(DAY) ? 1 : (message.equals(DAY2) ? 2 : 7);
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");

                for (int i = 0; i < daysToAdd; i++) {
                    String formattedDate = dateFormat.format(calendar.getTime());
                    String jsonData = scheduleParser.getScheduleForToday(formattedDate, formattedDate);
                    processScheduleData(chatId, jsonData);

                    // Увеличиваем дату на 1 день для следующего запроса (если есть)
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                }
            }
            case GROUP -> {
                // При получении команды /group, переводим пользователя в режим ввода номера группы
                sendMessage(chatId, "Введите номер группы:");
                isWaitingForGroupInput.put(chatId, true); // Устанавливаем флаг ожидания ввода группы
            }
            case HELP -> helpCommand(chatId);
            default -> {
                if (isWaitingForGroupInput.getOrDefault(chatId, false)) {
                    // Пользователь находится в режиме ввода номера группы
                    String groupName = message; // Сохраняем текст сообщения в переменную
                    // Далее можно выполнить дополнительную обработку, например, сохранить номер группы в базе данных
                    // и отправить сообщение "Ваша группа сохранена" пользователю
                    sendMessage(chatId, "Ваша группа сохранена: " + groupName);
                    isWaitingForGroupInput.put(chatId, false); // Завершаем режим ввода номера группы
                } else {
                    unknownCommand(chatId);
                }
            }
        }
    }

    private void processScheduleData(Long chatId, String jsonData) {
        if (jsonData != null) {
            List<ScheduleItem> scheduleItems = scheduleParser.parseScheduleData(jsonData);

            if (!scheduleItems.isEmpty()) {
                for (ScheduleItem item : scheduleItems) {
                    sendMessage(chatId, item.toString());
                }
            } else {
                sendMessage(chatId, "Расписание пусто.");
            }
        } else {
            sendMessage(chatId, "Не удалось получить JSON-данные.");
        }
    }
    private void startCommand(Long chatId, String userName) {
        var text = """
        Добро пожаловать,сладкая булочка, %s!
        Здесь вы сможете узнать расписание на деньписание группы 382007-3.
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

    // Функция для запроса сообщения у пользователя
    private void requestUserInput(Long chatId,Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Введите номер группы:");
        sendMessage.setReplyMarkup(createCancelKeyboard());

        // Добавим специальную команду для обработки ответов
        sendMessage.setReplyToMessageId(update.getMessage().getMessageId());

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    // Функция для создания клавиатуры для отмены операции
    private ReplyKeyboardMarkup createCancelKeyboard() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);

        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add("Отмена");

        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(keyboardRow);

        keyboardMarkup.setKeyboard(keyboard);

        return keyboardMarkup;
    }



}