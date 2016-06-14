package vn.com.basc.inco.model;

/**
 * Created by User on 23/05/2016.
 */
public class User {
    String id;
    String users_group_id;
    String name;
    String photo;
    String email;
    String phone;
    String users_group;

    public String getPhone() {
        return phone;
    }

    public String getUsers_group() {
        return users_group;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoto() {
        return photo;
    }

    public String getId() {
        return id;
    }
}
