//
//  TicketCell.swift
//  Inco
//
//  Created by admin on 7/26/16.
//  Copyright © 2016 Ban Sac. All rights reserved.
//

import Foundation

class TicketCell: BaseComponentCell{
    var id :String?
    var name :String?
    var departments :String?
    var tickets_status:String?
    var create_by:String?
    var projects_id :String?
    var project :String?
    static  func createCell(data :NSDictionary ) -> TicketCell{
        let cell = TicketCell()
        cell.id = data.valueForKey("id") as? String
        cell.name = data.valueForKey("name") as? String
        cell.departments = data.valueForKey("departments") as? String
        cell.tickets_status = data.valueForKey("tickets_status") as? String
        cell.create_by = (data.valueForKey("create_by") as? String)
        cell.project = data.valueForKey("project") as? String
        cell.projects_id = data.valueForKey("projects_id") as? String
        return cell
    }
}