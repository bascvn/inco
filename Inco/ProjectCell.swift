//
//  ProjectCell.swift
//  Inco
//
//  Created by admin on 7/21/16.
//  Copyright © 2016 Ban Sac. All rights reserved.
//

import Foundation
class ProjectCell :BaseComponentCell{
  //  var id :String?
    var name :String?
    var createAt :String?
    var createBy :String?
    var status :String?
    var type :String?
    static  func createCell(data :NSDictionary ) -> ProjectCell{
        let cell = ProjectCell()
        cell.id = data.valueForKey("id") as? String
        cell.name = data.valueForKey("name") as? String
        cell.createAt = data.valueForKey("created_at") as? String
        cell.createBy = data.valueForKey("created_by") as? String
        cell.status = data.valueForKey("status") as? String
        cell.type = data.valueForKey("type") as? String
        return cell
    }
}