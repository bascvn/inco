//
//  LeftMenuItemTableViewCell.swift
//  Inco
//
//  Created by admin on 7/18/16.
//  Copyright © 2016 Ban Sac. All rights reserved.
//

import UIKit

class LeftMenuItemTableViewCell: UITableViewCell {

    
    @IBOutlet weak var mIcon: UIImageView!
    @IBOutlet weak var subject: UILabel!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
