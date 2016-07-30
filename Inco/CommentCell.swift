//
//  CommentCell.swift
//  Inco
//
//  Created by admin on 7/30/16.
//  Copyright © 2016 Ban Sac. All rights reserved.
//

import Foundation
class CommentCell {
    var id:String?
    var name:String?
    var created_at:String?
    var  avatar:String?
    var  description:String?
    var attachments = [Attachment]()
    init(data:NSDictionary){
        self.id = data.valueForKey("id") as? String
        self.name = data.valueForKey("name") as? String
        self.created_at = data.valueForKey("created_at") as? String
        self.avatar = data.valueForKey("avatar") as? String
        self.description = data.valueForKey("description") as? String
        let files = data.valueForKey("attachments") as? [NSDictionary]
        if files != nil {
            for file in files! { // loop through data items
                let att = file as NSDictionary
                self.attachments.append(Attachment.create(att))
            }
        }
    }
}