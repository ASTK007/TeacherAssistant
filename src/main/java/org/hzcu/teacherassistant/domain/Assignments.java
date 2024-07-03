package org.hzcu.teacherassistant.domain;

import java.util.Date;

/**
 * 作业表
 */
public class Assignments {
    /**
    * 作业ID
    */
    private Integer id;

    /**
    * 课程ID
    */
    private Integer courseId;

    /**
    * 作业标题
    */
    private String title;

    /**
    * 作业描述
    */
    private String description;

    /**
    * 截止日期
    */
    private Date dueDate;

    /**
    * 作业示例材料文件URL
    */
    private String sampleFileUrl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getSampleFileUrl() {
        return sampleFileUrl;
    }

    public void setSampleFileUrl(String sampleFileUrl) {
        this.sampleFileUrl = sampleFileUrl;
    }
}