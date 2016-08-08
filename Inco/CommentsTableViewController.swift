//
//  CommentsTableViewController.swift
//  Inco
//
//  Created by admin on 7/27/16.
//  Copyright © 2016 Ban Sac. All rights reserved.
//

import UIKit
import Alamofire
import AlamofireImage
class CommentsTableViewController: UITableViewController {
    
    let indicator:UIActivityIndicatorView = UIActivityIndicatorView  (activityIndicatorStyle: UIActivityIndicatorViewStyle.WhiteLarge)
    var comments = [CommentCell]()
    var type:ComponentType = ComponentType.PROJECT
    var loadingcell :LoadingMoreCell!
    var id:String = "0"
    override func viewDidLoad() {
        super.viewDidLoad()
        self.tableView.estimatedRowHeight = 44
        self.tableView.rowHeight = UITableViewAutomaticDimension
        
        indicator.color = UIColor(red: 141.0/255.0, green: 184.0/255.0, blue: 61.0/255.0, alpha: 1.0)
        let x = UIScreen.mainScreen().applicationFrame.size.width/2;
        let y = UIScreen.mainScreen().applicationFrame.size.height/2;
        indicator.frame = CGRectMake(0.0, 0.0, 10.0, 10.0)
        indicator.center.x = x
          indicator.center.y = y
        self.tableView.addSubview(indicator)
        indicator.bringSubviewToFront(self.tableView)
        indicator.startAnimating()
        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem()
         self.tableView.registerNib(UINib(nibName: "LoadingMoreCell", bundle: nil), forCellReuseIdentifier: "LoadingMoreCell")
        self.tableView.registerNib(UINib(nibName: "CommentTableViewCell", bundle: nil), forCellReuseIdentifier: "CommentTableViewCell")
        getListComment()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    // MARK: - Table view data source

    override func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        // #warning Incomplete implementation, return the number of sections
        return 1
    }

    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if comments.count == 0 {
            
            return 1
        }
        if comments.count < IncoCommon.NUMBER_ITEMS_LOADING {
            return comments.count
        }
        return comments.count+1
    }
    func getApi(type:ComponentType) -> String {
        var url:String?
        switch type {
            case ComponentType.PROJECT:
                  url = IncoApi.getApi(IncoApi.API_COMMENT_OF_PROJECT)
            case ComponentType.TASKS:
                 url = IncoApi.getApi(IncoApi.API_COMMENT_OF_TASK)
            case ComponentType.TICKET:
                url = IncoApi.getApi(IncoApi.API_COMMENT_OF_TICKET)
            case ComponentType.DISCUSSTION:
                url = IncoApi.getApi(IncoApi.API_COMMENT_OF_DISCUSSIONS)
        }
        return url!

    }
    func getParameterApi(type:ComponentType,id:String) -> [String:AnyObject] {
        let token = IncoCommon.getToken() as String
        var parameters:[String:AnyObject] =  [:]
        parameters[IncoApi.TOKEN_PARAMETER] = token
        parameters[IncoApi.OFFSET_PARAMETER] = self.comments.count
        parameters[IncoApi.LIMIT_PARAMETER] = IncoCommon.NUMBER_ITEMS_LOADING
        switch type {
            case ComponentType.PROJECT:
                parameters[IncoApi.PROJECT_ID_PARAMETER] = id
            case ComponentType.TASKS:
                parameters[IncoApi.TASK_ID_PARAMETER] = id
            case ComponentType.TICKET:
               parameters[IncoApi.TICKET_ID_PARAMETER] = id
            case ComponentType.DISCUSSTION:
                parameters[IncoApi.DISCUSSION_ID_PARAMETER] = id
        }
        return parameters
    }
    func refresh() {
        indicator.startAnimating()
        self.comments.removeAll()
        self.getListComment()

    }
    func getListComment()  {
        let url = self.getApi(self.type)
        let parameters = self.getParameterApi(self.type, id: self.id)
        Alamofire.request(.POST, url,parameters: parameters) .responseJSON { response in // 1
            print(response.request)  // original URL request
            print(response.response) // URL response
            print(response.data)     // server data
            print(response.result)   // result of response serialization
            
            if let JSON = response.result.value {
                print("JSON: \(JSON)")
                let inco = IncoResponse(data: JSON as! NSDictionary)
                if inco.isOK(){
                    for item in inco.data {
                        self.comments.append(CommentCell(data: item))
                    }
                    if self.loadingcell != nil{
                        self.loadingcell.setLoading(false)
                    }
                    self.indicator.stopAnimating()
                    self.indicator.hidesWhenStopped = true
                    self.tableView.reloadData()
                    
                }
                
            }
        }
    }
    func createCommentCell(item:CommentCell,tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> CommentTableViewCell {
         let cell  = tableView.dequeueReusableCellWithIdentifier("CommentTableViewCell", forIndexPath: indexPath) as! CommentTableViewCell
        cell.mName.text = item.name
        cell.mCreateAt.text = item.created_at
        let downloadURL = NSURL(string: IncoApi.getAvatar(item.avatar!))
        cell.mAvatar.af_setImageWithURL(downloadURL!)
        let attrStr = try! NSAttributedString(
            data: (item.description!.dataUsingEncoding(NSUnicodeStringEncoding, allowLossyConversion: true)!),
            options: [ NSDocumentTypeDocumentAttribute: NSHTMLTextDocumentType],
            documentAttributes: nil)
        cell.mDiscription.attributedText = attrStr
         cell.mDiscription.font = UIFont.systemFontOfSize(15.0)
        if item.attachments.count > 0 {
            cell.mFileAttch.hidden = false
        }else {
            cell.mFileAttch.hidden = true
        }
        return cell
    }
    
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {

        if comments.count == 0 {
            let loadingcell = tableView.dequeueReusableCellWithIdentifier("LoadingMoreCell", forIndexPath: indexPath) as! LoadingMoreCell
            loadingcell.setEmptyState(true)
            return loadingcell;
        }else if comments.count < IncoCommon.NUMBER_ITEMS_LOADING {
            
            
            return self.createCommentCell(self.comments[indexPath.row], tableView: tableView, cellForRowAtIndexPath: indexPath)
            
        }else {
            if indexPath.row == comments.count {
                let loadingcell = tableView.dequeueReusableCellWithIdentifier("LoadingMoreCell", forIndexPath: indexPath) as! LoadingMoreCell
                
                loadingcell.setEmptyState(false)
                return loadingcell;
            }else{
                  return self.createCommentCell(self.comments[indexPath.row], tableView: tableView, cellForRowAtIndexPath: indexPath)
                
            }
            
        }

    }
    
    override func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        self.loadingcell = tableView.cellForRowAtIndexPath(indexPath) as? LoadingMoreCell
        if self.loadingcell != nil {
            loadingcell.setLoading(true)
            self.getListComment()
            return
        }
        let projectStoryboard: UIStoryboard = UIStoryboard(name: "detailcomponent", bundle: nil)
        let detail = projectStoryboard.instantiateViewControllerWithIdentifier("DetailCommentTableViewController") as! DetailCommentTableViewController
        detail.item = self.comments[indexPath.row]
        
        self.navigationController?.pushViewController(detail, animated: true)
    }
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
