import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.google.gson.reflect.TypeToken;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STIconSetType;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class JsonSerializer {

    private static class DataContainer {
        private List<Teacher> teachers;
        private List<Student> students;
        private List<Group> groups;
        private List<Lesson> lessons;
        private List<Mark> marks;
        private List<Subject> subjects;

        public DataContainer(List<Teacher> teachers, List<Student> students, List<Group> groups, List<Lesson> lessons,
                             List<Mark> marks, List<Subject> subjects) {
            this.teachers = teachers;
            this.students = students;
            this.groups = groups;
            this.lessons = lessons;
            this.marks = marks;
            this.subjects = subjects;
        }

        public DataContainer() {}
    }

    private static Gson gson;
    private static FileWriter writer;
    private static FileReader reader;
    private static final String JSON_FILEPATH = "C:\\personality\\java\\sata\\teacher_journal\\src\\main\\java\\journal.json";
    private static JsonObject jsonObject;
    private static DataContainer container;
    private static boolean notEmptyFile = false;

    public static void setUp() {
        gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            writer = new FileWriter(JSON_FILEPATH);
            reader = new FileReader(JSON_FILEPATH);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        container = new DataContainer();
    }
    public static void serializeTeachers(List<Teacher> teachers) {
        container.teachers = teachers;
    }
    public static void serializeStudents(List<Student> students) {
        container.students = students;
    }

    public static void serializeGroups(List<Group> groups) {
        container.groups = groups;
    }

    public static void serializeMarks(List<Mark> marks) {
        container.marks = marks;
    }

    public static void serializeSubjects(List<Subject> subjects) {
        container.subjects = subjects;
    }

    public static void serializeLessons(List<Lesson> lessons) {
        container.lessons = lessons;
    }
    public static List<Teacher> getTeachers() {
        return container.teachers;
    }

    public static List<Student> getStudents() {
        return container.students;
    }
    public static List<Group> getGroups() {
        return container.groups;
    }
    public static List<Lesson> getLessons() {
        return container.lessons;
    }
    public static List<Mark> getMarks() {
        return container.marks;
    }
    public static List<Subject> getSubjects() {
        return container.subjects;
    }

    public static void serializeInFile() {
        gson.toJson(container, writer);
        try {
            writer.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        notEmptyFile = true;
    }

    public static Group getGroupById(int id) {
        List<Group> groups = container.groups;
        return groups.stream().filter(group -> group.getId() == id).findFirst().orElse(null);
    }

    public static Group getGroupByNumber(String number) {
        List<Group> groups = container.groups;
        return groups.stream().filter(group -> group.getNumber().equals(number)).findFirst().orElse(null);
    }

    public static List<Lesson> getLessonsByTeacherId(int teacherId) {
        List<Lesson> lessons = container.lessons;
        return lessons.stream().filter(lesson -> lesson.getTeacherId() == teacherId).collect(Collectors.toList());
    }

    public static Subject getSubjectById(int subjectId) {
        List<Subject> subjects = container.subjects;
        return subjects.stream().filter(subject -> subject.getId() == subjectId).findFirst().orElse(null);
    }

    public static List<Student> getStudentsByGroupId(int groupId) {
        List<Student> students = container.students;
        return students.stream().filter(student -> student.getGroupId() == groupId).collect(Collectors.toList());
    }
    public static List<Mark> getMarksByStudentId(int studentId) {
        List<Mark> marks = container.marks;
        return marks.stream().filter(mark -> mark.getStudentId() == studentId).collect(Collectors.toList());
    }

    public static void removeStudent(Student student) {
        container.students.remove(student);
    }

    public static void removeLesson(int ind) {
        container.lessons.remove(ind);
    }
    public static void addLesson(Lesson lesson) {
        container.lessons.add(lesson);
    }

    public static void addMark(Mark mark) {
        container.marks.add(mark);
    }
}