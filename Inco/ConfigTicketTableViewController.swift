//
//  ConfigTicketTableViewController.swift
//  Inco
//
//  Created by admin on 8/4/16.
//  Copyright © 2016 Ban Sac. All rights reserved.
//

import UIKit
import Alamofire
import AlamofireImage

class ConfigTicketTableViewController: UITableViewController  {

    @IBOutlet weak var btnDeparment: UIButton!
    
    @IBOutlet weak var btnType: UIButton!
    
    @IBOutlet weak var btStaus: UIButton!
    
    
    @IBOutlet weak var mContainCreateBy: UIView!
    
    @IBOutlet weak var mContainNotify: UIView!
    var projectId:String?
    var ticketForm:TicketForm?
    var heightCreateBy = 0
    var heightNotify = 0
    var preData:UITableViewDataSource?

    var listCheckbox = [UISwitch]()
    var listnotify = [UISwitch]()
      let indicator:UIActivityIndicatorView = UIActivityIndicatorView  (activityIndicatorStyle: UIActivityIndicatorViewStyle.WhiteLarge)
    var isConfig = false
    override func viewDidLoad() {
        super.viewDidLoad()

        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem()
        indicator.color = UIColor(red: 141.0/255.0, green: 184.0/255.0, blue: 61.0/255.0, alpha: 1.0)
        let x = UIScreen.mainScreen().applicationFrame.size.width/2;
        let y = UIScreen.mainScreen().applicationFrame.size.height/2;
        indicator.frame = CGRectMake(0.0, 0.0, 40.0, 40.0)
        indicator.center.x = x
        indicator.center.y = y
        self.tableView.addSubview(indicator)
        indicator.bringSubviewToFront(self.tableView)
        indicator.startAnimating()
        preData = self.tableView.dataSource
        self.tableView.dataSource  = nil
        self.loadFormTicket()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    func changeDepartment(item:BaseItem)  {
        self.btnDeparment.setTitle(item.value, forState: UIControlState.Normal)
        self.btnDeparment.tag = Int((item.key)!)!

    }
    @IBAction func chooseDeparment(sender: UIButton) {
        // 1
        let optionMenu = UIAlertController(title: nil, message: "Deparment", preferredStyle: .ActionSheet)
        
        // 2
        for item in (self.ticketForm?.deparments)! {
            var style = UIAlertActionStyle.Default
            if item.key == "\(self.btnDeparment.tag)" {
                 style = UIAlertActionStyle.Destructive

            }
            let deleteAction = UIAlertAction(title: item.value, style: style, handler: {
                (alert: UIAlertAction!) -> Void in
                self.changeDepartment(item)
            })
            optionMenu.addAction(deleteAction)
        }
       
        //
        let cancelAction = UIAlertAction(title: "Cancel", style: .Cancel, handler: {
            (alert: UIAlertAction!) -> Void in
           // printn("Cancelled")
        })
        
        
        // 4
      
        optionMenu.addAction(cancelAction)
        optionMenu.popoverPresentationController?.sourceView = self.btnDeparment
        optionMenu.popoverPresentationController?.sourceRect = self.btnDeparment.bounds
        // 5
        self.presentViewController(optionMenu, animated: true, completion: nil)
    }
    
    func changeType(item:BaseItem)  {
        self.btnType.setTitle(item.value, forState: UIControlState.Normal)
        self.btnType.tag = Int((item.key)!)!
        
    }

    @IBAction func chooseType(sender: UIButton) {
        
        // 1
        let optionMenu = UIAlertController(title: nil, message: "Type", preferredStyle: .ActionSheet)
        
        // 2
        for item in (self.ticketForm?.ticketsTypes)! {
            var style = UIAlertActionStyle.Default
            if item.key == "\(self.btnType.tag)" {
                style = UIAlertActionStyle.Destructive
                
            }
            let deleteAction = UIAlertAction(title: item.value, style: style, handler: {
                (alert: UIAlertAction!) -> Void in
                self.changeType(item)
            })
            optionMenu.addAction(deleteAction)
        }
        
        //
        let cancelAction = UIAlertAction(title: "Cancel", style: .Cancel, handler: {
            (alert: UIAlertAction!) -> Void in
            // printn("Cancelled")
        })
        
        
        // 4
        
        optionMenu.addAction(cancelAction)
        optionMenu.popoverPresentationController?.sourceView = self.btnType
        optionMenu.popoverPresentationController?.sourceRect = self.btnType.bounds
        
        // 5
        self.presentViewController(optionMenu, animated: true, completion: nil)

    }
    
    func changeStatus(item:BaseItem){
        self.btStaus.setTitle(item.value, forState: UIControlState.Normal)
        self.btStaus.tag = Int((item.key)!)!
    
    }
    @IBAction func chooseStatus(sender: UIButton) {
        // 1
        let optionMenu = UIAlertController(title: nil, message: "Status", preferredStyle: .ActionSheet)
        
        // 2
        for item in (self.ticketForm?.ticketsStatus)! {
            var style = UIAlertActionStyle.Default
            if item.key == "\(self.btStaus.tag)" {
                style = UIAlertActionStyle.Destructive
                
            }
            let deleteAction = UIAlertAction(title: item.value, style: style, handler: {
                (alert: UIAlertAction!) -> Void in
                self.changeStatus(item)
            })
            optionMenu.addAction(deleteAction)
        }
        
        //
        let cancelAction = UIAlertAction(title: "Cancel", style: .Cancel, handler: {
            (alert: UIAlertAction!) -> Void in
            // printn("Cancelled")
        })
        
        
        // 4
        
        optionMenu.addAction(cancelAction)
        optionMenu.popoverPresentationController?.sourceView = self.btStaus
        optionMenu.popoverPresentationController?.sourceRect = self.btStaus.bounds
        
        // 5
        self.presentViewController(optionMenu, animated: true, completion: nil)
    }
    
    func initConfigForm() {
        if self.ticketForm == nil{
            return
        }
        self.btnDeparment.setTitle(self.ticketForm?.deparments[0].value, forState: UIControlState.Normal)
        self.btnDeparment.tag = Int((self.ticketForm?.deparments[0].key)!)!
        for item in (self.ticketForm?.ticketsStatus)! {
            if item.key == self.ticketForm?.ticketsStatusDefault {
                self.btStaus.setTitle(item.value, forState: UIControlState.Normal)
                self.btStaus.tag = Int((item.key)!)!
            }
        }
        for item in (self.ticketForm?.ticketsTypes)! {
            if item.key == self.ticketForm?.ticketsTypesDefault {
                self.btnType.setTitle(item.value, forState: UIControlState.Normal)
                self.btnType.tag = Int((item.key)!)!
            }
        }
        self.heightCreateBy = 0
        listCheckbox.removeAll()
        for item in (self.ticketForm?.users)! {
            let label = UILabel(frame:CGRect(x: 40 , y: self.heightCreateBy, width: 200, height: 15))
            label.text = item.name
            self.heightCreateBy += 15
            var isSet = false
            self.mContainCreateBy.addSubview(label)
            for itemchild in item.users {
                let user = CheckBoxUserView(frame: CGRect(x: 0, y: self.heightCreateBy, width: 400, height: 47))
                 user.mLabel.text = itemchild.value
                 user.tag = Int(itemchild.key!)!
                if isSet == false {
                    user.mCheckBox.on = true
                    user.mCheckBox.enabled = false
                    isSet = true
                }else{
                    user.mCheckBox.on = false
                }
                listCheckbox.append(user.mCheckBox)
                user.mCheckBox.addTarget(self, action: #selector(ConfigTicketTableViewController.changeCreateBy), forControlEvents: UIControlEvents.ValueChanged)
                self.heightCreateBy += 47
                self.mContainCreateBy.addSubview(user)
            }
        }
        self.heightNotify = 0
        self.listnotify.removeAll()
        for item in (self.ticketForm?.notify)! {
            let label = UILabel(frame:CGRect(x: 40 , y: self.heightNotify, width: 200, height: 15))
            label.text = item.name
            self.heightNotify += 15
            self.mContainNotify.addSubview(label)
            for itemchild in item.users {
                let user = CheckBoxUserView(frame: CGRect(x: 0, y: self.heightNotify, width: 400, height: 47))
                user.mLabel.text = itemchild.value
                user.mCheckBox.on = false
                user.tag = Int(itemchild.key!)!
                listnotify.append(user.mCheckBox)
               
                self.heightNotify += 47
                self.mContainNotify.addSubview(user)
            }
        }
        self.tableView.dataSource = self.preData

        self.tableView.reloadData()
        self.isConfig = true
    
    }
    func changeCreateBy() {
        for item in self.listCheckbox {
            if item.enabled == false && item.on == true{
                item.setOn(false, animated: true)
                item.enabled = true
            }else if item.enabled == true && item.on == true {
                item.enabled = false

            }
        }
    }
    func loadFormTicket() {
        let token = IncoCommon.getToken() as String
        print("token: \(token)")
        
        let parameters:[String:AnyObject] = [IncoApi.TOKEN_PARAMETER:token,
                                             IncoApi.PROJECT_ID_PARAMETER:self.projectId!]
        
      Alamofire.request(.POST, IncoApi.getApi(IncoApi.API_GET_TICKET_FORM),parameters: parameters) .responseJSON { response in // 1
            print(response.request)  // original URL request
            print(response.response) // URL response
            print(response.data)     // server data
            print(response.result)   // result of response serialization
            print(response.result.error )
            if let JSON = response.result.value {
                print("JSON: \(JSON)")
                let inco = IncoResponse(data: JSON as! NSDictionary)
                if inco.isOK(){
                    self.ticketForm = TicketForm(data:inco.data[0])
                    self.initConfigForm()
                   // self.detail = DetailTicketComponent (data: inco.data[0])
                   // self.populateInfo()
                   // self.tableView.reloadData()
                }
                self.indicator.stopAnimating()
               self.indicator.hidesWhenStopped = true
            }
            return
        }

    }
    func getDeparmentId() -> String {
        return "\(self.btnDeparment.tag)"
    }
    func getStatusId() -> String {
        return "\(self.btStaus.tag)"
    }
    func getTypeId() -> String {
        return "\(self.btnType.tag)"
    }
    func getCreateByID() -> String {
        for item in self.listCheckbox {
            if item.on == true {
                return "\(item.tag)"
            
            }
        
        }
        return"0"
    }
    func getNotifyID() -> [String] {
        var result = [String]()
        for item in self.listnotify {
            if item.on == true {
                    result.append("\(item.tag)")
            }
            
        }
        return result
    }
    // MARK: - Table view data source

    //override func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        // #warning Incomplete implementation, return the number of sections
     //   return 0
   // }

   // override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
//return 0
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
    // The number of columns of data
 
    override func tableView(tableView: UITableView, heightForRowAtIndexPath indexPath: NSIndexPath) -> CGFloat {
        if indexPath.row == 3{
            return CGFloat(self.heightCreateBy + 44)
        }
        if indexPath.row == 4 {
            return CGFloat(self.heightNotify + 44)
        }
        return 75
    }
}
