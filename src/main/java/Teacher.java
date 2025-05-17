public class Teacher {
    private int id;
    private String surname;

    public Teacher(int id, String surname) {
        this.id = id;
        this.surname = surname;
    }

    @Override
    public String toString() {
        return "Id " + id + " Surname " + surname;
    }

    public String getSurname() {
        return surname;
    }

    public int getId() {
        return id;
    }
}
