//
//  MainViewController.swift
//  Inco
//
//  Created by admin on 7/18/16.
//  Copyright © 2016 Ban Sac. All rights reserved.
//

import UIKit
import Alamofire

class MainViewController: UIViewController,UITableViewDelegate,UITableViewDataSource,UISearchResultsUpdating, UISearchBarDelegate,UISearchDisplayDelegate{
    var projects = [ProjectCell]()
    var loadingcell :LoadingMoreCell!
    let indicator:UIActivityIndicatorView = UIActivityIndicatorView  (activityIndicatorStyle: UIActivityIndicatorViewStyle.Gray)

    @IBOutlet weak var mSearchBar: UISearchBar!
    @IBOutlet weak var mTableview: UITableView!
    override func viewDidLoad() {
        super.viewDidLoad()
        //self.hideKeyboardWhenTappedAround()
        // loading state
        indicator.color = UIColor(red: 141.0/255.0, green: 184.0/255.0, blue: 61.0/255.0, alpha: 1.0)

        indicator.frame = CGRectMake(0.0, 0.0, 10.0, 10.0)
        indicator.center = self.view.center
        self.view.addSubview(indicator)
        indicator.bringSubviewToFront(self.view)
        indicator.startAnimating()
        // register loading more
         mTableview.registerNib(UINib(nibName: "LoadingMoreCell", bundle: nil), forCellReuseIdentifier: "LoadingMoreCell")
        mTableview.tableFooterView = UIView(frame: CGRectZero)
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
        if projects.count == 0 {
            
            return 1
        }
        if projects.count < IncoCommon.NUMBER_ITEMS_LOADING {
            return projects.count
        }
        return projects.count+1
        
    }
    internal func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell{
       
        if projects.count == 0 {
            let loadingcell = tableView.dequeueReusableCellWithIdentifier("LoadingMoreCell", forIndexPath: indexPath) as! LoadingMoreCell
                loadingcell.setEmptyState(true)
            return loadingcell;
        }else if projects.count < IncoCommon.NUMBER_ITEMS_LOADING {
            
            let cell = tableView.dequeueReusableCellWithIdentifier("projectcell", forIndexPath: indexPath) as! ProjectViewCell
            let item =  projects[indexPath.row]
            cell.mProjectName.text = item.name
            cell.mType.text = item.type
            cell.mStatus.text = item.status
            cell.mCreateAt.text = item.createAt
            cell.mCreateBy.text = item.createBy
            return cell;

        }else {
            if indexPath.row == projects.count {
                let loadingcell = tableView.dequeueReusableCellWithIdentifier("LoadingMoreCell", forIndexPath: indexPath) as! LoadingMoreCell
               
                    loadingcell.setEmptyState(false)
                return loadingcell;
            }else{
                
                let cell = tableView.dequeueReusableCellWithIdentifier("projectcell", forIndexPath: indexPath) as! ProjectViewCell
                let item =  projects[indexPath.row]
                cell.mProjectName.text = item.name
                cell.mType.text = item.type
                cell.mStatus.text = item.status
                cell.mCreateAt.text = item.createAt
                cell.mCreateBy.text = item.createBy
                return cell;
            
            }
        
        }
       
    }
    internal  func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath){
       
        self.loadingcell = tableView.cellForRowAtIndexPath(indexPath) as? LoadingMoreCell
        if self.loadingcell != nil {
            loadingcell.setLoading(true)
            getProjectList()
            return
        }
    
        let projectStoryboard: UIStoryboard = UIStoryboard(name: "project", bundle: nil)
            
        let projectTabs = projectStoryboard.instantiateViewControllerWithIdentifier("TabProjectViewController") as! TabProjectViewController
        self.navigationController?.pushViewController(projectTabs, animated: true)
    
    }
    internal func searchBarCancelButtonClicked(searchBar: UISearchBar) {
        self.projects.removeAll();
        self.indicator.startAnimating()
        self.getProjectList()
    }

    internal func searchBarSearchButtonClicked(searchBar: UISearchBar) {
        self.view.endEditing(true)
        self.projects.removeAll();
        self.indicator.startAnimating()
        self.getProjectList()
    }
    internal func updateSearchResultsForSearchController(searchController: UISearchController){
       
        
    }
    internal func getProjectList(){
        let token = IncoCommon.getToken() as String
          print("token: \(token)")
        let parameters:[String:AnyObject] = [IncoApi.TOKEN_PARAMETER:token,
                                             IncoApi.OFFSET_PARAMETER:self.projects.count,
                                             IncoApi.LIMIT_PARAMETER :IncoCommon.NUMBER_ITEMS_LOADING,
                                             IncoApi.SEARCH_PARAMETER :self.mSearchBar.text!
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
                    self.indicator.stopAnimating()
                    self.indicator.hidesWhenStopped = true
                  
                    if self.loadingcell != nil{
                        self.loadingcell.setLoading(false)
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
