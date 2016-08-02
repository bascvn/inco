//
//  DetailTicketComponent.swift
//  Inco
//
//  Created by admin on 7/31/16.
//  Copyright © 2016 Ban Sac. All rights reserved.
//

import Foundation

class DetailTicketComponent: DetailBaseComponent {
    override init(data:NSDictionary) {
        let extend = data.valueForKey("detail") as? NSDictionary
        self.detail = DetailTicketComponent.Detail(data: extend!)
        super.init(data: data)
    }
    var  detail:DetailTicketComponent.Detail
    class Detail{
        var id:String?
        var department:String?
        var status:String?
        var type:String?
        var user:User?
        init(data:NSDictionary){
            self.id = data.valueForKey("id") as? String
            self.department = data.valueForKey("department") as? String
            self.status = data.valueForKey("status") as? String
            self.type = data.valueForKey("type") as? String
            let user = data.valueForKey("user") as? NSDictionary
            self.user = User(data: user!)
            
        }
    }
}