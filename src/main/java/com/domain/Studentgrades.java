package com.domain;

import java.math.BigDecimal;

/**
 * 学生成绩表
 */
public class Studentgrades {
    /**
    * 学生成绩ID
    */
    private Integer id;

    /**
    * 学生ID
    */
    private Integer studentId;

    /**
    * 课程ID
    */
    private Integer courseId;

    /**
    * 成绩
    */
    private BigDecimal grade;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public BigDecimal getGrade() {
        return grade;
    }

    public void setGrade(BigDecimal grade) {
        this.grade = grade;
    }
}