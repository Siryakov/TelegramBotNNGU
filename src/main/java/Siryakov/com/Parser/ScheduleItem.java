package Siryakov.com.Parser;

// Объявление класса ScheduleItem
public class ScheduleItem {
    // Приватные поля для хранения данных
    private String discipline;      // Дисциплина
    private String building;        // Здание
    private String auditorium;      //Аудитория
    private String beginLesson;     // Начало занятия
    private String endLesson;       //Конец занятия
    private String lecturerTitle;   // Звание лектора
    private String dayOfWeekString; // День недели
    private String date; //Дата
    public ScheduleItem() {
        // Пустой конструктор, который может пригодиться при создании объектов через Spring
    }

    // Конструктор класса, который принимает значения для полей при создании объекта
    public ScheduleItem(String discipline, String building,String auditorium, String beginLesson,String endLesson, String lecturerTitle ,String dayOfWeekString, String date) {
        this.discipline = discipline;
        this.building = building;
        this.auditorium = auditorium;
        this.beginLesson = beginLesson;
        this.endLesson = endLesson;
        this.lecturerTitle = lecturerTitle;
        this.dayOfWeekString =dayOfWeekString;
        this.date = date;
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

    public String getDayOfWeekString() {
        return dayOfWeekString;
    }

    public void setDayOfWeekString(String dayOfWeekString) {
        this.dayOfWeekString = dayOfWeekString;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public String getEndLesson() {
        return endLesson;
    }

    public void setEndLesson(String endLesson) {
        this.endLesson = endLesson;
    }
    public String getAuditorium() {
        return auditorium;
    }

    public void setAuditorium(String auditorium) {
        this.auditorium = auditorium;
    }
    // Переопределение метода toString для удобного вывода информации о объекте
    @Override
    public String toString() {
        return  date +" "+ dayOfWeekString + "\n" +
                beginLesson + " - " + endLesson +  "\n" +
                discipline + "\n" +
                building + " " + auditorium +"\n" +
                lecturerTitle + "\n";
    }



}
