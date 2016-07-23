//
//  MainViewController.swift
//  Inco
//
//  Created by admin on 7/18/16.
//  Copyright © 2016 Ban Sac. All rights reserved.
//

import UIKit
import Alamofire

class MainViewController: UIViewController,UITableViewDelegate,UITableViewDataSource,UISearchResultsUpdating, UISearchBarDelegate{
    var projects = [ProjectCell]()
    
    @IBOutlet weak var mSearchBar: UISearchBar!
    @IBOutlet weak var mTableview: UITableView!
    override func viewDidLoad() {
        super.viewDidLoad()
        getProjectList()
        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func leftSideBarTap(sender: UIBarButtonItem) {
        let appDelegate: AppDelegate = UIApplication.sharedApplication().delegate as! AppDelegate
        appDelegate.centerContainer?.toggleDrawerSide(MMDrawerSide.Left, animated: true, completion:nil)
        
        
    }
    internal func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int{
        
        return projects.count;
        
    }
    internal func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell{
        let cell = tableView.dequeueReusableCellWithIdentifier("projectcell", forIndexPath: indexPath) as! ProjectViewCell
          let item =  projects[indexPath.row]
            cell.mProjectName.text = item.name
            cell.mType.text = item.type
            cell.mStatus.text = item.status
            cell.mCreateAt.text = item.createAt
            cell.mCreateBy.text = item.createBy
        return cell
    }
    internal  func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath){
        switch indexPath.row {
        case 0:
            break
        case 5:
            break
            
        default:
            
            break
            
        }
    }
    internal func updateSearchResultsForSearchController(searchController: UISearchController){
        
    }
    internal func getProjectList(){
        let token = IncoCommon.getToken() as String
          print("token: \(token)")
        let parameters:[String:AnyObject] = [IncoApi.TOKEN_PARAMETER:token,
                                             IncoApi.OFFSET_PARAMETER:"0",
                                             IncoApi.LIMIT_PARAMETER :"30",
                                             IncoApi.SEARCH_PARAMETER :""
                                             ]
        Alamofire.request(.POST, IncoApi.getApi(IncoApi.API_PROJECT_LIST),parameters: parameters) .responseJSON { response in // 1
            print(response.request)  // original URL request
            print(response.response) // URL response
            print(response.data)     // server data
            print(response.result)   // result of response serialization
            
            if let JSON = response.result.value {
                print("JSON: \(JSON)")
                let inco = IncoResponse(data: JSON as! NSDictionary)
                if inco.isOK(){
                    for item in inco.data {
                        self.projects.append(ProjectCell.createCell(item))
                    }
                    
                    self.mTableview.reloadData()
                }
                
            }
            return
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
