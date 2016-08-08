//
//  CommentTableViewCell.swift
//  Inco
//
//  Created by admin on 7/30/16.
//  Copyright © 2016 Ban Sac. All rights reserved.
//

import UIKit

class CommentTableViewCell: UITableViewCell {

    @IBOutlet weak var mFileAttch: UIImageView!
    @IBOutlet weak var mAvatar: UIImageView!
    
    @IBOutlet weak var mDiscription: UILabel!
    @IBOutlet weak var mCreateAt: UILabel!
    @IBOutlet weak var mName: UILabel!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
}
