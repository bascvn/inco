//
//  DetailProjectComponent.swift
//  Inco
//
//  Created by admin on 7/31/16.
//  Copyright © 2016 Ban Sac. All rights reserved.
//

import Foundation

class DetailProjectComponent: DetailBaseComponent {
    override init(data:NSDictionary) {
        let extend = data.valueForKey("detail") as? NSDictionary
        self.detail = DetailProjectComponent.Detail(data: extend!)
        super.init(data: data)
    }
    var  detail:DetailProjectComponent.Detail
    class Detail{
        
        var id:String?
        var status:String?
        var type:String?
        var team = [User]()
        init(data:NSDictionary){
            self.id = data.valueForKey("id") as? String
            self.status = data.valueForKey("status") as? String
            self.type = data.valueForKey("type") as? String
            let users = data.valueForKey("team") as? [NSDictionary]
            for user in users! { // loop through data items
                let att = user as NSDictionary
                self.team.append(User(data: att))
            }
            
        }
    }
}