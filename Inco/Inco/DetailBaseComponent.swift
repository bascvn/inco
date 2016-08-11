//
//  DetailBaseComponent.swift
//  Inco
//
//  Created by admin on 7/27/16.
//  Copyright © 2016 Ban Sac. All rights reserved.
//

import Foundation
class DetailBaseComponent {
    var id:String?
    var description: String?
    var users: String?
    var photo: String?
    var create_at: String?
    var attachments = [Attachment]()
    var name: String?
    var title: String?
    init(data:NSDictionary){
        self.id = data.valueForKey("id") as? String
        self.description = data.valueForKey("description") as? String
        self.users = data.valueForKey("users") as? String
        self.photo = data.valueForKey("photo") as? String
        self.create_at = data.valueForKey("create_at") as? String
        self.name = data.valueForKey("name") as? String
        self.title = data.valueForKey("title") as? String
        let files = data.valueForKey("data") as? [NSDictionary]
        if files != nil {
            for file in files! { // loop through data items
                let att = file as NSDictionary
                self.attachments.append(Attachment.create(att))
            }
        }

    }
    
}