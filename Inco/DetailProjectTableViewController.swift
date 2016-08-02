//
//  DetailProjectTableViewController.swift
//  Inco
//
//  Created by admin on 7/31/16.
//  Copyright © 2016 Ban Sac. All rights reserved.
//

import UIKit
import Alamofire
import AlamofireImage
class DetailProjectTableViewController: UITableViewController {
    var detail:DetailProjectComponent?
    var item:ProjectCell?

    @IBOutlet weak var mType: UILabel!
    @IBOutlet weak var mStatus: UILabel!
    @IBOutlet weak var mId: UILabel!
    @IBOutlet weak var mCreateAt: UILabel!
    @IBOutlet weak var mName: UILabel!
    @IBOutlet weak var mAvatar: UIImageView!
    @IBOutlet weak var mDiscription: UILabel!
    
     let indicator:UIActivityIndicatorView = UIActivityIndicatorView  (activityIndicatorStyle: UIActivityIndicatorViewStyle.Gray)
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem()
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
        
        self.loadDetailProject()
    }
    @IBOutlet weak var mTeam: UIView!

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    func populateInfo()  {
        if self.detail == nil {
            return
        }
        self.mName.text = self.detail?.name
        self.mCreateAt.text = self.detail?.create_at
        self.mId.text = self.detail?.id
        self.mStatus.text = self.detail?.detail.status
        let photo = NSURL(string: IncoApi.getAvatar((self.detail?.photo)!))
        self.mAvatar.af_setImageWithURL(photo!)
        let attrStr = try! NSAttributedString(
            data: (self.detail?.description!.dataUsingEncoding(NSUnicodeStringEncoding, allowLossyConversion: true)!)!,
            options: [ NSDocumentTypeDocumentAttribute: NSHTMLTextDocumentType],
            documentAttributes: nil)
        self.mDiscription.attributedText = attrStr
        self.mDiscription.font = UIFont.systemFontOfSize(15.0)

        if self.detail?.detail.team.count > 0 {
            var  count = 0
            var frame = self.mTeam.frame
            var y = 0
            for userInfo in (self.detail?.detail.team)!
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
                self.mTeam.frame = frame
                self.mTeam.addSubview(user)
                count = count + 1;
                
            }
            
        }
        
    }
    func loadDetailProject() {
        let token = IncoCommon.getToken() as String
        print("token: \(token)")
        
        let parameters:[String:AnyObject] = [IncoApi.TOKEN_PARAMETER:token,
                                             IncoApi.PROJECT_ID_PARAMETER:self.item!.id!
                                            ]
        Alamofire.request(.POST, IncoApi.getApi(IncoApi.API_GET_PROJECT),parameters: parameters) .responseJSON { response in // 1
            print(response.request)  // original URL request
            print(response.response) // URL response
            print(response.data)     // server data
            print(response.result)   // result of response serialization
            
            if let JSON = response.result.value {
                print("JSON: \(JSON)")
                let inco = IncoResponse(data: JSON as! NSDictionary)
                if inco.isOK(){
                    self.detail = DetailProjectComponent (data: inco.data[0])
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
        if indexPath.section == 1 && indexPath.row == 3{
            return self.mTeam.frame.height + 16
        }
        return UITableViewAutomaticDimension
    }
    // MARK: - Table view data source

    //override func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        // #warning Incomplete implementation, return the number of sections
        //return 0
    //}

   // override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
      //  return 0
   // }

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

}
