//
//  Attachment.swift
//  Inco
//
//  Created by admin on 7/27/16.
//  Copyright © 2016 Ban Sac. All rights reserved.
//

import Foundation
class Attachment {
    var id:String?
    var file: String?
    var info:String?
    static func create(data:NSDictionary) -> Attachment {
        let attachment = Attachment()
        attachment.id = data.valueForKey("id") as? String
        attachment.file = data.valueForKey("file") as? String
        attachment.info = data.valueForKey("info") as? String
        return attachment
        
    }
}