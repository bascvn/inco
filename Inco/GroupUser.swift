//
//  GroupUser.swift
//  Inco
//
//  Created by admin on 8/6/16.
//  Copyright © 2016 Ban Sac. All rights reserved.
//

import Foundation
class GroupUser {
    var name:String?
    var users = [BaseItem]()
    init(name:String,users:NSDictionary){
        self.name = name
        let keys = (users.allKeys as! [String]).sort(<)
        for key in keys {
            self.users.append(BaseItem(key:key,value:users.valueForKey(key) as! String))
        }
    
    }
}