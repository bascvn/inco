package vn.com.basc.inco.model;

import java.util.List;

/**
 * Created by SONY on 6/13/2016.
 */
public class DetailTaskComponent extends DetailBaseComponent{
    private Detail detail;

    public Detail getDetail() {
        return detail;
    }

    public class Detail{
        String id;
        String label;
        String status;
        String priority;
        String type;
        List<User> assigendTo;

        public String getId() {
            return id;
        }

        public String getLabel() {
            return label;
        }

        public String getStatus() {
            return status;
        }

        public String getPriority() {
            return priority;
        }

        public String getType() {
            return type;
        }

        public List<User> getAssigendTo() {
            return assigendTo;
        }
    }

}
