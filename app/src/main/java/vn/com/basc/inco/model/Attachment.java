package vn.com.basc.inco.model;

import java.io.Serializable;

/**
 * Created by User on 27/05/2016.
 */
public class Attachment implements Serializable {
    String id;
    String file;
    String info;

    public String getId() {
        return id;
    }

    public String getFile() {
        return file;
    }

    public String getInfo() {
        return info;
    }
}
