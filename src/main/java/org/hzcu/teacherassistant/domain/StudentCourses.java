package org.hzcu.teacherassistant.domain;

public class StudentCourses {
    private Integer studentId;
    private Integer courseId;

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public StudentCourses(Integer studentId, Integer courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
    }
}
