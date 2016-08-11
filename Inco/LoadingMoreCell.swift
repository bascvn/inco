//
//  LoadingMoreCell.swift
//  Inco
//
//  Created by admin on 7/24/16.
//  Copyright © 2016 Ban Sac. All rights reserved.
//

import UIKit

class LoadingMoreCell: UITableViewCell {

    @IBOutlet weak var mLoadingLabel: UILabel!
    
    @IBOutlet weak var mLoadingIndicator: UIActivityIndicatorView!
    override func awakeFromNib() {
        super.awakeFromNib()
        mLoadingLabel.hidden = false
        mLoadingIndicator.hidden = true
        // Initialization code
    }

    override func setSelected(selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    func setEmptyState(empty: Bool)  {
        if empty == true {
            self.mLoadingLabel.text = CommonMess.CLICK_REFRESH
        }else{
            self.mLoadingLabel.text = CommonMess.READ_MORE

        }
    }
     func setLoading(loading:Bool)  {
        if loading == true{
            self.mLoadingLabel.hidden = true
            self.mLoadingIndicator.hidden = false
            self.mLoadingIndicator.startAnimating()
        }else{
            self.mLoadingLabel.hidden = false
            self.mLoadingIndicator.hidden = true
            self.mLoadingIndicator.stopAnimating()
            self.mLoadingIndicator.hidesWhenStopped = true
        }
    }
    
}
