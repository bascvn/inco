package vn.com.basc.inco.common;

/**
 * Created by User on 22/05/2016.
 */
public class Globals {
    public static String PRE_FIX_API;
    public static String PROTOCOL;
    public static String API_LOGIN;
    public static String LOGIN_EMAIL;
    public static String LOGIN_PASS;
    public static String LOGIN_DEVICE_ID;
    public static String LOGIN_DEVICE_TYPE;
    public static String LOGIN_COMPANY;
    public static String ANDROID_DEVICE = "1";
    public static String AVATAR_PATH ;
    public static String API_PROJECT_LIST;
    public static String TOKEN_PARAMETER;
    public static String OFFSET_PARAMETER;
    public static String LIMIT_PARAMETER;
    public static String SEARCH_PARAMETER;
    public static String TICKET_ID_PARAMETER;

    public static String API_TASK_LIST;
    public static String PROJECT_ID_PARAMETER;
    public static String TASK_ID_PARAMETER;
    public static String DISCUSSION_ID_PARAMETER;
    public static  String ID_EXTRA = "ID_EXTRA";
    public static  String MESS_EXTRA = "MESS_EXTRA";
    public static String COMPONENT_EXTRA = "COMPONENT_EXTRA";


    public static String API_TICKET_LIST;
    public static String API_DISCUSSION_LIST;
    public static String API_COMMENT_OF_TASK;
    public static String API_COMMENT_OF_DISCUSSIONS;

    public static  String API_COMMENT_OF_TICKET;

    static {
        PRE_FIX_API ="incodemo";
        PROTOCOL = "http://";
        API_LOGIN = PRE_FIX_API+"/mobile_api/gateway.php?controller=auth.login";
        API_PROJECT_LIST = PRE_FIX_API+"/mobile_api/gateway.php?controller=project.get_list";
        API_TASK_LIST = PRE_FIX_API+"/mobile_api/gateway.php?controller=task.get_list";
        API_TICKET_LIST = PRE_FIX_API+"/mobile_api/gateway.php?controller=tickets.get_list";
        API_DISCUSSION_LIST = PRE_FIX_API+"/mobile_api/gateway.php?controller=discussions.get_list";
        API_COMMENT_OF_TASK = PRE_FIX_API+"/mobile_api/gateway.php?controller=task.get_comments";
        API_COMMENT_OF_TICKET = PRE_FIX_API+"/mobile_api/gateway.php?controller=tickets.get_comments";
        API_COMMENT_OF_DISCUSSIONS = PRE_FIX_API+"/mobile_api/gateway.php?controller=discussions.get_comments";
        LOGIN_EMAIL = "email";
        LOGIN_PASS = "password";
        LOGIN_DEVICE_ID = "device_id";
        LOGIN_DEVICE_TYPE =  "device_type";
        DISCUSSION_ID_PARAMETER ="discussion_id";
        TOKEN_PARAMETER = "token";
        OFFSET_PARAMETER = "offset";
        LIMIT_PARAMETER = "limit";
        SEARCH_PARAMETER ="search";
        PROJECT_ID_PARAMETER ="projects_id";
        TICKET_ID_PARAMETER = "ticket_id";
        TASK_ID_PARAMETER ="task_id";
        AVATAR_PATH =PRE_FIX_API+"/uploads/users";
    }
}
