package org.hzcu.teacherassistant.domain;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 学生作业表
 */
public class StudentAssignments {
    /**
    * 学生作业ID
    */
    private Integer id;

    /**
    * 作业ID
    */
    private Integer assignmentId;

    /**
    * 学生ID
    */
    private Integer studentId;

    /**
    * 提交时间
    */
    private Date submissionDate;

    /**
    * 作业成绩
    */
    private BigDecimal grade;

    /**
    * 提交作业文件URL
    */
    private String submissionFileUrl;

    /**
    * 作业提交描述
    */
    private String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(Integer assignmentId) {
        this.assignmentId = assignmentId;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Date getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }

    public BigDecimal getGrade() {
        return grade;
    }

    public void setGrade(BigDecimal grade) {
        this.grade = grade;
    }

    public String getSubmissionFileUrl() {
        return submissionFileUrl;
    }

    public void setSubmissionFileUrl(String submissionFileUrl) {
        this.submissionFileUrl = submissionFileUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}