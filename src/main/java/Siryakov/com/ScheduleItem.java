package Siryakov.com;

// Объявление класса ScheduleItem
public class ScheduleItem {
    // Приватные поля для хранения данных
    private String discipline;      // Дисциплина
    private String building;        // Здание
    private String beginLesson;     // Начало занятия
    private String lecturerTitle;   // Звание лектора

    public ScheduleItem() {
        // Пустой конструктор, который может пригодиться при создании объектов через Spring
    }

    // Конструктор класса, который принимает значения для полей при создании объекта
    public ScheduleItem(String discipline, String building, String beginLesson, String lecturerTitle) {
        this.discipline = discipline;
        this.building = building;
        this.beginLesson = beginLesson;
        this.lecturerTitle = lecturerTitle;
    }

    // Геттеры для получения данных из объекта

    // Геттер для получения названия дисциплины
    public String getDiscipline() {
        return discipline;
    }

    // Геттер для получения названия здания
    public String getBuilding() {
        return building;
    }

    // Геттер для получения времени начала занятия
    public String getBeginLesson() {
        return beginLesson;
    }

    // Геттер для получения имени лектора
    public String getLecturerTitle() {
        return lecturerTitle;
    }

    // Переопределение метода toString для удобного вывода информации о объекте
    @Override
    public String toString() {
        return "Discipline: " + discipline + "\n" +
                "Building: " + building + "\n" +
                "Begin Lesson: " + beginLesson + "\n" +
                "Lecturer Title: " + lecturerTitle + "\n";
    }
}
