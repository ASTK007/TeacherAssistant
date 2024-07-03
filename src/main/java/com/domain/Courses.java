package com.domain;

/**
 * 课程表
 */
public class Courses {
    /**
    * 课程ID
    */
    private Integer id;

    /**
    * 课程名称
    */
    private String name;

    /**
    * 课程描述
    */
    private String description;

    /**
    * 学分
    */
    private Integer credits;

    /**
    * 学时
    */
    private Integer hours;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public Integer getHours() {
        return hours;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }
}