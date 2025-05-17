import java.util.List;
import java.util.Scanner;

public class Main {
    public static final String FILE_PATH = "C:\\personality\\java\\sata\\teacher_journal\\src\\main\\java\\jorunal2.xlsx";
    public static void main(String[] args) {
        ExcelManager.openExcel(FILE_PATH);
        serializeData();

        JsonSerializer.getTeachers().stream().forEach(teacher -> {System.out.println(teacher);});

        Scanner scanner = new Scanner(System.in);
        Menu menu = new Menu(scanner);
        menu.openMenu();
        menu.chooseAction();
    }

    public static void serializeData() {
        JsonSerializer.setUp();
        JsonSerializer.serializeTeachers(ExcelManager.getTeachers());
        JsonSerializer.serializeStudents(ExcelManager.getStudents());
        JsonSerializer.serializeMarks(ExcelManager.getMarks());
        JsonSerializer.serializeSubjects(ExcelManager.getSubjects());
        JsonSerializer.serializeLessons(ExcelManager.getLessons());
        JsonSerializer.serializeGroups(ExcelManager.getGroups());
    }

    public static void deserializeData() {
        JsonSerializer.serializeInFile();
        ExcelManager.saveData();
        ExcelManager.closeExcel();
    }
}
