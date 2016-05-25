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
    public static String API_PROJECT_LIST;
    public static String TOKEN_PARAMETER;
    public static String OFFSET_PARAMETER;
    public static String LIMIT_PARAMETER;
    public static String SEARCH_PARAMETER;

    static {
        PROTOCOL = "http://";
        API_LOGIN = "inco/mobile_api/gateway.php?controller=auth.login";
        API_PROJECT_LIST = "inco/mobile_api/gateway.php?controller=project.get_list";
        LOGIN_EMAIL = "email";
        LOGIN_PASS = "password";
        LOGIN_DEVICE_ID = "device_id";
        LOGIN_DEVICE_TYPE =  "device_type";
        TOKEN_PARAMETER = "token";
        OFFSET_PARAMETER = "offset";
        LIMIT_PARAMETER = "limit";
        SEARCH_PARAMETER ="search";
    }
}
