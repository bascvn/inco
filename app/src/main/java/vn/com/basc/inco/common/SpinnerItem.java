package vn.com.basc.inco.common;

/**
 * Created by SONY on 6/8/2016.
 */
public class SpinnerItem {
    String id;
    String value;
    public SpinnerItem(String id, String value){
        this.id = id;
        this.value = value;
    }
    public String getValue() {
        return value;
    }

    public String getId() {
        return id;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String toString(){
        return value;
    }
}
