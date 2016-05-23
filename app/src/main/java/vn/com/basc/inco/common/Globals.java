package vn.com.basc.inco.common;

/**
 * Created by User on 22/05/2016.
 */
public class Globals {
    public static String PROTOCOL;
    public static String API_LOGIN;
    public static String LOGIN_EMAIL;
    public static String LOGIN_PASS;
    public static String LOGIN_DEVICE_ID;
    public static String LOGIN_DEVICE_TYPE;
    public static String LOGIN_COMPANY;
    public static String ANDROID_DEVICE = "1";
    public static String AVATAR_PATH ="inco/uploads/users";

    static {
        PROTOCOL = "http://";
        API_LOGIN = "inco/mobile_api/gateway.php?controller=auth.login";
        LOGIN_EMAIL = "email";
        LOGIN_PASS = "password";
        LOGIN_DEVICE_ID = "device_id";
        LOGIN_DEVICE_TYPE =  "device_type";
    }
}
