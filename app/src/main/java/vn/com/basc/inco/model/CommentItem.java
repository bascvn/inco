package vn.com.basc.inco.model;

import java.util.List;

/**
 * Created by User on 27/05/2016.
 */
public class CommentItem extends  Item{
    public String id;
    public String name;
    public String created_at;
    public String avatar;
    public String description;
    public List<Attachment> attachments;
}
