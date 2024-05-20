package org.example.entity;

public class StudentEntity {
    private int studentId;
    private String studentName;
    private CourseEntity studentCourse;
    private double studentGrade;

    public StudentEntity(int studentId, String studentName, String studentCourse, double studentGrade) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentCourse = studentCourse;
        this.studentGrade = studentGrade;
    }
    public StudentEntity(){

    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentCourse() {
        return studentCourse;
    }

    public void setStudentCourse(CourseEntity studentCourse) {
        this.studentCourse = studentCourse;
    }

    public float getStudentGrade() {
        return studentGrade;
    }

    public void setStudentGrade(double studentGrade) {
        this.studentGrade = studentGrade;
    }

    @Override
    public String toString() {
        return "StudentEntity{" +
                "studentId=" + studentId +
                ", studentName='" + studentName + '\'' +
                ", studentCourse=" + studentCourse +
                ", studentGrade=" + studentGrade +
                '}';
    }
}
