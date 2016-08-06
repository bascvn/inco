//
//  TicketForm.swift
//  Inco
//
//  Created by admin on 8/6/16.
//  Copyright © 2016 Ban Sac. All rights reserved.
//

import Foundation

class TicketForm{
    var deparments = [BaseItem]()
    var ticketsStatus = [BaseItem]()
    var ticketsTypes = [BaseItem]()
    var ticketsTypesDefault:String?
    var ticketsStatusDefault:String?
    var users = [GroupUser]()
    var notify = [GroupUser]()
    init(data:NSDictionary){
        var depart = data.valueForKey("deparments") as! NSDictionary
        var keys = (depart.allKeys as! [String]).sort(<)
        for key in keys {
            deparments.append(BaseItem(key:key,value:depart.valueForKey(key) as! String))
        }
        depart = data.valueForKey("ticketsStatus") as! NSDictionary
        keys = (depart.allKeys as! [String]).sort(<)
        for key in keys {
            ticketsStatus.append(BaseItem(key:key,value:depart.valueForKey(key) as! String))
        }
        depart = data.valueForKey("ticketsTypes") as! NSDictionary
        keys = (depart.allKeys as! [String]).sort(<)
        for key in keys {
            ticketsTypes.append(BaseItem(key:key,value:depart.valueForKey(key) as! String))
        }
        ticketsTypesDefault = data.valueForKey("ticketsTypesDefault") as? String
        ticketsStatusDefault = data.valueForKey("ticketsStatusDefault") as? String
        
        depart = data.valueForKey("users") as! NSDictionary
        keys = (depart.allKeys as! [String]).sort(<)
        for key in keys {
            self.users.append(GroupUser(name: key,users: depart.valueForKey(key) as! NSDictionary))
        }
        depart = data.valueForKey("notify") as! NSDictionary
        keys = (depart.allKeys as! [String]).sort(<)
        for key in keys {
            self.notify.append(GroupUser(name: key,users: depart.valueForKey(key) as! NSDictionary))
        }

    }
}