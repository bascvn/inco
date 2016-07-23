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
    static let TOKEN_PARAMETER = "token"
    static let OFFSET_PARAMETER = "offset"
    static let LIMIT_PARAMETER = "limit"
    static let SEARCH_PARAMETER = "search"
    
    // getlist project
    
    static func getLogin(company:String) -> String {
        return IncoApi.PROTOCOL+IncoApi.DOMAIN+"/"+company+IncoApi.LOGIN_API
    }
    static func getApi(api: String) -> String {
        return IncoApi.PROTOCOL+IncoApi.DOMAIN+"/"+IncoCommon.getClientID()+api
    }
}
