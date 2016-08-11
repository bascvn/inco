//
//  CompanyTableViewController.swift
//  Inco
//
//  Created by admin on 8/1/16.
//  Copyright © 2016 Ban Sac. All rights reserved.
//

import UIKit
import Alamofire
import AlamofireImage

protocol CompanyTableDelegate {
    func companyslected(clientId:String)
}
class CompanyTableViewController: UITableViewController,UISearchResultsUpdating, UISearchBarDelegate,UISearchDisplayDelegate {
    var companies = [Company]()
    var delegate: CompanyTableDelegate?
    let indicator:UIActivityIndicatorView = UIActivityIndicatorView  (activityIndicatorStyle: UIActivityIndicatorViewStyle.WhiteLarge)
    override func viewDidLoad() {
        super.viewDidLoad()
        self.title = CommonMess.SEARCH_COMPANY_TITLE
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
        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem()
       // self.getCompany()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    func getCompany(search:String)   {
        self.indicator.startAnimating()
        self.companies.removeAll()
        let parameters = [IncoApi.SEARCH_WORD : search]
        Alamofire.request(.POST, IncoApi.getCompanies(),parameters: parameters ) .responseJSON { response in // 1
            print(response.request)  // original URL request
            print(response.response) // URL response
            print(response.data)     // server data
            print(response.result)   // result of response serialization
            
            if let JSON = response.result.value {
                let status = JSON.valueForKey("status") as! Int
                if status == 200 {
                   let data = JSON.valueForKey("data") as! [NSDictionary]
                    for item in data {
                        self.companies.append(Company(data:item))
                    }
                }
                self.tableView.reloadData()
                self.indicator.stopAnimating()
                self.indicator.hidesWhenStopped = true
                
            }
        }

    }
    override func tableView(tableView: UITableView, heightForRowAtIndexPath indexPath: NSIndexPath) -> CGFloat {
        
        return 76
    }
    // MARK: - Table view data source

    override func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        // #warning Incomplete implementation, return the number of sections
        if self.companies.count  == 0 {
         
            
            let messageLabel = UILabel(frame: CGRect(x: 0,y: 0,width: self.view.bounds.size.width, height: self.view.bounds.size.height))
            messageLabel.text = CommonMess.SEARCH_ERROR_MESS
            messageLabel.textColor = UIColor.blackColor()
            messageLabel.textAlignment = NSTextAlignment.Center
            messageLabel.font = UIFont(name: "Palatino-Italic", size: 20)
            messageLabel.numberOfLines = 0
            messageLabel.sizeToFit()

            self.tableView.backgroundView = messageLabel;
            self.tableView.separatorStyle = UITableViewCellSeparatorStyle.None
            return 0
        }
        self.tableView.backgroundView = nil
        self.tableView.separatorStyle = UITableViewCellSeparatorStyle.SingleLine;

        return 1
    }

    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        return self.companies.count
    }

    
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier("CompanyTableViewCell", forIndexPath: indexPath) as! CompanyTableViewCell
        cell.mClientCode.text = self.companies[indexPath.row].ClientCode
        cell.mDiscription.text = self.companies[indexPath.row].ClientName
        let photo = NSURL(string: IncoApi.getLogoCompany(self.companies[indexPath.row].Logo!))
        cell.mLogo.af_setImageWithURL(photo!)
        // Configure the cell...

        return cell
    }
    
    override func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        self.delegate?.companyslected(self.companies[indexPath.row].ClientCode!)
        self.navigationController?.popViewControllerAnimated(true)
    }
    internal func searchBarCancelButtonClicked(searchBar: UISearchBar) {
    }
    
    internal func searchBarSearchButtonClicked(searchBar: UISearchBar) {
        view.endEditing(true)
        if searchBar.text?.characters.count > 2 {
            self.getCompany(searchBar.text!)
        }

     
    }
    internal func updateSearchResultsForSearchController(searchController: UISearchController){
        
        
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
