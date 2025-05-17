import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.exit;

public class Menu {

    private boolean authorizated = false;
    private String teacherName;
    private int teacherId;
    private final Scanner scanner;

    Menu(Scanner scanner) {
        this.scanner = scanner;
    }

    public void openMenu() {
        authorization();
    }

    private void authorization() {
        List<Teacher> teachers = JsonSerializer.getTeachers();
        if(teachers.isEmpty()) {
            System.out.println("В базе еще нет преподавателей.");
            System.out.println("Нажмите enter чтобы выйти");
            scanner.nextLine();
            exit(1);
        }
        System.out.println("Введите вашу фамилию: ");
        String teacherSurname = scanner.nextLine();

        for (Teacher teacher : teachers) {
            if(teacher.getSurname().equals(teacherSurname)) {
                this.teacherName = teacherName;
                teacherId = teacher.getId();
                authorizated = true;
                System.out.println("Успешно авторизирован");
                System.out.println("Нажмите enter чтобы продолжить");
                scanner.nextLine();
                break;
            }
        }
        if (!authorizated) {
            System.out.println("Преподавателя с такой фамилией не найдено.");
            authorization();
            return;
        }
        chooseAction();
    }

    public void chooseAction() {
        System.out.println("1. Посмотреть или изменить свое расписание.");
        System.out.println("2. Посмотреть студентов группы");
        System.out.println("3. Изменить оценки");
        System.out.println("4. Посмотреть или изменить студентов");
        System.out.println("5. Закончить работу");
        int answer = Integer.parseInt(scanner.nextLine());
        if (answer < 1 || answer > 5) {
            System.out.println("Неверный ввод");
            chooseAction();
            return;
        }
        switch (answer) {
            case 1:
                openSchedule();
                break;
            case 2:
                openGroups();
                break;
            case 3:
                changeMarks();
                break;
            case 4:
                changeStudents();
                break;
            case 5:
                Main.deserializeData();
                exit(0);
                break;
        }
    }

    private void openSchedule() {
        System.out.println("1. Посмотреть расписание");
        System.out.println("2. Изменить расписание");
        System.out.println("3. Назад");
        System.out.println("Выберите действие: ");
        int answer = Integer.parseInt(scanner.nextLine());
        if (answer < 1 || answer > 4) {
            System.out.println("Неверный номер действия");
            openSchedule();
        } else if (answer == 3) {
            chooseAction();
        } else if (answer == 1) {
            showSchedule();
        } else {
            changeSchedule();
        }

    }
    private void showSchedule() {
        List<Lesson> lessons = JsonSerializer.getLessonsByTeacherId(teacherId);
        if (lessons.isEmpty()) {
            System.out.println("У вас еще нет расписания, отдыхайте!");
            chooseAction();
            return;
        }
        System.out.println("Ваше расписание: ");
        for (Lesson lesson : lessons) {
            String groupNumber = JsonSerializer.getGroupById(lesson.getGroupId()).getNumber();
            String subjectName = JsonSerializer.getSubjectById(lesson.getSubjectId()).getName();
            System.out.println(lesson.getDayOfWeek() + " " + lesson.getLessonNumber() + ". Предмет: " + subjectName + ". Группа: " + groupNumber);
        }
    }

    private void changeSchedule() {
        System.out.println("1. Удалить пару");
        System.out.println("2. Добавить пару");
        System.out.println("3. Выйти");
        int answer = Integer.parseInt(scanner.nextLine());
        if (answer < 1 || answer > 3) {
            System.out.println("Неверный номер");
            scanner.nextLine();
            chooseAction();
        } else if (answer == 3) {
            chooseAction();
        } else if (answer == 1) {
            removeLesson();
        } else {
            addLesson();
        }
    }

