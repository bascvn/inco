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
        print("setSelected")
        self.mIcon.image?.imageWithRenderingMode(.AlwaysTemplate)
        mIcon.tintColor = UIColor(red: 141.0/255.0, green: 184.0/255.0, blue: 61.0/255.0, alpha: 1.0)
        

        // Configure the view for the selected state
    }

}
