package org.hzcu.teacherassistant.domain;

import java.util.Date;

/**
 * 考勤表
 */
public class Attendance {
    /**
    * 考勤ID
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
    * 考勤日期
    */
    private Date attendanceDate;

    /**
    * 考勤状态（出席、缺席、迟到、请假）
    */
    private String status;

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

    public Date getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(Date attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}