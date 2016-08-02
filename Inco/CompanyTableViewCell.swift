//
//  CompanyTableViewCell.swift
//  Inco
//
//  Created by admin on 8/1/16.
//  Copyright © 2016 Ban Sac. All rights reserved.
//

import UIKit

class CompanyTableViewCell: UITableViewCell {

    @IBOutlet weak var mDiscription: UILabel!
    @IBOutlet weak var mLogo: UIImageView!
    @IBOutlet weak var mClientCode: UILabel!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
