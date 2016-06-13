package vn.com.basc.inco.model;

import java.util.List;

/**
 * Created by SONY on 6/13/2016.
 */
public class DetailProjectComponent extends DetailBaseComponent{
    private Detail detail;

    public Detail getDetail() {
        return detail;
    }

    public class Detail{
        String id;
        String type;
        String status;
        List<User> team;

        public String getStatus() {
            return status;
        }

        public String getId() {
            return id;
        }

        public String getType() {
            return type;
        }

        public List<User> getTeam() {
            return team;
        }
    }

}
