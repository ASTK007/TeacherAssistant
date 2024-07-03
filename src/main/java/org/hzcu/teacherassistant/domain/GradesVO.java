package org.hzcu.teacherassistant.domain;

import java.math.BigDecimal;

public class GradesVO {
    private String name;
    private String CourseName;
    private BigDecimal Grade;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourseName() {
        return CourseName;
    }

    public void setCourseName(String courseName) {
        CourseName = courseName;
    }

    public BigDecimal getGrade() {
        return Grade;
    }

    public void setGrade(BigDecimal grade) {
        Grade = grade;
    }

    public GradesVO(String courseName, BigDecimal grade, String name) {
        CourseName = courseName;
        Grade = grade;
        this.name = name;
    }

    public GradesVO() {
    }

    @Override
    public String toString() {
        return "GradesVO{" +
                "姓名='" + name + '\'' +
                ", 课程名='" + CourseName + '\'' +
                ", 成绩=" + Grade +
                '}';
    }
}
