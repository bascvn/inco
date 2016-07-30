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
    static let LOGIN_API        = "/mobile_api/gateway.php?controller=auth.login"
    static let LOGIN_EMAIL      = "email"
    static let LOGIN_PASS       = "password"
    static let LOGIN_DEVICE_ID  = "device_id"
    static let LOGIN_DEVICE_TYPE    =  "device_type"
    static let API_PROJECT_LIST = "/mobile_api/gateway.php?controller=project.get_list"
    static let API_TASK_LIST =  "/mobile_api/gateway.php?controller=task.get_list"
    static let API_TICKET_LIST = "/mobile_api/gateway.php?controller=tickets.get_list"
    static let API_DISCUSSION_LIST = "/mobile_api/gateway.php?controller=discussions.get_list"
    static let API_GET_TASK = "/index.php/mobile/tasks"
    
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
    
    // getlist project
    
    static func getLogin(company:String) -> String {
        return IncoApi.PROTOCOL+IncoApi.DOMAIN+"/"+company+IncoApi.LOGIN_API
    }
    static func getApi(api: String) -> String {
        return IncoApi.PROTOCOL+IncoApi.DOMAIN+"/"+IncoCommon.getClientID()+api
    }
    static func getAvatar(avatar:String) -> String  {
        return IncoApi.PROTOCOL+IncoApi.DOMAIN+"/"+IncoCommon.getClientID()+AVATAR_PATH+"/"+avatar
    }
}
