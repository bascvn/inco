//
//  DetailTaskComponent.swift
//  Inco
//
//  Created by admin on 7/27/16.
//  Copyright © 2016 Ban Sac. All rights reserved.
//

import Foundation
class DetailTaskComponent: DetailBaseComponent {
  
    override init(data:NSDictionary) {
        let extend = data.valueForKey("detail") as? NSDictionary
        self.detail = DetailTaskComponent.Detail(data: extend!)
        super.init(data: data)
    }
    var  detail:DetailTaskComponent.Detail
    class Detail{
        var id:String?
        var label:String?
        var status:String?
        var priority:String?
        var type:String?
        var assigendTo = [User]()
        init(data:NSDictionary){
            self.id = data.valueForKey("id") as? String
            self.label = data.valueForKey("label") as? String
            self.status = data.valueForKey("status") as? String
            self.priority = data.valueForKey("priority") as? String
            self.type = data.valueForKey("type") as? String
            let users = data.valueForKey("assigendTo") as? [NSDictionary]
            for user in users! { // loop through data items
                let att = user as NSDictionary
                self.assigendTo.append(User(data: att))
            }

        }
    }
    
}