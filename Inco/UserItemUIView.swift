//
//  UserItemUIView.swift
//  Inco
//
//  Created by admin on 7/29/16.
//  Copyright © 2016 Ban Sac. All rights reserved.
//

import UIKit
import Alamofire
import AlamofireImage
@IBDesignable class UserItemUIView: UIView {

    @IBOutlet weak var mAvatar: UIImageView!

    @IBOutlet weak var mEmail: UILabel!
   
  
    var view:UIView!
    
    @IBInspectable var nameUser: String?
        {
        get
        {
            return mEmail.text
        }
        
        set(name)
        {
            mEmail.text = name
        }
    }
    @IBInspectable var photoUser: NSURL?
        {
      
        get{
        
            return nil
        }
        set(URL)
        {
            mAvatar.af_setImageWithURL(URL!)
        }
    }
    /*
    // Only override drawRect: if you perform custom drawing.
    // An empty implementation adversely affects performance during animation.
    override func drawRect(rect: CGRect) {
        // Drawing code
    }
    */
    // once with a NSCoder
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
        let nib = UINib(nibName: "UserItemUIView", bundle: bundle)
        let view = nib.instantiateWithOwner(self, options: nil)[0] as! UIView
        
        return view
    }
   
}
