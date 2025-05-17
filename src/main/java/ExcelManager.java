import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelManager {

    private static FileInputStream inputFile;
    private static Workbook workbook;
    private static boolean opened = false;
    private static String filePath;

    public static void openExcel(String filepath) {
        if (opened) {
            System.out.println("Excel file is already opened.");
            return;
        }
        try {
            filePath = filepath;
            inputFile = new FileInputStream(filepath);
            workbook = new XSSFWorkbook(inputFile);
            opened = true;
        } catch (IOException e) {
            System.out.println("Error opening excel file");
            System.out.println(e.getMessage());
        }
    }

    public static void closeExcel() {
        if (!opened) {
            System.out.println("Excel file is already closed");
            return;
        }
        try {
            workbook.close();
            inputFile.close();
            opened = false;
        } catch (IOException e) {
            System.out.println("Error closing excel file");
        }
    }

    public static ArrayList<Teacher> getTeachers() {
        if (!opened) {
            System.out.println("Excel file is not opened");
            return null;
        }
        ArrayList<Teacher> teachers = new ArrayList<>();
        Sheet sheet = workbook.getSheetAt(0);
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            teachers.add(new Teacher((int)(row.getCell(0).getNumericCellValue()), row.getCell(1).getStringCellValue()));
        }
        return teachers;
    }

    public static ArrayList<Student> getStudents() {
        if (!opened) {
            System.out.println("Excel file is not opened");
            return null;
        }
        ArrayList<Student> students = new ArrayList<>();
        Sheet sheet = workbook.getSheetAt(1);
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            students.add(new Student((int)(row.getCell(0).getNumericCellValue()), row.getCell(1).getStringCellValue(), (int)row.getCell(2).getNumericCellValue()));
        }
        return students;
    }

    public static ArrayList<Mark> getMarks() {
        if (!opened) {
            System.out.println("Excel file is not opened");
            return null;
        }
        ArrayList<Mark> marks = new ArrayList<>();
        Sheet sheet = workbook.getSheetAt(3);
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            int id = (int) row.getCell(0).getNumericCellValue();
            int studentId = (int) row.getCell(1).getNumericCellValue();
            int subjectId = (int) row.getCell(2).getNumericCellValue();
            int mark = (int) row.getCell(3).getNumericCellValue();
            marks.add(new Mark(id, studentId, subjectId, mark));
        }
        return marks;
    }

    public static ArrayList<Lesson> getLessons() {
        if (!opened) {
            System.out.println("Excel file is not opened");
            return null;
        }
        ArrayList<Lesson> lessons = new ArrayList<>();
        Sheet sheet = workbook.getSheetAt(2);
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            int lessonNumber = (int) row.getCell(0).getNumericCellValue();
            int groupId = (int) row.getCell(1).getNumericCellValue();
            int teacherId = (int) row.getCell(2).getNumericCellValue();
            String dayOfWeek = row.getCell(3).getStringCellValue();
            int subjectId = (int) row.getCell(4).getNumericCellValue();
            lessons.add(new Lesson(lessonNumber, groupId, teacherId, dayOfWeek, subjectId));
        }
        return lessons;
    }

    public static List<Subject> getSubjects() {
        if (!opened) {
            System.out.println("Excel file is not opened");
            return null;
        }
        ArrayList<Subject> subjects = new ArrayList<>();
        Sheet sheet = workbook.getSheetAt(4);
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            int id = (int) row.getCell(0).getNumericCellValue();
            String name = row.getCell(1).getStringCellValue();

            int teacherId = (int) row.getCell(2).getNumericCellValue();
            subjects.add(new Subject(id, name, teacherId));
        }
        return subjects;
    }

    public static List<Group> getGroups() {
        if (!opened) {
            System.out.println("Excel file is not opened");
            return null;
        }
        ArrayList<Group> groups = new ArrayList<>();
        Sheet sheet = workbook.getSheetAt(5);
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            int id = (int) row.getCell(0).getNumericCellValue();
            String number = row.getCell(1).getStringCellValue();
            groups.add(new Group(id, number));
        }
        return groups;
    }

    public static void saveData() {
        if (!opened) {
            System.out.println("Excel file is not opened");
            return;
        }
        for (Sheet sheet : workbook) {
            int lastRowNum = sheet.getLastRowNum();

            // Удаляем все строки, кроме первой (заголовков)
            for (int i = lastRowNum; i >= 1; i--) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    sheet.removeRow(row);
                }
            }
        }

        saveTeachers(JsonSerializer.getTeachers());
        saveStudents(JsonSerializer.getStudents());
        saveLessons(JsonSerializer.getLessons());
        saveMarks(JsonSerializer.getMarks());
        saveSubjects(JsonSerializer.getSubjects());
        saveGroups(JsonSerializer.getGroups());
        try {
            workbook.write(new FileOutputStream(filePath));
            workbook.close();
        }  catch (IOException e) {
            System.out.println("Saving excel file failed");
            System.out.println(e.getMessage());
        }
    }
    public static void saveTeachers(List<Teacher> teachers) {
        Sheet sheet = workbook.getSheetAt(0);
        for (int i = 0; i < teachers.size(); i++) {
            Row row = sheet.createRow(i + 1); // Пропускаем строку заголовков
            Cell cell = row.createCell(0);
            cell.setCellValue(teachers.get(i).getId());
            cell = row.createCell(1);
            cell.setCellValue(teachers.get(i).getSurname());
        }
    }

    public static void saveStudents(List<Student> students) {
        Sheet sheet = workbook.getSheetAt(1);
        for (int i = 0; i < students.size(); i++) {
            Row row = sheet.createRow(i + 1);
            Cell cell = row.createCell(0);
            cell.setCellValue(students.get(i).getId());
            cell = row.createCell(1);
            cell.setCellValue(students.get(i).getSurname());
            cell = row.createCell(2);
            cell.setCellValue(students.get(i).getGroupId());
        }
    }

    public static void saveLessons(List<Lesson> lessons) {
        Sheet sheet = workbook.getSheetAt(2);
        for (int i = 0; i < lessons.size(); i++) {
            Row row = sheet.createRow(i + 1);
            Cell cell = row.createCell(0);
            cell.setCellValue(lessons.get(i).getLessonNumber());
            cell = row.createCell(1);
            cell.setCellValue(lessons.get(i).getGroupId());
            cell = row.createCell(2);
            cell.setCellValue(lessons.get(i).getTeacherId());
            cell = row.createCell(3);
            cell.setCellValue(lessons.get(i).getDayOfWeek());
            cell = row.createCell(4);
            cell.setCellValue(lessons.get(i).getSubjectId());
        }
    }

    public static void saveMarks(List<Mark> marks) {
        Sheet sheet = workbook.getSheetAt(3);
        for (int i = 0; i < marks.size(); i++) {
            Row row = sheet.createRow(i + 1);
            Cell cell = row.createCell(0);
            cell.setCellValue(marks.get(i).getId());
            cell = row.createCell(1);
            cell.setCellValue(marks.get(i).getStudentId());
            cell = row.createCell(2);
            cell.setCellValue(marks.get(i).getSubjectId());
            cell = row.createCell(3);
            cell.setCellValue(marks.get(i).getMark());
        }
    }

    public static void saveSubjects(List<Subject> subjects) {
        Sheet sheet = workbook.getSheetAt(4);
        for (int i = 0; i < subjects.size(); i++) {
            Row row = sheet.createRow(i + 1);
            Cell cell = row.createCell(0);
            cell.setCellValue(subjects.get(i).getId());
            cell = row.createCell(1);
            cell.setCellValue(subjects.get(i).getName());
            cell = row.createCell(2);
            cell.setCellValue(subjects.get(i).getTeacherId());
        }
    }

    public static void saveGroups(List<Group> groups) {
        Sheet sheet = workbook.getSheetAt(5);
        for (int i = 0; i < groups.size(); i++) {
            Row row = sheet.createRow(i + 1);
            Cell cell = row.createCell(0);
            cell.setCellValue(groups.get(i).getId());
            cell = row.createCell(1);
            cell.setCellValue(groups.get(i).getNumber());
        }
    }

//    public static void printSchedule(int teacherId) {
//        if (!opened) {
//            System.out.println("Excel file is not opened");
//        }
//        Sheet sheet = workbook.getSheetAt(2);
//        for (Row row : sheet) {
//            if (row.getZeroHeight()) {
//                continue;
//            }
//            // добавить сортировку по дням
//            if ((int)row.getCell(2).getNumericCellValue() == teacherId) {
//                int group = getGroup((int)row.getCell(1).getNumericCellValue());
//                int subject = getSubject((int)row.getCell(4).getNumericCellValue());
//                System.out.println('\t' + (int) row.getCell(0).getNumericCellValue() + ". " +
//                        "Группа: " + group + ", Предмет: " + subject);
//            }
//        }
//    }

}
