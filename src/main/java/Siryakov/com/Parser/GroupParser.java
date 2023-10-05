package Siryakov.com.Parser;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
public class GroupParser {

    public static String getGroup (String groupNumber) {

        // Создаем HTTP-клиент и GET-запрос
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(" https://portal.unn.ru/ruzapi/search?term=" + groupNumber + "&type=group");
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

}
