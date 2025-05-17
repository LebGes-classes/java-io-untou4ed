public class Mark {
    private int id;
    private int studentId;
    private int subjectId;
    private int mark;
    public Mark(int id, int studentId, int subjectId, int mark) {
        this.id = id;
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.mark = mark;
    }

    public int getId() {
        return id;
    }
    public int getMark() {
        return mark;
    }
    public int getStudentId() {
        return studentId;
    }
    public int getSubjectId() {
        return subjectId;
    }
}
