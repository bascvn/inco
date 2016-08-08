//
//  DetailCommentTableViewController.swift
//  Inco
//
//  Created by admin on 8/7/16.
//  Copyright © 2016 Ban Sac. All rights reserved.
//

import UIKit
import Alamofire
import AlamofireImage
class DetailCommentTableViewController: UITableViewController {

    @IBOutlet weak var mAvatar: UIImageView!
    @IBOutlet weak var mCreateBy: UILabel!
    
    @IBOutlet weak var mCreateAt: UILabel!
    
    @IBOutlet weak var mDescription: UILabel!
    
    
    @IBOutlet weak var mFileContain: UIView!
    
    var item:CommentCell!
    override func viewDidLoad() {
        super.viewDidLoad()
        self.tableView.estimatedRowHeight = 44
        self.tableView.rowHeight = UITableViewAutomaticDimension
        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem()
        let photo = NSURL(string: IncoApi.getAvatar((self.item.avatar)!))
        self.mAvatar.af_setImageWithURL(photo!)
        let attrStr = try! NSAttributedString(
            data: (self.item?.description!.dataUsingEncoding(NSUnicodeStringEncoding, allowLossyConversion: true)!)!,
            options: [ NSDocumentTypeDocumentAttribute: NSHTMLTextDocumentType],
            documentAttributes: nil)
        self.mDescription.attributedText = attrStr
        self.mDescription.font = UIFont.systemFontOfSize(15.0)
        self.mCreateBy.text = self.item.name
        self.mCreateAt.text = self.item.created_at
        var y = 0
        for file in self.item.attachments {
            let fileview = DownloadFileView(frame: CGRect(x: 0, y: y, width: 400, height: 49))
            fileview.mFileName.text = file.file
            fileview.mFileInfo.text = file.info
            fileview.id = file.id!
        
            self.mFileContain.addSubview(fileview)
            y = y + 49
            
        }
    }
    
    override func tableView(tableView: UITableView, heightForRowAtIndexPath indexPath: NSIndexPath) -> CGFloat {
        if indexPath.section == 2{
            if self.item.attachments.count == 0 {

            }
            return CGFloat(self.item.attachments.count*49)
        }
        return UITableViewAutomaticDimension
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

    //override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
    //    return 0
    //}

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
