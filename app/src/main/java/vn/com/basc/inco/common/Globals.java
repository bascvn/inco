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
    public static String TYPE_BIND_PARAMATER;
    public static String API_TASK_LIST;
    public static String PROJECT_ID_PARAMETER;
    public static String ID_PARAMETER;
    public static String TASK_ID_PARAMETER;
    public static String DISCUSSION_ID_PARAMETER;
    public static  String ID_EXTRA = "ID_EXTRA";
    public static  String MESS_EXTRA = "MESS_EXTRA";
    public static String COMPONENT_EXTRA = "COMPONENT_EXTRA";
    public static String PROJECT_ID_EXTRA ="PROJECT_ID_EXTRA";


    public static String API_TICKET_LIST;
    public static String API_DISCUSSION_LIST;
    public static String API_COMMENT_OF_TASK;
    public static String API_COMMENT_OF_DISCUSSIONS;
    public static String API_COMMENT_OF_PROJECT;
    public static  String API_COMMENT_OF_TICKET;

    public static  String API_ADD_COMMENT_OF_TASK;
    public static  String API_ADD_COMMENT_OF_TICKET;
    public static  String API_ADD_COMMENT_OF_DISCUSS;
    public static  String API_ADD_COMMENT_OF_PROJECT;

    public static  String API_UPLOAD_FILE ;
    public static  String API_DOWNLOAD_FILE;
    public static String API_GET_TICKET_FORM;
    public static String API_NEW_TICKET;

    public static String API_GET_PROJECT;
    public static String API_GET_TASK;
    public  static  String API_GET_TICKET;
    public  static  String API_GET_DISCUSSIONS;
    public static  String ADD_TASK_COMM_ID ;
    public static  String ADD_TASK_COMM_BY ;
    public static  String ADD_TASK_COMM_DES ;
    public static  String ADD_COMMENT_PRO_ID ;
    public static  String ADD_TASK_ID;
    public  static String API_LOGOUT;
    public static  String ADD_TICKET_COMM_ID;
    public static  String ADD_TICKET_COMM_BY ;
    public static  String ADD_TICKET_ID;
    public static  String ADD_TICKET_DES;
    public static String ADD_DISCUSS_COM_ID;
    public static String ADD_DISCUSS_COMM_BY ;
    public static String ADD_DISCUSS_ID ;
    public static String ADD_DISCUSS_DES ;


    public static String NEW_TICKET_COM_ID ;
    public static String NEW_TICKET_DEPA_ID ;
    public static String NEW_TICKET_TYPE_ID;
    public static String NEW_TICKET_STATUS_ID ;
    public static String NEW_TICKET_NAME ;
    public static String NEW_TICKET_DES;
    public static String NEW_TIECKT_USER;
    public static String EXTRA_NOTIFY;

    public static String ADD_PROJECT_COMM_ID ;
    public static String ADD_PROJECT_COMM_BY;
    public static String ADD_PROJECT_COMM_DES;
    public static int MAX_SIZE_UPLOAD = 5*1024*1024;
    public static String API_REFRESH_TOKEN;

    public static final String PUSH_NOTIFICATION = "vn.com.bsc.incotest.pushNotification";
    static {
        PRE_FIX_API ="";
        PROTOCOL = "http://www.kiemtraduan.net/";
        API_LOGOUT = PRE_FIX_API+"/index.php/mobile/logout";
        API_REFRESH_TOKEN = PRE_FIX_API+"/index.php/mobile/refreshtoken";
        API_LOGIN = PRE_FIX_API+"/mobile_api/gateway.php?controller=auth.login";
        API_PROJECT_LIST = PRE_FIX_API+"/mobile_api/gateway.php?controller=project.get_list";
        API_TASK_LIST = PRE_FIX_API+"/mobile_api/gateway.php?controller=task.get_list";
        API_TICKET_LIST = PRE_FIX_API+"/mobile_api/gateway.php?controller=tickets.get_list";
        API_DISCUSSION_LIST = PRE_FIX_API+"/mobile_api/gateway.php?controller=discussions.get_list";
        API_COMMENT_OF_TASK = PRE_FIX_API+"/mobile_api/gateway.php?controller=task.get_comments";
        API_COMMENT_OF_TICKET = PRE_FIX_API+"/mobile_api/gateway.php?controller=tickets.get_comments";
        API_COMMENT_OF_DISCUSSIONS = PRE_FIX_API+"/mobile_api/gateway.php?controller=discussions.get_comments";
        API_COMMENT_OF_PROJECT = PRE_FIX_API+"/index.php/mobile/projectscomments";
        API_ADD_COMMENT_OF_TASK =PRE_FIX_API+"/index.php/mobile/addcommenttask";
        API_ADD_COMMENT_OF_TICKET = PRE_FIX_API+"/index.php/mobile/addcommentticket";
        API_ADD_COMMENT_OF_DISCUSS = PRE_FIX_API+"/index.php/mobile/addcommentdiscussion";
        API_ADD_COMMENT_OF_PROJECT = PRE_FIX_API+"/index.php/mobile/addcommentproject";
        API_UPLOAD_FILE = PRE_FIX_API+"/index.php/mobile/upload";
        API_DOWNLOAD_FILE = PRE_FIX_API+"/index.php/mobile/download";
        API_GET_TICKET_FORM = PRE_FIX_API+"/index.php/mobile/ticketform";
        API_NEW_TICKET = PRE_FIX_API+"/index.php/mobile/newticket";
        API_GET_PROJECT = PRE_FIX_API+"/index.php/mobile/projects";
        API_GET_TASK = PRE_FIX_API+"/index.php/mobile/tasks";
        API_GET_TICKET = PRE_FIX_API+"/index.php/mobile/tickets";
        API_GET_DISCUSSIONS = PRE_FIX_API+"/index.php/mobile/discussions";
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
        ID_PARAMETER ="id";
        // add comment;

        ADD_PROJECT_COMM_ID = "projects_comments[projects_id]";
        ADD_PROJECT_COMM_BY = "projects_comments[created_by]";
        ADD_PROJECT_COMM_DES = "projects_comments[description]";

        ADD_TASK_COMM_ID = "tasks_comments[tasks_id]";
        ADD_TASK_COMM_BY ="tasks_comments[created_by]";
        ADD_TASK_COMM_DES ="tasks_comments[description]";
        ADD_COMMENT_PRO_ID = "projects_id";
        ADD_TASK_ID ="tasks_id";

        ADD_TICKET_COMM_ID ="tickets_comments[tickets_id]";
        ADD_TICKET_COMM_BY ="tickets_comments[users_id]";
        ADD_TICKET_ID ="tickets_id";
        ADD_TICKET_DES ="tickets_comments[description]";

        ADD_DISCUSS_COM_ID ="discussions_comments[discussions_id]";
        ADD_DISCUSS_COMM_BY ="discussions_comments[users_id]";
        ADD_DISCUSS_ID ="discussions_id";
        ADD_DISCUSS_DES = "discussions_comments[description]";

        NEW_TICKET_COM_ID = "tickets[projects_id]";
        NEW_TICKET_DEPA_ID = "tickets[departments_id]";
        NEW_TICKET_TYPE_ID = "tickets[tickets_types_id]";
        NEW_TICKET_STATUS_ID = "tickets[tickets_status_id]";
        NEW_TICKET_NAME = "tickets[name]";
        NEW_TICKET_DES = "tickets[description]";
        NEW_TIECKT_USER = "tickets[users_id]";
        EXTRA_NOTIFY ="extra_notification[]";
        TYPE_BIND_PARAMATER = "bind_type";


    }
}
