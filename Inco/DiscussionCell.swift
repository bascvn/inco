//
//  DiscussionCell.swift
//  Inco
//
//  Created by admin on 7/26/16.
//  Copyright © 2016 Ban Sac. All rights reserved.
//

import Foundation

class DiscussionCell: BaseComponentCell{
  //  var id :String?
    var name :String?
    var status :String?
    var projects:String?
    var create_by:String?
    var projects_id :String?
    static  func createCell(data :NSDictionary ) -> DiscussionCell{
        let cell = DiscussionCell()
        cell.id = data.valueForKey("id") as? String
        cell.name = data.valueForKey("name") as? String
        cell.status = data.valueForKey("status") as? String
        cell.projects = data.valueForKey("projects") as? String
        cell.create_by = (data.valueForKey("create_by") as? String)
        cell.projects_id = data.valueForKey("projects_id") as? String
        return cell
    }
    
    
}