//
//  UploadFileCell.swift
//  Inco
//
//  Created by admin on 7/31/16.
//  Copyright © 2016 Ban Sac. All rights reserved.
//

import UIKit

class UploadFileCell: UITableViewCell {

    @IBOutlet weak var mBtnDelete: UIButton!
    @IBOutlet weak var mProgess: UIProgressView!
    
    @IBOutlet weak var mFileName: UILabel!
    
    @IBOutlet weak var mInfo: UITextField!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
}
