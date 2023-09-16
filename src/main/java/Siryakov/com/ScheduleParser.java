package Siryakov.com;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import com.google.gson.JsonObject;
public class ScheduleParser {

    public static String getScheduleForToday(String formattedDate, String formattedEndDate) {


        // Создаем HTTP-клиент и GET-запрос с использованием сегодняшней и будущей дат
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("https://portal.unn.ru/ruzapi/schedule/student/268044?start=" + formattedDate + "&finish=" + formattedEndDate + "&lng=1");

        // Переменная для хранения ответа
        String responseBody;

        try {
            // Отправляем HTTP-запрос и получаем ответ
            responseBody = EntityUtils.toString(httpClient.execute(httpGet).getEntity());

            // Возвращаем полученные данные
            return responseBody;
        } catch (IOException e) {
            // Обработка ошибок при выполнении HTTP-запроса
            e.printStackTrace();
            return null; // Или можно выбрасывать исключение здесь, в зависимости от вашей логики обработки ошибок.
        }
    }

    /**
     * Парсит JSON-данные и создает список объектов ScheduleItem.
     *
     * @param jsonData JSON-данные для парсинга
     * @return Список объектов ScheduleItem
     */
    public static List<ScheduleItem> parseScheduleData(String jsonData) {
        // Создаем пустой список для хранения объектов ScheduleItem
        List<ScheduleItem> scheduleItems = new ArrayList<>();

        // Парсим JSON-данные в массив объектов JSON
        JsonArray jsonArray = JsonParser.parseString(jsonData).getAsJsonArray();

        // Проходим по каждому элементу в массиве и создаем объекты ScheduleItem
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject item = jsonArray.get(i).getAsJsonObject();

            // Извлекаем значения полей из объекта JSON
            String discipline = item.get("discipline").getAsString();
            String building = item.get("building").getAsString();
            String auditorium = item.get("auditorium").getAsString();
            String beginLesson = item.get("beginLesson").getAsString();
            String endLesson = item.get("endLesson").getAsString();
            String lecturerTitle = item.get("lecturer_title").getAsString();
            String date = item.get("date").getAsString();
            String dayOfWeekString = item.get("dayOfWeekString").getAsString();

            // Создаем новый объект ScheduleItem и добавляем его в список
            ScheduleItem scheduleItem = new ScheduleItem(discipline, building,auditorium ,beginLesson,endLesson, lecturerTitle,date ,dayOfWeekString);
            scheduleItems.add(scheduleItem);
        }

        // Возвращаем список объектов ScheduleItem
        return scheduleItems;
    }
}
