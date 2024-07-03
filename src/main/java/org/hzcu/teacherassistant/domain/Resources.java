package org.hzcu.teacherassistant.domain;

public class Resources {
    private Integer id;
    private Integer courseId;
    private String resourceUrl;

    public Resources(Integer id, Integer courseId, String resourceUrl) {
        this.id = id;
        this.courseId = courseId;
        this.resourceUrl = resourceUrl;
    }

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

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }
}
