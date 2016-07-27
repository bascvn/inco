//
//  User.swift
//  Inco
//
//  Created by admin on 7/27/16.
//  Copyright © 2016 Ban Sac. All rights reserved.
//

import Foundation
class User {
    var id:String?
    var users_group_id:String?
    var name:String?
    var photo:String?
    var email:String?
    var phone:String?
    var users_group:String?
    init(data:NSDictionary){
        self.id = data.valueForKey("id") as? String
        self.users_group_id = data.valueForKey("users_group_id") as? String
        self.name = data.valueForKey("name") as? String
        self.photo = data.valueForKey("photo") as? String
        self.email = data.valueForKey("email") as? String
        self.phone = data.valueForKey("phone") as? String
        self.users_group = data.valueForKey("users_group") as? String
    }
  
    
}