package model;

import java.util.Date;

/**
 * Model class representing a single student record stored in the database.
 *
 * Mirrors the "student" table used by {@code utility.DatabaseConnector}:
 *   student_id | first_name | last_name | gender | dob | age |
 *   phone | email | college | experience | hobbies | photo
 *
 * The photo is stored as the raw image bytes (a MySQL LONGBLOB) so the picture
 * travels inside the database itself.
 */
public class User {

    private int userId;
    private String firstName;
    private String lastName;
    private String gender;
    private Date dob;          // date of birth (chosen with the calendar)
    private int age;           // auto-calculated from the date of birth
    private String phone;
    private String email;
    private String college;
    private String experience;
    private String hobby;
    private byte[] photo;      // raw image bytes -> stored as a BLOB

    public User() {
    }

    public User(String firstName, String lastName, String gender, Date dob, int age,
                String phone, String email, String college, String experience,
                String hobby, byte[] photo) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.dob = dob;
        this.age = age;
        this.phone = phone;
        this.email = email;
        this.college = college;
        this.experience = experience;
        this.hobby = hobby;
        this.photo = photo;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /** Convenience: "First Last" (used for dialogs and greetings). */
    public String getFullName() {
        String f = (firstName == null) ? "" : firstName;
        String l = (lastName == null) ? "" : lastName;
        return (f + " " + l).trim();
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    /**
     * Friendly summary shown in the "Success!" dialog after an insert.
     */
    @Override
    public String toString() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yyyy");
        String dobStr = (dob != null) ? sdf.format(dob) : "N/A";
        return "Welcome " + getFullName() + "!"
                + "\n\nFirst Name: " + firstName
                + "\nLast Name: " + lastName
                + "\nGender: " + gender
                + "\nDate of Birth: " + dobStr
                + "\nAge: " + age
                + "\nPhone: " + phone
                + "\nEmail: " + email
                + "\nCollege: " + college
                + "\nExperience: " + experience
                + "\nHobbies: " + hobby;
    }
}
