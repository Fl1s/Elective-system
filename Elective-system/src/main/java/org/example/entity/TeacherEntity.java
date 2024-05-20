package org.example.entity;

public class TeacherEntity {
    private int teacherId;
    private String teacherName;

    public TeacherEntity(String teacherName, int teacherId) {
        this.teacherName = teacherName;
        this.teacherId = teacherId;
    }
    public TeacherEntity() {

    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    @Override
    public String toString() {
        return "TeacherEntity{" +
                "teacherId=" + teacherId +
                ", teacherName='" + teacherName + '\'' +
                '}';
    }
}
