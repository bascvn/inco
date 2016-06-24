package vn.com.basc.inco.model;

import java.util.List;

/**
 * Created by SONY on 6/12/2016.
 */
public class DetailBaseComponent {
    private String id;
    private String description;
    private String users;
    private String photo;
    private String create_at;
    List<Attachment> attachments;
    private String name;
    private String title;

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getUsers() {
        return users;
    }

    public String getPhoto() {
        return photo;
    }

    public String getCreate_at() {
        return create_at;
    }

    public List<Attachment> getAttachments() {

        return attachments;
    }

    public String getName() {
        return name;
    }
}
