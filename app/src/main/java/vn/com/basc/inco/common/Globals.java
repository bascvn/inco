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

    public static String API_TASK_LIST;
    public static String PROJECT_ID_PARAMETER;
    public static String TASK_ID_PARAMETER;
    public static  String ID_EXTRA = "ID_EXTRA";
    public static  String MESS_EXTRA = "MESS_EXTRA";


    public static String API_TICKET_LIST;
    public static String API_DISCUSSION_LIST;
    public static String API_COMMENT_OF_TASK;
    static {
        PROTOCOL = "http://";
        API_LOGIN = "inco/mobile_api/gateway.php?controller=auth.login";
        API_PROJECT_LIST = "inco/mobile_api/gateway.php?controller=project.get_list";
        API_TASK_LIST = "inco/mobile_api/gateway.php?controller=task.get_list";
        API_TICKET_LIST = "inco/mobile_api/gateway.php?controller=tickets.get_list";
        API_DISCUSSION_LIST = "inco/mobile_api/gateway.php?controller=discussions.get_list";
        API_COMMENT_OF_TASK = "inco/mobile_api/gateway.php?controller=task.get_comments";
        LOGIN_EMAIL = "email";
        LOGIN_PASS = "password";
        LOGIN_DEVICE_ID = "device_id";
        LOGIN_DEVICE_TYPE =  "device_type";
        TOKEN_PARAMETER = "token";
        OFFSET_PARAMETER = "offset";
        LIMIT_PARAMETER = "limit";
        SEARCH_PARAMETER ="search";
        PROJECT_ID_PARAMETER ="projects_id";
        TASK_ID_PARAMETER ="task_id";
    }
}
