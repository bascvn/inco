//
//  Company.swift
//  Inco
//
//  Created by admin on 8/1/16.
//  Copyright © 2016 Ban Sac. All rights reserved.
//

import Foundation
class Company {
    var ClientID:String?
    var ClientName:String?
    var ClientCode:String?
    var Logo:String?
    var Description:String?
    var DateCreated:String?
    var DateExpired:String?
    var BuildNumber:String?
    var Status:String?
    var url_ios:String?
    var url_android:String?
    var msg:String?
    var android_ver:String?
    init(data:NSDictionary){
        self.ClientID = data.valueForKey("ClientID") as? String
        self.ClientName = data.valueForKey("ClientName") as? String
        self.ClientCode = data.valueForKey("ClientCode") as? String
        self.Logo = data.valueForKey("Logo") as? String
        self.Description = data.valueForKey("Description") as? String
        self.DateCreated = data.valueForKey("DateCreated") as? String
        self.DateExpired = data.valueForKey("DateExpired") as? String
        self.BuildNumber = data.valueForKey("BuildNumber") as? String
        self.Status = data.valueForKey("Status") as? String
        self.url_ios = data.valueForKey("url_ios") as? String
        self.url_android = data.valueForKey("url_android") as? String
        self.msg = data.valueForKey("msg") as? String
        self.android_ver = data.valueForKey("android_ver") as? String



    }
}