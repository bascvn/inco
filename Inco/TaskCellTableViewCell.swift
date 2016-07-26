//
//  TaskCellTableViewCell.swift
//  Inco
//
//  Created by admin on 7/24/16.
//  Copyright © 2016 Ban Sac. All rights reserved.
//

import UIKit

class TaskCellTableViewCell: UITableViewCell {

    @IBOutlet weak var mTaskName: UILabel!
    
    @IBOutlet weak var mProjectName: UILabel!
    
    @IBOutlet weak var mStatus: UILabel!
    
    
    @IBOutlet weak var mPriority: UILabel!
    
    
    @IBOutlet weak var mAssignTo: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
}
