package org.hzcu.teacherassistant.domain;

/**
 * 权限表
 */
public class Permissions {
    /**
    * 权限ID
    */
    private Integer id;

    /**
    * 权限名称
    */
    private String permissionName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }
}