//
//  LeftSideViewController.swift
//  Inco
//
//  Created by admin on 7/18/16.
//  Copyright © 2016 Ban Sac. All rights reserved.
//

import UIKit

import Alamofire
import AlamofireImage

class LeftSideViewController: UIViewController ,UITableViewDelegate,UITableViewDataSource{
    
    @IBOutlet weak var mAvatar: UIImageView!
    
    @IBOutlet weak var mVersion: UILabel!
    @IBOutlet weak var mEmail: UILabel!
    @IBOutlet weak var mName: UILabel!
    var menus:[[LeftMenuItem]] =
        [ [LeftMenuItem(icon: "project",subject: CommonMess.PROJECT),
    
            LeftMenuItem(icon: "task",subject: CommonMess.TASKS),
            LeftMenuItem(icon: "ticket",subject: CommonMess.TICKETS),
            LeftMenuItem(icon: "disccustion",subject: CommonMess.PROJECT)],
          
        [ LeftMenuItem(icon: "profile",subject: NSLocalizedString("Profile",comment: "")),
        LeftMenuItem(icon: "logout",subject: NSLocalizedString("Logout",comment: ""))]]


    override func viewDidLoad() {
        super.viewDidLoad()
        mVersion.text = IncoCommon.getVersion()
        let user = IncoCommon.getUsrInfo()! as NSDictionary
        self.mEmail.text = user.valueForKey("email") as? String
        self.mName.text  = user.valueForKey("name") as? String
        let avatar = user.valueForKey("photo") as? String
        if avatar != nil{
            let downloadURL = NSURL(string: IncoApi.getAvatar(avatar!))
                mAvatar.af_setImageWithURL(downloadURL!)
        }
        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        //print("section: \(self.menus.count) ")
        return self.menus.count
    }
    internal func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int{
        
        return self.menus[section].count
    
    }
     func tableView(tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        return " "
    }
    // Row display. Implementers should *always* try to reuse cells by setting each cell's reuseIdentifier and querying for available reusable cells with dequeueReusableCellWithIdentifier:
    // Cell gets various attributes set automatically based on table (separators) and data source (accessory views, editing controls)
    
   
    internal func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell{
        let cell = tableView.dequeueReusableCellWithIdentifier("LeftMenuCell", forIndexPath: indexPath) as! LeftMenuItemTableViewCell
        cell.subject.text = menus[indexPath.section][indexPath.row].subject
        let image : UIImage = UIImage(named: menus[indexPath.section][indexPath.row].icon)!
        cell.mIcon.image = image
       
        return cell
    }
    func logout() {
        let token = IncoCommon.getToken() as String
        print("token: \(token)")
        
        let parameters:[String:AnyObject] = [IncoApi.TOKEN_PARAMETER:token]
        Alamofire.request(.POST, IncoApi.getApi(IncoApi.API_LOGOUT),parameters: parameters) .responseJSON { response in // 1
            print(response.request)  // original URL request
            print(response.response) // URL response
            print(response.data)     // server data
            print(response.result)   // result of response serialization
            
            if let JSON = response.result.value {
                print("JSON: \(JSON)")
                let inco = IncoResponse(data: JSON as! NSDictionary)
                if inco.isOK(){
                   
                }
                
            }
            return
        }

        IncoCommon.logOut()
        let loginViewController = self.storyboard?.instantiateViewControllerWithIdentifier("LoginViewController")
            as! ViewController
        let appDelegate: AppDelegate = UIApplication.sharedApplication().delegate as! AppDelegate
          let mainNavContoller = UINavigationController(rootViewController: loginViewController)
        appDelegate.window?.rootViewController = mainNavContoller
    
    }
    internal  func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath){
        switch indexPath.section {
        case 0:
            switch indexPath.row {
                case 0,1,2,3:
                    let mainViewController = self.storyboard?.instantiateViewControllerWithIdentifier("MainViewController")
                        as! MainViewController
                    let mainNavContoller = UINavigationController(rootViewController: mainViewController)
                    let appDelegate: AppDelegate = UIApplication.sharedApplication().delegate as! AppDelegate
                    if indexPath.row == 1 {
                        mainViewController.type = ComponentType.TASKS
                    }else if indexPath.row == 2{
                        mainViewController.type = ComponentType.TICKET
                        
                    }else if indexPath.row == 3{
                        mainViewController.type = ComponentType.DISCUSSTION
                    }else{
                    }
                    appDelegate.centerContainer?.centerViewController = mainNavContoller
                    appDelegate.centerContainer?.toggleDrawerSide(MMDrawerSide.Left, animated: true, completion: nil)
               
                default: break
                
            }
           
            break
        case 1:
            if indexPath.row == 1{
                let callActionHandler = { (action:UIAlertAction!) -> Void in
                    self.logout()
                }
                let alertController = UIAlertController(title: CommonMess.ALERT, message: CommonMess.LOGOUT_CONFIRM, preferredStyle: UIAlertControllerStyle.Alert)
                alertController.addAction(UIAlertAction(title: "NO", style:UIAlertActionStyle.Cancel, handler: nil))
                 alertController.addAction(UIAlertAction(title: "OK", style:UIAlertActionStyle.Default, handler:callActionHandler ))
                self.presentViewController(alertController, animated: true, completion: nil)
            } else{
                let profile = self.storyboard?.instantiateViewControllerWithIdentifier("ProfileTableViewController")
                    as! ProfileTableViewController
                let mainNavContoller = UINavigationController(rootViewController: profile)
                let appDelegate: AppDelegate = UIApplication.sharedApplication().delegate as! AppDelegate
                
                appDelegate.centerContainer?.centerViewController = mainNavContoller
                appDelegate.centerContainer?.toggleDrawerSide(MMDrawerSide.Left, animated: true, completion: nil)
            }
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
