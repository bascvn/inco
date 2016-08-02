//
//  BaseComponentCell.swift
//  Inco
//
//  Created by admin on 7/26/16.
//  Copyright © 2016 Ban Sac. All rights reserved.
//

import Foundation

enum ComponentType {
    case PROJECT, TASKS, TICKET , DISCUSSTION
}
class BindTypeUpload{
    static let TASK_COMMENT = "comments"
    static let TICKET_COMMENT = "ticketsComments"
    static let DISCUSSION_COMMENT = "discussionsComments"
    static let PROJECT_COMMENT = "projectsComments"
    static let NEW_TICKET_FILE = "tickets"
}
class BaseComponentCell{
    var id:String?
    var name :String?
}