//
//  TaskCell.swift
//  Inco
//
//  Created by admin on 7/24/16.
//  Copyright © 2016 Ban Sac. All rights reserved.
//

import Foundation
class TaskCell : BaseComponentCell{
        var id :String?
        var name :String?
        var projects :String?
        var tasks_priority:String?
        var assigned_to:String?
        var tasks_status :String?
        var projects_id :String?
        static  func createCell(data :NSDictionary ) -> TaskCell{
            let cell = TaskCell()
            cell.id = data.valueForKey("id") as? String
            cell.name = data.valueForKey("name") as? String
            cell.projects = data.valueForKey("projects") as? String
            cell.tasks_priority = data.valueForKey("tasks_priority") as? String
            cell.assigned_to = (data.valueForKey("assigned_to") as? String)
            cell.tasks_status = data.valueForKey("tasks_status") as? String
            cell.projects_id = data.valueForKey("projects_id") as? String
            return cell
        }
}