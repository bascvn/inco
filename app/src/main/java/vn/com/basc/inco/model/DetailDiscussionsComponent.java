package vn.com.basc.inco.model;

import java.util.List;

/**
 * Created by SONY on 6/13/2016.
 */
public class DetailDiscussionsComponent extends DetailBaseComponent{
    private Detail detail;

    public Detail getDetail() {
        return detail;
    }

    public class Detail{
        String id;
        String status;
        List<User> assigendTo;

        public String getId() {
            return id;
        }

        public List<User> getAssigendTo() {
            return assigendTo;
        }

        public String getStatus() {
            return status;
        }
    }

}
