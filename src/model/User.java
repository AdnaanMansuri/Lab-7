package model;

/**
 * Model class representing a single student record stored in the database.
 *
 * Mirrors the "student" table used by {@code utility.DatabaseConnector}:
 *   student_id | first_name | college | age | hobbies
 */
public class User {

    private int userId;
    private String name;
    private String college;
    private int age;
    private String hobby;

    public User() {
    }

    public User(String name, int age, String college, String hobby) {
        this.name = name;
        this.age = age;
        this.college = college;
        this.hobby = hobby;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    /**
     * Friendly summary shown in the "Success!" dialog after an insert.
     */
    @Override
    public String toString() {
        return "Welcome " + name + " to " + college + "!"
                + "\n\nName: " + name
                + "\nAge: " + age
                + "\nCollege: " + college
                + "\nHobbies: " + hobby;
    }
}
