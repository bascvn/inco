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
    
    @IBOutlet weak var mDiscription: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.tableView.estimatedRowHeight = 44
        self.tableView.rowHeight = UITableViewAutomaticDimension
        
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
                    if self.detail != nil {
                        var attrStr = try! NSAttributedString(
                            data: (self.detail?.description!.dataUsingEncoding(NSUnicodeStringEncoding, allowLossyConversion: true)!)!,
                            options: [ NSDocumentTypeDocumentAttribute: NSHTMLTextDocumentType],
                            documentAttributes: nil)
                       // label.attributedText = attrStr
                        self.mDiscription.attributedText = attrStr
                    }
                    self.tableView.reloadData()
                }
                
            }
            return
        }
        
    }
    override func tableView(tableView: UITableView, heightForRowAtIndexPath indexPath: NSIndexPath) -> CGFloat {
        return UITableViewAutomaticDimension
    }
}
