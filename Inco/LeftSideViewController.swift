//
//  LeftSideViewController.swift
//  Inco
//
//  Created by admin on 7/18/16.
//  Copyright © 2016 Ban Sac. All rights reserved.
//

import UIKit


class LeftSideViewController: UIViewController ,UITableViewDelegate,UITableViewDataSource{
    
    var menus: [LeftMenuItem] = [ LeftMenuItem(icon: "project",subject: "Project"),
                                  LeftMenuItem(icon: "task",subject: "Task"),
                                  LeftMenuItem(icon: "ticket",subject: "Ticket"),
                                  LeftMenuItem(icon: "disccustion",subject: "Disccustion"),
                                  LeftMenuItem(icon: "profile",subject: "Profile"),
                                  LeftMenuItem(icon: "logout",subject: "Logout")

                                  ]

    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    internal func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int{
        
        return menus.count;
    
    }
    
    // Row display. Implementers should *always* try to reuse cells by setting each cell's reuseIdentifier and querying for available reusable cells with dequeueReusableCellWithIdentifier:
    // Cell gets various attributes set automatically based on table (separators) and data source (accessory views, editing controls)
    
   
    internal func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell{
        let cell = tableView.dequeueReusableCellWithIdentifier("LeftMenuCell", forIndexPath: indexPath) as! LeftMenuItemTableViewCell
        cell.subject.text = menus[indexPath.row].subject
        let image : UIImage = UIImage(named: menus[indexPath.row].icon)!
        cell.mIcon.image = image
       
        return cell
    }
    func logout() {
        IncoCommon.logOut()
        let loginViewController = self.storyboard?.instantiateViewControllerWithIdentifier("LoginViewController")
            as! ViewController
        let appDelegate: AppDelegate = UIApplication.sharedApplication().delegate as! AppDelegate
        appDelegate.window?.rootViewController = loginViewController
    
    }
    internal  func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath){
        switch indexPath.row {
        case 0:
            break
        case 5:
            let callActionHandler = { (action:UIAlertAction!) -> Void in
                self.logout()
            }
            let alertController = UIAlertController(title: CommonMess.ALERT, message: CommonMess.LOGOUT_CONFIRM, preferredStyle: UIAlertControllerStyle.Alert)
            alertController.addAction(UIAlertAction(title: "NO", style:UIAlertActionStyle.Cancel, handler: nil))
             alertController.addAction(UIAlertAction(title: "OK", style:UIAlertActionStyle.Default, handler:callActionHandler ))
            self.presentViewController(alertController, animated: true, completion: nil)
            break
            
        default:
            
            break
            
        }
    }

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
