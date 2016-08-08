//
//  DownloadFileView.swift
//  Inco
//
//  Created by admin on 8/7/16.
//  Copyright © 2016 Ban Sac. All rights reserved.
//

import UIKit
import Alamofire
import AlamofireImage
class DownloadFileView: UIView {

    /*
    // Only override drawRect: if you perform custom drawing.
    // An empty implementation adversely affects performance during animation.
    override func drawRect(rect: CGRect) {
        // Drawing code
    }
    */
    var view:UIView!
    var id:String = ""
    @IBAction func downloadFile(sender: AnyObject) {
        let destination = Alamofire.Request.suggestedDownloadDestination(directory: .LibraryDirectory, domain: .UserDomainMask)
        
        Alamofire.download(.GET, IncoApi.getApi(IncoApi.API_DOWNLOAD_FILE)+"?id="+self.id+"&token="+IncoCommon.getToken(), destination: destination)
            .progress { bytesRead, totalBytesRead, totalBytesExpectedToRead in
                print(totalBytesRead)
                
                // This closure is NOT called on the main queue for performance
                // reasons. To update your ui, dispatch to the main queue.
                dispatch_async(dispatch_get_main_queue()) {
                    print("Total bytes read on main queue: \(totalBytesRead)")
                }
            }
            .response { _, _, data, error in
                if let error = error {
                    print("Failed with error: \(error)")
                } else {
                    //let image: UIImage = UIImage(data:data!,scale:1.0)!
                    //UIImageWriteToSavedPhotosAlbum(image, nil, nil, nil );
                    print("Downloaded file successfully")
                }
        }
    }
    @IBOutlet weak var mFileInfo: UILabel!
    @IBOutlet weak var mFileName: UILabel!
    @IBOutlet weak var downloadBtn: UIButton!
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
        let nib = UINib(nibName: "DownloadFileView", bundle: bundle)
        let view = nib.instantiateWithOwner(self, options: nil)[0] as! UIView
        
        return view
    }
}
