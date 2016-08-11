//
//  IncoApi.swift
//  Inco
//
//  Created by admin on 7/19/16.
//  Copyright © 2016 Ban Sac. All rights reserved.
//

import Foundation
class IncoApi {
    static let PROTOCOL: String = "http://"
    static let DOMAIN:String    = "kiemtraduan.net"
    static let API_GET_COMPANY = "gateway.php?controller=client.get_clients"
    static let API_GET_COMPANY_STATUS = "gateway.php?controller=client.get_client_staus"
    static let API_LOGOUT = "/index.php/mobile/logout"

    static let LOGIN_API        = "/mobile_api/gateway.php?controller=auth.login"
    static let LOGIN_EMAIL      = "email"
    static let LOGIN_PASS       = "password"
    static let LOGIN_DEVICE_ID  = "device_id"
    static let LOGIN_DEVICE_TYPE    =  "device_type"
    static let API_PROJECT_LIST = "/mobile_api/gateway.php?controller=project.get_list"
    static let API_TASK_LIST =  "/mobile_api/gateway.php?controller=task.get_list"
    static let API_TICKET_LIST = "/mobile_api/gateway.php?controller=tickets.get_list"
    static let API_DISCUSSION_LIST = "/mobile_api/gateway.php?controller=discussions.get_list"
    
    static let  API_UPLOAD_FILE = "/index.php/mobile/upload"
    
    static let API_GET_TASK = "/index.php/mobile/tasks"
    static let API_GET_PROJECT = "/index.php/mobile/projects"
    static let API_GET_TICKET = "/index.php/mobile/tickets"
    static let API_GET_DISCUSSIONS = "/index.php/mobile/discussions"
    
    static let ID_PARAMETER = "id"
    static let PROJECT_ID_PARAMETER = "projects_id"
    static let TOKEN_PARAMETER = "token"
    static let OFFSET_PARAMETER = "offset"
    static let LIMIT_PARAMETER = "limit"
    static let SEARCH_PARAMETER = "search"
    static let AVATAR_PATH = "/uploads/users"
    
    // comments
    static let API_COMMENT_OF_TASK = "/mobile_api/gateway.php?controller=task.get_comments"
    static let API_COMMENT_OF_TICKET = "/mobile_api/gateway.php?controller=tickets.get_comments"
    static let API_COMMENT_OF_DISCUSSIONS = "/mobile_api/gateway.php?controller=discussions.get_comments"
    static let API_COMMENT_OF_PROJECT = "/index.php/mobile/projectscomments"
    static let TASK_ID_PARAMETER = "task_id"
    static let TICKET_ID_PARAMETER = "ticket_id"
    static let DISCUSSION_ID_PARAMETER = "discussion_id"
    
    
    static let  TYPE_BIND_PARAMATER = "bind_type"
    // Ticket 
    static let API_GET_TICKET_FORM = "/index.php/mobile/ticketform"
    static let API_NEW_TICKET = "/index.php/mobile/newticket"
    static let NEW_TICKET_COM_ID = "tickets[projects_id]"
    static let NEW_TICKET_DEPA_ID = "tickets[departments_id]"
    static let NEW_TICKET_TYPE_ID = "tickets[tickets_types_id]"
    static let NEW_TICKET_STATUS_ID = "tickets[tickets_status_id]"
    static let NEW_TICKET_NAME = "tickets[name]"
    static let NEW_TICKET_DES = "tickets[description]"
    static let NEW_TIECKT_USER = "tickets[users_id]"
    static let EXTRA_NOTIFY = "extra_notification[]"

    // download
    static let  API_DOWNLOAD_FILE = "/index.php/mobile/download"

    // add comments
    static let API_ADD_COMMENT_OF_TASK = "/index.php/mobile/addcommenttask"
    static let API_ADD_COMMENT_OF_TICKET = "/index.php/mobile/addcommentticket"
    static let API_ADD_COMMENT_OF_DISCUSS = "/index.php/mobile/addcommentdiscussion"
    static let API_ADD_COMMENT_OF_PROJECT = "/index.php/mobile/addcommentproject"
    
    static let ADD_PROJECT_COMM_ID = "projects_comments[projects_id]"
    static let ADD_PROJECT_COMM_BY = "projects_comments[created_by]"
    static let ADD_PROJECT_COMM_DES = "projects_comments[description]"
    
    static let ADD_TASK_COMM_ID = "tasks_comments[tasks_id]"
    static let ADD_TASK_COMM_BY = "tasks_comments[created_by]"
    static let ADD_TASK_COMM_DES = "tasks_comments[description]"
    static let ADD_COMMENT_PRO_ID = "projects_id"
    static let ADD_TASK_ID = "tasks_id"
    static let CLIENT_CODE = "ClientCode"
    static let ADD_TICKET_COMM_ID = "tickets_comments[tickets_id]"
    static let ADD_TICKET_COMM_BY = "tickets_comments[users_id]"
    static let ADD_TICKET_ID = "tickets_id"
    static let ADD_TICKET_DES = "tickets_comments[description]"
    
    static let ADD_DISCUSS_COM_ID = "discussions_comments[discussions_id]"
    static let ADD_DISCUSS_COMM_BY = "discussions_comments[users_id]"
    static let ADD_DISCUSS_ID = "discussions_id"
    static let ADD_DISCUSS_DES = "discussions_comments[description]"
    // getlist project
    
    static let SEARCH_WORD = "SearchWord"

    static func getLogin(company:String) -> String {
        return IncoApi.PROTOCOL + company + "." + IncoApi.DOMAIN+"/"+company+IncoApi.LOGIN_API
    }
    static func getApi(api: String) -> String {
        return IncoApi.PROTOCOL+IncoCommon.getClientID()+"."+IncoApi.DOMAIN+"/"+IncoCommon.getClientID()+api
    }
    static func getAvatar(avatar:String) -> String  {
        return IncoApi.PROTOCOL+IncoCommon.getClientID()+"."+IncoApi.DOMAIN+"/"+IncoCommon.getClientID()+AVATAR_PATH+"/"+avatar
    }
    static func getCompanies() -> String {
        return IncoApi.PROTOCOL+"wwww."+IncoApi.DOMAIN+"/"+IncoApi.API_GET_COMPANY
    }
    static func getStatusCompany() -> String{
        return IncoApi.PROTOCOL+"wwww."+IncoApi.DOMAIN+"/"+IncoApi.API_GET_COMPANY_STATUS

    }
    static func getLogoCompany(logo:String) ->String {
        return IncoApi.PROTOCOL+IncoApi.DOMAIN+"/logo/"+logo

    }
}