    public void addLesson() {
        System.out.println("Введите группу у которой нужно добавить пару");
        String groupNumber = scanner.nextLine();
        int groupId = -1;
        boolean correctGroup = false;
        for (Group group : JsonSerializer.getGroups()) {
            if (group.getNumber().equals(groupNumber)) {
                correctGroup = true;
                groupId = group.getId();
                break;
            }
        }
        if (!correctGroup) {
            System.out.println("Группы с таким номером не найдено");
            System.out.println("Нажмите enter чтобы выйти");
            scanner.nextLine();
            changeSchedule();
            return;
        }
        System.out.println("Введите предмет который хотите добавить");
        String subjectName = scanner.nextLine();
        boolean correctSubject = false;
        int subjectId = -1;
        for (Subject subject : JsonSerializer.getSubjects()) {
            if (subject.getName().equals(subjectName)) {
                if (subject.getTeacherId() == teacherId) {
                    correctSubject = true;
                    subjectId = subject.getId();
                    break;
                } else {
                    System.out.println("Вы не ведете этот предмет");
                    System.out.println("Нажмите enter чтобы выйти");
                    scanner.nextLine();
                    changeSchedule();
                    return;
                }
            }
        }
        if (!correctSubject) {
            System.out.println("Не получится добавить пару по этому предмету.");
            System.out.println("Нажмите enter чтобы выйти");
            scanner.nextLine();
            changeSchedule();
            return;
        }
        System.out.println("Введите номер пары, который хотите добавить.");
        int lessonNumber = Integer.parseInt(scanner.nextLine());
        if (lessonNumber < 1) {
            System.out.println("Неверный номер");

        }
        System.out.println("Введите день недели, в который будет пара.");
        String dayOfWeek = scanner.nextLine();
        for (Lesson lesson : JsonSerializer.getLessons()) {
            if (lesson.getDayOfWeek().equals(dayOfWeek) && lesson.getLessonNumber() == lessonNumber &&
                    lesson.getGroupId() == groupId && lesson.getTeacherId() == teacherId) {
                System.out.println("Такая пара уже есть");
                System.out.println("Нажмите enter чтобы выйти");
                scanner.nextLine();
                changeSchedule();
                return;
            }
        }
        Lesson lesson = new Lesson(lessonNumber, groupId, teacherId, dayOfWeek, subjectId);
        JsonSerializer.addLesson(lesson);
        System.out.println("Пара добавлена");
        System.out.println("Нажмите enter чтобы выйти");
        scanner.nextLine();
        changeSchedule();
    }

    public void removeLesson() {
        List<Lesson> lessons = JsonSerializer.getLessonsByTeacherId(teacherId);
        if (lessons.isEmpty()) {
            System.out.println("У вас еще нет пар.");
            System.out.println("Нажмите enter чтобы вернуться");
            scanner.nextLine();
            changeSchedule();
        } else {
            System.out.println("Выберите номер пары, которую нужно убрать");
            System.out.println("0. Назад");
            printLessons(lessons);
            int answer = Integer.parseInt(scanner.nextLine());
            if (answer < 0 || answer > lessons.size()) {
                System.out.println("Неправильный ввод");
            } else if (answer == 0) {
                changeSchedule();
            } else {
                JsonSerializer.removeLesson(answer - 1);
                System.out.println("Удалено");
                System.out.println("Нажмите enter чтобы выйти");
                scanner.nextLine();
            }
        }
    }

    public void printLessons(List<Lesson> lessons) {
        int ind = 0;
        for (Lesson lesson : lessons) {
            System.out.println(++ind + "Пара № " +lesson.getLessonNumber() +
                    ", день недели:" + lesson.getDayOfWeek() +  "у группы: " +
                    JsonSerializer.getGroupById(lesson.getGroupId()) + ", предмет: " +
                    JsonSerializer.getSubjectById(lesson.getSubjectId()));
        }
    }

    private void openGroups() {
        System.out.println("Введите номер группы или 0 чтобы вернуться:");
        String groupNumber = scanner.nextLine();
        if (groupNumber.equals("0")) {
            chooseAction();
            return;
        }
        Group group = JsonSerializer.getGroupByNumber(groupNumber);
        if (group == null) {
            System.out.println("Групп с таким номером не найдено");
            System.out.println("Нажмите enter чтобы вернуться");
            scanner.nextLine();
            openGroups();
            return;
        }
        List<Student>  students = JsonSerializer.getStudentsByGroupId(group.getId());
        int ind = 0;
        for (Student student : students) {
            System.out.println(++ind + ". " + student.getSurname());
        }
        System.out.println("Введите номер студента (0 чтобы выйти)");
        int studentNumber = Integer.parseInt(scanner.nextLine());
        if (studentNumber == 0) {
            chooseAction();
            return;
        }
        if (studentNumber < 1 || studentNumber > students.size()) {
            System.out.println("Неверный номер");
            System.out.println("Нажмитер enter чтобы выйти");
            scanner.nextLine();
            openGroups();
            return;
        }
        chooseStudentAction(students.get(studentNumber - 1));
    }

