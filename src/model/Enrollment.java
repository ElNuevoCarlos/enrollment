package model;

import java.time.LocalDate;

public class Enrollment {
    private String studentId;
    private String courseCode;
    private LocalDate enrollmentDate;

    public Enrollment(String studentId, String courseCode, LocalDate enrollmentDate) {
        this.studentId = studentId;
        this.courseCode = courseCode;
        this.enrollmentDate = enrollmentDate;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }
}
