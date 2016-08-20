//
//  NotificationsTableViewCell.swift
//  Inco
//
//  Created by admin on 8/20/16.
//  Copyright © 2016 Ban Sac. All rights reserved.
//

import UIKit

class NotificationsTableViewCell: UITableViewCell {

    @IBOutlet weak var attachFile: UIImageView!
    @IBOutlet weak var mParent: UILabel!
    @IBOutlet weak var mType: UIImageView!
    @IBOutlet weak var mContent: UILabel!
    @IBOutlet weak var mDate: UILabel!
    @IBOutlet weak var mName: UILabel!
    @IBOutlet weak var mAvatar: UIImageView!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
