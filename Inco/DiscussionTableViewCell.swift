//
//  DiscussionTableViewCell.swift
//  Inco
//
//  Created by admin on 7/26/16.
//  Copyright © 2016 Ban Sac. All rights reserved.
//

import UIKit

class DiscussionTableViewCell: UITableViewCell {

    @IBOutlet weak var mDiscussionName: UILabel!
    
    @IBOutlet weak var mCreateBy: UILabel!
    @IBOutlet weak var mStatusName: UILabel!
    @IBOutlet weak var mProjectName: UILabel!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
}
