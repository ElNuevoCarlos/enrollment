package model;

public class Course {
    private String code;
    private String name;
    private int credits;

    public Course(String code, String name, int credits) {
        this.code = code;
        this.name = name;
        this.credits = credits;
    }

    // Getters
    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getCredits() {
        return credits;
    }

    // Setters
    public void setCode(String code) {
        this.code = code;
    }

    public void setTitle(String name) {
        this.name = name;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

}
