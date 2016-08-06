//
//  DetailTableViewController.swift
//  Inco
//
//  Created by admin on 7/27/16.
//  Copyright © 2016 Ban Sac. All rights reserved.
//

import UIKit
import Alamofire
import AlamofireImage

class DetailTableViewController: UITableViewController {
    
    var item:TaskCell?
    var detail:DetailTaskComponent?
    
    @IBOutlet weak var mAssignToContainer: UIView!
    @IBOutlet weak var mAssignCell: UITableViewCell!
    @IBOutlet weak var mDiscription: UILabel!
    
    @IBOutlet weak var mId: UILabel!
    
    @IBOutlet weak var mLabel: UILabel!
    
    @IBOutlet weak var mStatus: UILabel!
    
    @IBOutlet weak var mPriority: UILabel!
    
    @IBOutlet weak var mType: UILabel!
    
    @IBOutlet weak var photoCreate: UIImageView!
    
    @IBOutlet weak var createBy: UILabel!
    
    @IBOutlet weak var createAt: UILabel!
    @IBOutlet weak var mContainerCell: UIView!
    
    
    let indicator:UIActivityIndicatorView = UIActivityIndicatorView  (activityIndicatorStyle: UIActivityIndicatorViewStyle.Gray)
    override func viewDidLoad() {
        super.viewDidLoad()
        self.tableView.estimatedRowHeight = 44
        self.tableView.rowHeight = UITableViewAutomaticDimension
        
        indicator.color = UIColor(red: 141.0/255.0, green: 184.0/255.0, blue: 61.0/255.0, alpha: 1.0)
        let x = UIScreen.mainScreen().applicationFrame.size.width/2;
        let y = UIScreen.mainScreen().applicationFrame.size.height/2;
        indicator.frame = CGRectMake(0.0, 0.0, 40.0, 40.0)
        indicator.center.x = x
        indicator.center.y = y
        self.tableView.addSubview(indicator)
        indicator.bringSubviewToFront(self.tableView)
        indicator.startAnimating()

        getDetail()
        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem()

    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    // MARK: - Table view data source

   // override func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        // #warning Incomplete implementation, return the number of sections
   //     return 0
   // }

   // override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
   //     return 0
  //  }

    /*
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier("reuseIdentifier", forIndexPath: indexPath)

        // Configure the cell...

        return cell
    }
    */

    /*
    // Override to support conditional editing of the table view.
    override func tableView(tableView: UITableView, canEditRowAtIndexPath indexPath: NSIndexPath) -> Bool {
        // Return false if you do not want the specified item to be editable.
        return true
    }
    */

    /*
    // Override to support editing the table view.
    override func tableView(tableView: UITableView, commitEditingStyle editingStyle: UITableViewCellEditingStyle, forRowAtIndexPath indexPath: NSIndexPath) {
        if editingStyle == .Delete {
            // Delete the row from the data source
            tableView.deleteRowsAtIndexPaths([indexPath], withRowAnimation: .Fade)
        } else if editingStyle == .Insert {
            // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
        }    
    }
    */

    /*
    // Override to support rearranging the table view.
    override func tableView(tableView: UITableView, moveRowAtIndexPath fromIndexPath: NSIndexPath, toIndexPath: NSIndexPath) {

    }
    */

    /*
    // Override to support conditional rearranging of the table view.
    override func tableView(tableView: UITableView, canMoveRowAtIndexPath indexPath: NSIndexPath) -> Bool {
        // Return false if you do not want the item to be re-orderable.
        return true
    }
    */

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */
    func populateInfo() {
        if self.detail == nil {
            return
        }
        let attrStr = try! NSAttributedString(
            data: (self.detail?.description!.dataUsingEncoding(NSUnicodeStringEncoding, allowLossyConversion: true)!)!,
            options: [ NSDocumentTypeDocumentAttribute: NSHTMLTextDocumentType],
            documentAttributes: nil)
        self.mDiscription.attributedText = attrStr
        self.mDiscription.font = UIFont.systemFontOfSize(15.0)

        self.mId.text = self.detail?.id
        self.mLabel.text = self.detail?.detail.label
        print("label:\(self.detail?.detail.label)")
        self.mPriority.text = self.detail?.detail.priority
        self.mType.text = self.detail?.detail.type
        self.createAt.text = self.detail?.create_at
        self.createBy.text = self.detail?.name
          let photo = NSURL(string: IncoApi.getAvatar(self.detail!.photo!))
        self.photoCreate.af_setImageWithURL(photo!)
        if self.detail?.detail.assigendTo.count > 0 {
            var  count = 0
            var frame = self.mAssignToContainer.frame
            var y = 0
            for userInfo in (self.detail?.detail.assigendTo)!
            {
                y = count * 50
                let user = UserItemUIView(frame: CGRect(x: 0, y: y, width: 200, height: 50))
                // user.translatesAutoresizingMaskIntoConstraints = false
                user.nameUser = userInfo.name
                let downloadURL = NSURL(string: IncoApi.getAvatar(userInfo.photo!))
                user.photoUser = downloadURL
                
                
                if count > 0{
                    user.frame.origin.y =  frame.size.height
                    frame.size.height += user.frame.size.height
                }else{
                    user.frame.origin.y = 0
                    frame.size.height = user.frame.size.height
                }
                self.mAssignToContainer.frame = frame
                self.mAssignToContainer.addSubview(user)
                count = count + 1;
                
            }
            
        }

    }
     func getDetail(){
        let token = IncoCommon.getToken() as String
        print("token: \(token)")
        
        let parameters:[String:AnyObject] = [IncoApi.TOKEN_PARAMETER:token,
                                             IncoApi.ID_PARAMETER:self.item!.id!,
                                             IncoApi.PROJECT_ID_PARAMETER:self.item!.projects_id!]
        Alamofire.request(.POST, IncoApi.getApi(IncoApi.API_GET_TASK),parameters: parameters) .responseJSON { response in // 1
            print(response.request)  // original URL request
            print(response.response) // URL response
            print(response.data)     // server data
            print(response.result)   // result of response serialization
            
            if let JSON = response.result.value {
                print("JSON: \(JSON)")
                let inco = IncoResponse(data: JSON as! NSDictionary)
                if inco.isOK(){
                  self.detail = DetailTaskComponent(data: inco.data[0])
                    self.populateInfo()
                    self.tableView.reloadData()
                }
                self.indicator.stopAnimating()
                self.indicator.hidesWhenStopped = true
            }
            return
        }
        
    }
    override func tableView(tableView: UITableView, heightForRowAtIndexPath indexPath: NSIndexPath) -> CGFloat {
        if indexPath.section == 1 && indexPath.row == 5 {
            return self.mAssignToContainer.frame.height + 16
        }
        return UITableViewAutomaticDimension
    }
}
