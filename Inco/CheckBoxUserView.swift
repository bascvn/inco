//
//  CheckBoxUserView.swift
//  Inco
//
//  Created by admin on 8/6/16.
//  Copyright © 2016 Ban Sac. All rights reserved.
//

import UIKit

class CheckBoxUserView: UIView {

    @IBOutlet weak var mLabel: UILabel!
    @IBOutlet weak var mCheckBox: UISwitch!
    var view:UIView!
    /*
    // Only override drawRect: if you perform custom drawing.
    // An empty implementation adversely affects performance during animation.
    override func drawRect(rect: CGRect) {
        // Drawing code
    }
    */
    override init(frame: CGRect) {
        super.init(frame: frame)
        setup()
    }
    
    required init(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)!
        setup()
    }
    
    func setup() {
        view = loadViewFromNib()
        view.frame = bounds
        view.autoresizingMask = [.FlexibleWidth, .FlexibleWidth]
        self.addSubview(view)
    }
    
    func loadViewFromNib() -> UIView {
        let bundle = NSBundle(forClass:self.dynamicType)
        let nib = UINib(nibName: "CheckBoxUserView", bundle: bundle)
        let view = nib.instantiateWithOwner(self, options: nil)[0] as! UIView
        
        return view
    }
    

}
