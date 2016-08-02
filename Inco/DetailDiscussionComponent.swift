//
//  DetailDiscussionComponent.swift
//  Inco
//
//  Created by admin on 7/31/16.
//  Copyright © 2016 Ban Sac. All rights reserved.
//

import Foundation

class DetailDiscussionComponent: DetailBaseComponent {
    override init(data:NSDictionary) {
        let extend = data.valueForKey("detail") as? NSDictionary
        self.detail = DetailDiscussionComponent.Detail(data: extend!)
        super.init(data: data)
    }
    var  detail:DetailDiscussionComponent.Detail
    class Detail{
        var id:String?
        var status:String?
        var assigendTo = [User]()
        init(data:NSDictionary){
            self.id = data.valueForKey("id") as? String
            self.status = data.valueForKey("status") as? String
            let users = data.valueForKey("assigendTo") as? [NSDictionary]
            for user in users! { // loop through data items
                let att = user as NSDictionary
                self.assigendTo.append(User(data: att))
            }
            
        }
    }
}