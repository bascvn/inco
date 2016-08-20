//
//  NotificationsTableViewController.swift
//  Inco
//
//  Created by admin on 8/11/16.
//  Copyright © 2016 Ban Sac. All rights reserved.
//

import UIKit

class NotificationsTableViewController: UITableViewController {
    var notifications:[Push]?
    override func viewDidLoad() {
        super.viewDidLoad()
        self.tableView.estimatedRowHeight = 44
        self.tableView.rowHeight = UITableViewAutomaticDimension
        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false
        let appDelegate: AppDelegate = UIApplication.sharedApplication().delegate as! AppDelegate
        self.notifications = appDelegate.notifications
        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem()
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
        // #warning Incomplete implementation, return the number of rows
        return (self.notifications?.count)!
    }

    
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier("notificationcell", forIndexPath: indexPath) as! NotificationsTableViewCell
        cell.mDate.text = self.notifications![indexPath.row].date
        cell.mName.text = self.notifications![indexPath.row].title
        cell.mContent.text = self.notifications![indexPath.row].message
        cell.mParent.text = self.notifications![indexPath.row].parent
        if self.notifications![indexPath.row].attach?.characters.count > 0 {
            cell.attachFile.hidden = false
        }else{
            cell.attachFile.hidden = true
        }
        let type = self.notifications![indexPath.row].type
        if type ==  ComponentType.PROJECT {
           cell.mType.image = UIImage(named: "project")
        
        }else if type == ComponentType.TASKS {
            cell.mType.image = UIImage(named: "tabs_task")


        }else if type == ComponentType.TICKET {
            cell.mType.image = UIImage(named: "tabs_ticket")

        }else if type == ComponentType.DISCUSSTION {
        
            cell.mType.image = UIImage(named: "tabs_discusstion")

        }
        return cell
    }
    
    override func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        let tabBarController = DetailTabBarViewController()
        let item = self.notifications![indexPath.row]
        tabBarController.type = item.type!
        tabBarController.iD = item.id!
        let projectStoryboard: UIStoryboard = UIStoryboard(name: "detailcomponent", bundle: nil)
        var detail:UITableViewController
        if item.type == ComponentType.TASKS {
            detail = projectStoryboard.instantiateViewControllerWithIdentifier("DetailTableViewController") as! DetailTableViewController
            let cell = TaskCell()
            cell.id = item.id!
            cell.projects_id = item.project
            (detail as! DetailTableViewController).item = cell
            
            tabBarController.projectID = item.project!
            
        }else if item.type == ComponentType.TICKET {
            detail = projectStoryboard.instantiateViewControllerWithIdentifier("DetailTicketTableViewController") as! DetailTicketTableViewController
            let cell = TicketCell()
            cell.id = item.id!
            cell.projects_id = item.project
            (detail as! DetailTicketTableViewController).item = cell
            
            tabBarController.projectID = item.project!
            
        }else if item.type == ComponentType.DISCUSSTION{
            detail = projectStoryboard.instantiateViewControllerWithIdentifier("DetailDiscussionTableViewController") as! DetailDiscussionTableViewController
            let cell = DiscussionCell()
            cell.id = item.id!
            cell.projects_id = item.project
            (detail as! DetailDiscussionTableViewController).item = cell
            
            tabBarController.projectID = item.project!
            
        }else{
            detail = projectStoryboard.instantiateViewControllerWithIdentifier("DetailProjectTableViewController") as! DetailProjectTableViewController
            tabBarController.projectID = item.project!
        }
        
        let comment =   projectStoryboard.instantiateViewControllerWithIdentifier("CommentsTableViewController") as! CommentsTableViewController
        comment.type = item.type!
        comment.id = item.id!
        let controllers = [detail,comment]
        tabBarController.commentView = comment
        tabBarController.viewControllers = controllers
        let firstImage = UIImage(named: "ic_description")
        let secondImage = UIImage(named: "tabs_discusstion")
        detail.tabBarItem = UITabBarItem(
            title: "",
            image: firstImage,
            tag: 1)
        comment.tabBarItem = UITabBarItem(
            title: "",
            image: secondImage,
            tag:2)
        self.navigationController?.pushViewController(tabBarController, animated: true)
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
