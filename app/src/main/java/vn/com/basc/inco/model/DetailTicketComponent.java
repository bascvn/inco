package vn.com.basc.inco.model;

import java.util.List;

/**
 * Created by SONY on 6/13/2016.
 */
public class DetailTicketComponent extends DetailBaseComponent{
    private Detail detail;

    public Detail getDetail() {
        return detail;
    }

    public class Detail{
        String id;
        String department;
        User user;
        String status;
        String type;

        public String getId() {
            return id;
        }

        public String getDepartment() {
            return department;
        }

        public User getUser() {
            return user;
        }

        public String getStatus() {
            return status;
        }

        public String getType() {
            return type;
        }
    }

}
