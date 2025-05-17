public class Student {
    private int id;
    private String surname;
    private int groupId;

    public Student(int id, String surname, int groupId) {
        this.id = id;
        this.surname = surname;
        this.groupId = groupId;
    }

    public int getGroupId() {
        return groupId;
    }

    public String getSurname() {
        return surname;
    }

    public int getId() {
        return id;
    }
}
