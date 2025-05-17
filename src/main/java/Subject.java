public class Subject {
    private int id;
    private String name;
    private int teacherId;

    public Subject(int id, String name, int teacherId) {
        this.id = id;
        this.name = name;
        this.teacherId = teacherId;
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public int getTeacherId() {
        return teacherId;
    }
}
