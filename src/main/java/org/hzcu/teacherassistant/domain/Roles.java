package org.hzcu.teacherassistant.domain;

/**
 * 角色表
 */
public class Roles {
    /**
    * 角色ID
    */
    private Integer id;

    /**
    * 角色名称
    */
    private String roleName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}