package vn.com.basc.inco.common;

/**
 * Created by User on 23/05/2016.
 */
public class INCOResponse {
    private static String STATUS_ERROR  = "error";
    public static String STATUS_TAG="status";
    public static String TOKEN_TAG ="token";
    public static String USER_TAG ="user";
    public static String DATA_TAG ="data";
    String status;
    String error_code;
    String error_mess;
    Object data;

    public String getStatus() {
        return status;
    }

    public Object getData() {
        return data;
    }

    public static boolean isError(String error){
        if( error.compareTo(STATUS_ERROR) == 0){
            return true;
        }
        return false;
    }

}