    private void changeMarks() {
        System.out.println("Выберите студента: ");
        List<Student> students = JsonSerializer.getStudents();
        int ind = 0;
        for(Student student: students) {
            System.out.println(++ind + ". Студент: " + student.getSurname()+ ", из группы" + JsonSerializer.getGroupById(student.getGroupId()));
        }
        int answer = Integer.parseInt(scanner.nextLine());
        if (answer < 1 || answer > students.size()) {
            System.out.println("Неверный номер.");
            System.out.println("Нажмите enter для выхода.");
            changeMarks();
            return;
        }
        Student student = students.get(answer - 1);
        System.out.println("Выберите предмет, по которому нужно поставить оценку");
        ind = 0;
        List<Subject> subjects = JsonSerializer.getSubjects();
        for (Subject subject : subjects) {
            if (subject.getTeacherId() == teacherId) {
                System.out.println(++ind + ". " + subject.getName());
            }
        }
        if (ind == 0) {
            System.out.println("Вы не ведете ни одного предмета");
            System.out.println("Нажмите enter чтобы выйти");
            changeMarks();
            return;
        }
        int subjectNumber = Integer.parseInt(scanner.nextLine());
        if (subjectNumber < 1 || subjectNumber > subjects.size()) {
            System.out.println("Неверный ввод.");
            System.out.println("Нажмите enter чтобы выйти");
            changeMarks();
            return;
        }
        Subject subject = subjects.get(subjectNumber - 1);
        System.out.println("Выберите оценку (от 1 до 5)");
        int markValue = Integer.parseInt(scanner.nextLine());
        if (markValue < 1 || markValue > 5) {
            System.out.println("Неверный ввод.");
            System.out.println("Нажмите enter чтобы выйти");
            changeMarks();
            return;
        }
        Mark mark;
        if (JsonSerializer.getMarks().isEmpty()) {
            mark = new Mark(0, student.getId(), subject.getId(), markValue);
        } else {
            mark = new Mark(JsonSerializer.getMarks().getLast().getId() + 1, student.getId(), subject.getId(), markValue);
        }
        JsonSerializer.addMark(mark);
        System.out.println("Оценка добавлена");
        System.out.println("Нажмите enter, чтобы выйти");
        scanner.nextLine();
        changeMarks();
    }

    private void changeStudents() {}

    private void chooseStudentAction(Student student) {
        String groupNumber = JsonSerializer.getGroupById(student.getGroupId()).getNumber();
        System.out.println("студент " + student.getSurname() + " из группы " + groupNumber);
        System.out.println("1. Посмотреть оценки");
        System.out.println("2. Узнать средний балл");
        System.out.println("3. Отчислить");
        System.out.println("4. Назад");
        int answer = Integer.parseInt(scanner.nextLine());
        if (answer < 1 || answer > 4) {
            System.out.println("Неверный номер");
            System.out.println("Нажмите enter чтобы вернуться");
            scanner.nextLine();
            chooseAction();
            return;
        }
        if (answer == 4) {
            chooseAction();
        } else if (answer == 1) {
            List<Mark> marks = JsonSerializer.getMarksByStudentId(student.getId());
            if (marks.isEmpty()) {
                System.out.println("У студента еще нет оценок");
                chooseStudentAction(student);
            }
            for (Mark mark : marks) {
                System.out.println("Предмет: " + JsonSerializer.getSubjectById(mark.getId()).getName() + ", оценка: " + mark.getMark());
            }
            scanner.nextLine();
            System.out.println("Нажмите enter чтобы выйти");
            chooseAction();
        } else if (answer == 2) {
            List<Mark> marks = JsonSerializer.getMarksByStudentId(student.getId());
            if (marks.isEmpty()) {
                System.out.println("У студента еще нет оценок");
                chooseStudentAction(student);
            }
            int sum = 0;
            for (Mark mark : marks) {
                sum += mark.getMark();
            }
            System.out.println("Всего оценок: " + marks.size() + ", средний балл: " + (sum / (double) marks.size()));
            System.out.println("Нажмите enter чтобы вернуться");
            scanner.nextLine();
            chooseAction();
        } else {
            JsonSerializer.removeStudent(student);
            System.out.println("Студент успешно отчислен.");
            System.out.println("Нажмите enter чтобы выйти");
            scanner.nextLine();
            chooseAction();
        }

    }
}
