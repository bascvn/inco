package vn.com.basc.inco.gcm;

import java.io.Serializable;

/**
 * Created by SONY on 6/19/2016.
 */
public class Push implements Serializable{
    private String title;
    private String photo;
    private String message;
    private String component;
    private String project;
    private String id;
    private String date;
    private String parent;
    private String attach;
    private long idDB;

    public String getParent() {
        return parent;
    }

    public String getAttach() {
        return attach;
    }

    public long getIdDB() {
        return idDB;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public void setIdDB(long idDB) {
        this.idDB = idDB;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getPhoto() {
        return photo;
    }

    public String getMessage() {
        return message;
    }

    public String getComponent() {
        return component;
    }

    public String getProject() {
        return project;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }
}
