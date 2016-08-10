//
//  DownloadFileView.swift
//  Inco
//
//  Created by admin on 8/7/16.
//  Copyright © 2016 Ban Sac. All rights reserved.
//

import UIKit
import AssetsLibrary
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
        self.mIndicator.startAnimating()
        self.downloadBtn.hidden  = true
        var localPath: NSURL?
        let destination: (NSURL, NSHTTPURLResponse) -> (NSURL) = {
            (temporaryURL, response) in
            
            let directoryURL = NSFileManager.defaultManager().URLsForDirectory(.DocumentDirectory, inDomains: .UserDomainMask)[0]
            let pathComponent = response.suggestedFilename
            localPath = directoryURL.URLByAppendingPathComponent(pathComponent!)
            if NSFileManager.defaultManager().fileExistsAtPath((localPath?.path)!) {
                try! NSFileManager.defaultManager().removeItemAtPath((localPath?.path)!)
            }
            return localPath!
        }
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
                    let photo = NSData(contentsOfURL: localPath!)
                    let image = UIImage(data: photo!)
                    if  image != nil {
                        UIImageWriteToSavedPhotosAlbum(image!, nil,nil, nil)
                    }
                    if NSFileManager.defaultManager().fileExistsAtPath((localPath?.path)!) {
                        try! NSFileManager.defaultManager().removeItemAtPath((localPath?.path)!)
                    }

                    print("Downloaded file successfully")
                }
                self.downloadBtn.hidden  = false
                self.mIndicator.stopAnimating()
        }
    }
    @IBOutlet weak var mFileInfo: UILabel!
    @IBOutlet weak var mFileName: UILabel!
    @IBOutlet weak var downloadBtn: UIButton!
    override init(frame: CGRect) {
        super.init(frame: frame)
        setup()
    }
    @IBOutlet weak var mIndicator: UIActivityIndicatorView!
    
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
