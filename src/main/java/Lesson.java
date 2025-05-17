public class Lesson {
    private int lessonNumber;
    private int groupId;
    private int teacherId;
    private String dayOfWeek;
    private int subjectId;

    public Lesson(int lessonNumber, int groupId, int teacherId, String dayOfWeek, int subjectId) {
        this.lessonNumber = lessonNumber;
        this.groupId = groupId;
        this.teacherId = teacherId;
        this.dayOfWeek = dayOfWeek;
        this.subjectId = subjectId;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public int getGroupId() {
        return groupId;
    }

    public int getLessonNumber() {
        return lessonNumber;
    }

}
