package org.example.entity;

import java.sql.Date;

public class ArchiveEntity {
    private int archiveId;
    private int enrollmentId;
    private double grade;
    private Date archiveDate;

    public ArchiveEntity(int archiveId, int enrollmentId, double grade, Date archiveDate) {
        this.archiveId = archiveId;
        this.enrollmentId = enrollmentId;
        this.grade = grade;
        this.archiveDate = archiveDate;
    }

    public ArchiveEntity() {
    }


    public int getArchiveId() {
        return archiveId;
    }

    public void setArchiveId(int archiveId) {
        this.archiveId = archiveId;
    }

    public int getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(int enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public Date getArchiveDate() {
        return archiveDate;
    }

    public void setArchiveDate(Date archive_date) {
        this.archiveDate = archive_date;
    }

    @Override
    public String toString() {
        return "ArchiveEntity{" +
                "archiveId=" + archiveId +
                ", enrollmentId=" + enrollmentId +
                ", grade=" + grade +
                ", archiveDate=" + archiveDate +
                '}';
    }
}
