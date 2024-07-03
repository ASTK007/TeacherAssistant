package org.hzcu.teacherassistant.domain;

/**
 * 用户表
 */
public class Users{
    /**
    * 主键
    */
    private Integer id;

    /**
    * 用户名(学号、工号）
    */
    private String username;

    /**
    * 密码
    */
    private String password;

    /**
    * 邮箱
    */
    private String email;

    /**
    * 姓名
    */
    private String name;

    /**
    * 性别
    */
    private String gender;

    /**
    * 角色（Student, Teacher, Admin）
    */

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

}