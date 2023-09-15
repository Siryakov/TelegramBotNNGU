package Siryakov.com;


import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
public class Main {


        public static void main(String[] args) {
            SpringApplication.run(Main.class, args);
        }
//        ScheduleParser scheduleParser = new ScheduleParser();
//
//        // Получаем JSON-данные для сегодняшней даты
//        String jsonData = scheduleParser.getScheduleForToday();
//
//        if (jsonData != null) {
//            // Парсим JSON и получаем список ScheduleItem
//            List<ScheduleItem> scheduleItems = scheduleParser.parseScheduleData(jsonData);
//
//            // Выводим данные на экран
//            for (ScheduleItem item : scheduleItems) {
//                System.out.println(item);
//            }
//        } else {
//            System.out.println("Не удалось получить JSON-данные.");
//        }
    }


