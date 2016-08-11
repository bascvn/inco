//
//  MainViewController.swift
//  Inco
//
//  Created by admin on 7/18/16.
//  Copyright © 2016 Ban Sac. All rights reserved.
//

import UIKit
import Alamofire

class MainViewController: UIViewController,UITableViewDelegate,UITableViewDataSource,UISearchResultsUpdating, UISearchBarDelegate,UISearchDisplayDelegate,RefreshProtocol{
    var isSetTitle = true
    var isCheckStatus = false
    var projects = [BaseComponentCell]()
    var loadingcell :LoadingMoreCell!
    var type:ComponentType = ComponentType.PROJECT
    var projectId:String = ""
    var refreshControl = UIRefreshControl()
    let indicator:UIActivityIndicatorView = UIActivityIndicatorView  (activityIndicatorStyle: UIActivityIndicatorViewStyle.WhiteLarge)
    var leftBarButton: ENMBadgedBarButtonItem?

    var count = 10
    @IBOutlet weak var mSearchBar: UISearchBar!
    @IBOutlet weak var mTableview: UITableView!
    override func viewDidLoad() {
        super.viewDidLoad()
        // set title
        
        if self.isCheckStatus == true {
            self.checkCompanyStatus()
        
        
        }
        self.setUpLeftBarButton()
        //self.hideKeyboardWhenTappedAround()
        // loading state
        indicator.color = UIColor(red: 141.0/255.0, green: 184.0/255.0, blue: 61.0/255.0, alpha: 1.0)

        indicator.frame = CGRectMake(0.0, 0.0, 30.0, 30.0)
        indicator.center = self.view.center
        self.view.addSubview(indicator)
        indicator.bringSubviewToFront(self.view)
        indicator.startAnimating()
        self.refreshControl.attributedTitle = NSAttributedString(string: "Pull to refresh")
        self.refreshControl.addTarget(self, action: #selector(MainViewController.refresh(_:)), forControlEvents: UIControlEvents.ValueChanged)
        self.mTableview?.addSubview(refreshControl)
       // self.mTableview.estimatedRowHeight = 44
        //self.mTableview.rowHeight = UITableViewAutomaticDimension
        

        // register loading more
         mTableview.registerNib(UINib(nibName: "LoadingMoreCell", bundle: nil), forCellReuseIdentifier: "LoadingMoreCell")
         mTableview.registerNib(UINib(nibName: "TaskCellTableViewCell", bundle: nil), forCellReuseIdentifier: "TaskCellTableViewCell")
        mTableview.registerNib(UINib(nibName: "TicketTableViewCell", bundle: nil), forCellReuseIdentifier: "TicketTableViewCell")
         mTableview.registerNib(UINib(nibName: "DiscussionTableViewCell", bundle: nil), forCellReuseIdentifier: "DiscussionTableViewCell")
        mTableview.tableFooterView = UIView(frame: CGRectZero)
        //
        //self.mTableview.rowHeight = UITableViewAutomaticDimension
       // self.mTableview.estimatedRowHeight = 200
        getProjectList()
        // Do any additional setup after loading the view.
    }
    override func viewDidAppear(animated: Bool) {
        if self.isSetTitle == true {
            self.setTitleView(type)
        }
    }
    override func viewDidDisappear(animated: Bool) {
        self.title = ""
    }
    @IBAction func refreshClick(sender: AnyObject) {
        refresh()
    }
    func refresh(){
           indicator.startAnimating()
           self.projects.removeAll()
           self.getProjectList()
    }
    func refresh(sender:AnyObject) {
        self.refresh()
        self.refreshControl.endRefreshing()
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
             return self.createCell(type, item: self.projects[indexPath.row], tableView: tableView, cellForRowAtIndexPath: indexPath)
          
        }else {
            if indexPath.row == projects.count {
                let loadingcell = tableView.dequeueReusableCellWithIdentifier("LoadingMoreCell", forIndexPath: indexPath) as! LoadingMoreCell
               
                    loadingcell.setEmptyState(false)
                return loadingcell;
            }else{
               return self.createCell(type, item: self.projects[indexPath.row], tableView: tableView, cellForRowAtIndexPath: indexPath)
            
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
        switch self.type {
            case ComponentType.TASKS,ComponentType.TICKET,ComponentType.DISCUSSTION:
                let tabBarController = DetailTabBarViewController()
                tabBarController.title = (self.projects[indexPath.row] ).name
                tabBarController.type = self.type
                tabBarController.iD = self.projects[indexPath.row].id!
                let projectStoryboard: UIStoryboard = UIStoryboard(name: "detailcomponent", bundle: nil)
                var detail:UITableViewController
                if type == ComponentType.TASKS {
                    detail = projectStoryboard.instantiateViewControllerWithIdentifier("DetailTableViewController") as! DetailTableViewController
                      (detail as! DetailTableViewController).item = self.projects[indexPath.row] as? TaskCell
                    tabBarController.projectID = ((self.projects[indexPath.row] as? TaskCell)?.projects_id)!

                }else if type == ComponentType.TICKET {
                      detail = projectStoryboard.instantiateViewControllerWithIdentifier("DetailTicketTableViewController") as! DetailTicketTableViewController
                    (detail as! DetailTicketTableViewController).item = self.projects[indexPath.row] as? TicketCell
                    tabBarController.projectID = ((self.projects[indexPath.row] as? TicketCell)?.projects_id)!

                }else if type == ComponentType.DISCUSSTION{
                     detail = projectStoryboard.instantiateViewControllerWithIdentifier("DetailDiscussionTableViewController") as! DetailDiscussionTableViewController
                     (detail as! DetailDiscussionTableViewController).item = self.projects[indexPath.row] as? DiscussionCell
                    tabBarController.projectID = ((self.projects[indexPath.row] as? DiscussionCell)?.projects_id)!

                }else{
                        detail = projectStoryboard.instantiateViewControllerWithIdentifier("DetailProjectTableViewController") as! DetailProjectTableViewController
                    tabBarController.projectID = self.projects[indexPath.row].id!
                }
              
                let comment =   projectStoryboard.instantiateViewControllerWithIdentifier("CommentsTableViewController") as! CommentsTableViewController 
                comment.type = self.type
                comment.id = self.projects[indexPath.row].id!
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
            return
        default: break
            
        
        }
        let tabBarController = ProjectTabBarViewController()
        tabBarController.item = self.projects[indexPath.row] as? ProjectCell

        tabBarController.title = (self.projects[indexPath.row] ).name
        let tasks = self.storyboard?.instantiateViewControllerWithIdentifier("MainViewController")
            as! MainViewController
            tasks.type = ComponentType.TASKS
        tasks.isSetTitle = false
        let tickets = self.storyboard?.instantiateViewControllerWithIdentifier("MainViewController")
            as! MainViewController
        tickets.type = ComponentType.TICKET
        tickets.isSetTitle = false

        let discussions = self.storyboard?.instantiateViewControllerWithIdentifier("MainViewController")
            as! MainViewController
            tasks.projectId = (self.projects[indexPath.row]).id!
        
            tickets.projectId = (self.projects[indexPath.row] ).id!
            discussions.projectId = (self.projects[indexPath.row] ).id!

        discussions.isSetTitle = false

        discussions.type = ComponentType.DISCUSSTION
        discussions.title = ""
        let controllers = [tasks,tickets,discussions]
        tabBarController.viewControllers = controllers
        tabBarController.taskview = tasks
        tabBarController.ticketView = tickets
        tabBarController.discussionView = discussions
        let firstImage = UIImage(named: "tabs_task")
        let secondImage = UIImage(named: "tabs_ticket")
        let threeImage = UIImage(named: "tabs_discusstion")
        tasks.tabBarItem = UITabBarItem(
            title: "",
            image: firstImage,
            tag: 1)
        tickets.tabBarItem = UITabBarItem(
            title: "",
            image: secondImage,
            tag:2)
        discussions.tabBarItem = UITabBarItem(
            title: "",
            image: threeImage,
            tag:3)
        self.navigationController?.pushViewController(tabBarController, animated: true)
        
    
    }
    internal func searchBarCancelButtonClicked(searchBar: UISearchBar) {
        self.projects.removeAll()
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
        var parameters:[String:AnyObject] = [IncoApi.TOKEN_PARAMETER:token,
                                             IncoApi.OFFSET_PARAMETER:self.projects.count,
                                             IncoApi.LIMIT_PARAMETER :IncoCommon.NUMBER_ITEMS_LOADING,
                                             IncoApi.SEARCH_PARAMETER :self.mSearchBar.text!
                                             ]
        if self.projectId != ""{
            parameters[IncoApi.PROJECT_ID_PARAMETER] = self.projectId
        }
        let url = self.getAPI(self.type)
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
                        self.addItemToList(item)
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
    func setTitleView(type:ComponentType)  {
        switch type {
        case ComponentType.PROJECT:
            self.title = CommonMess.PROJECT
        case ComponentType.TASKS:
            self.title = CommonMess.TASKS
        case ComponentType.TICKET:
            self.title = CommonMess.TICKETS
        case ComponentType.DISCUSSTION:
            self.title = CommonMess.DISCUSSIONS
        default:break

        }
    }
    func addItemToList(item:NSDictionary)  {
        switch type {
        case ComponentType.PROJECT:
            self.projects.append(ProjectCell.createCell(item))
        case ComponentType.TASKS:
            self.projects.append(TaskCell.createCell(item))
        case ComponentType.TICKET:
            self.projects.append(TicketCell.createCell(item))
        case ComponentType.DISCUSSTION:
            self.projects.append(DiscussionCell.createCell(item))
        default:break

        }
    }
    func getAPI(type: ComponentType) -> String {
        var url:String?
        switch type {
            case ComponentType.PROJECT:
                 url = IncoApi.getApi(IncoApi.API_PROJECT_LIST)
            case ComponentType.TASKS:
                 url = IncoApi.getApi(IncoApi.API_TASK_LIST)
            case ComponentType.TICKET:
                 url = IncoApi.getApi(IncoApi.API_TICKET_LIST)
            case ComponentType.DISCUSSTION:
                url = IncoApi.getApi(IncoApi.API_DISCUSSION_LIST)
        default:break

        }
        return url!
    }
    func createCell(type:ComponentType,item:BaseComponentCell,tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        var cell:UITableViewCell
        switch type {
        case ComponentType.PROJECT:
            cell  = tableView.dequeueReusableCellWithIdentifier("projectcell", forIndexPath: indexPath) as! ProjectViewCell
            let item =  item as! ProjectCell
            (cell as! ProjectViewCell).mProjectName.text = item.name
            (cell as! ProjectViewCell).mType.text = item.type
            (cell as! ProjectViewCell).mStatus.text = item.status
            (cell as! ProjectViewCell).mCreateAt.text = item.createAt
            (cell as! ProjectViewCell).mCreateBy.text = item.createBy
        
        case ComponentType.TASKS:
            cell  = tableView.dequeueReusableCellWithIdentifier("TaskCellTableViewCell", forIndexPath: indexPath) as! TaskCellTableViewCell
            let item =  item as! TaskCell
           (cell as! TaskCellTableViewCell).mTaskName.text = item.name
            (cell as! TaskCellTableViewCell).mStatus.text = item.tasks_status
            (cell as! TaskCellTableViewCell).mPriority.text = item.tasks_priority
            (cell as! TaskCellTableViewCell).mAssignTo.text = item.assigned_to
            (cell as! TaskCellTableViewCell).mProjectName.text = item.projects
        case ComponentType.TICKET:
            cell  = tableView.dequeueReusableCellWithIdentifier("TicketTableViewCell", forIndexPath: indexPath) as! TicketTableViewCell
            let item =  item as! TicketCell
            (cell as! TicketTableViewCell).mProjectName.text = item.project
            (cell as! TicketTableViewCell).mStatus.text = item.tickets_status
            (cell as! TicketTableViewCell).mCreateBy.text = item.create_by
            (cell as! TicketTableViewCell).mDepartment.text = item.departments
            (cell as! TicketTableViewCell).mTicketName.text = item.name
        default :
            cell  = tableView.dequeueReusableCellWithIdentifier("DiscussionTableViewCell", forIndexPath: indexPath) as! DiscussionTableViewCell
            let item =  item as! DiscussionCell
            (cell as! DiscussionTableViewCell).mDiscussionName.text = item.name
            (cell as! DiscussionTableViewCell).mProjectName.text = item.projects
            (cell as! DiscussionTableViewCell).mCreateBy.text = item.create_by
            (cell as! DiscussionTableViewCell).mStatusName.text = item.status

    
        }
        return cell
    }
    func showErrorStatus(mess:String)  {
        let updateActionHandler = { (action:UIAlertAction!) -> Void in
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
        let alertController = UIAlertController(title: CommonMess.ALERT, message: mess, preferredStyle: UIAlertControllerStyle.Alert)

        alertController.addAction(UIAlertAction(title: CommonMess.OK, style:UIAlertActionStyle.Default, handler:updateActionHandler ))
        self.presentViewController(alertController, animated: true, completion: nil)
    }
    func showUpdateVersion(version:String,url:String,build:String ){
        let updateActionHandler = { (action:UIAlertAction!) -> Void in
            let urlString  = NSURL(string: url)
            if UIApplication.sharedApplication().canOpenURL(urlString!) {
                UIApplication.sharedApplication().openURL(urlString!)
            }
        }
        let cancelActionHandler = { (action:UIAlertAction!) -> Void in
            IncoCommon.setNumberBuild(build)
        }
        let mess = String(format: CommonMess.HAVE_NEW_VERSION, arguments: [version])
        let alertController = UIAlertController(title: CommonMess.ALERT, message: mess, preferredStyle: UIAlertControllerStyle.Alert)
        alertController.addAction(UIAlertAction(title: CommonMess.NO, style:UIAlertActionStyle.Cancel, handler: cancelActionHandler))
        alertController.addAction(UIAlertAction(title: CommonMess.UPDATE, style:UIAlertActionStyle.Default, handler:updateActionHandler ))
        self.presentViewController(alertController, animated: true, completion: nil)
    }
    func checkCompanyStatus(){
        
       let parameters = [IncoApi.CLIENT_CODE : IncoCommon.getClientID()]
        Alamofire.request(.POST, IncoApi.getStatusCompany(),parameters: parameters ) .responseJSON { response in // 1
            print(response.request)  // original URL request
            print(response.response) // URL response
            print(response.data)     // server data
            print("getStatusCompany \(response.result.value)")   // result of response serialization
            
            if let JSON = response.result.value {
                let status = JSON.valueForKey("status") as! Int
                if status == 200 {
                    let data = JSON.valueForKey("data") as! NSDictionary
                    let cpnStatus = data.valueForKey("Status") as! String
                    if  cpnStatus == "0" {
                        var mess = data.valueForKey("msg") as? String
                        if mess == nil {
                            mess = ""
                        }
                        self.showErrorStatus(mess!)
                        return
                    }
                    let buildNumber = data.valueForKey("BuildNumber") as! String
                    let currentBuild = NSBundle.mainBundle().infoDictionary?["CFBundleVersion"] as? String
                    if Int(buildNumber) > Int(currentBuild!)! && IncoCommon.getNumberBuild() != buildNumber{
                        var version = data.valueForKey("ios_ver") as? String
                        if version == nil {
                            version = ""
                        }
                        var url = data.valueForKey("url_ios") as? String
                        if url == nil {
                            url = ""
                        }
                        self.showUpdateVersion(version!,url: url!,build: buildNumber)
                    }
                    
                }
                       
            }
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

extension MainViewController {
    
    func setUpLeftBarButton() {
        let image = UIImage(named: "ic_public_white")
        let button = UIButton(type: .Custom)
        if let knownImage = image {
            button.frame = CGRectMake(0.0, 0.0, knownImage.size.width, knownImage.size.height)
        } else {
            button.frame = CGRectZero;
        }
        
        button.setBackgroundImage(image, forState: UIControlState.Normal)
        button.addTarget(self,
                         action: #selector(MainViewController.leftButtonPressed(_:)),
                         forControlEvents: UIControlEvents.TouchUpInside)
        
        let newBarButton = ENMBadgedBarButtonItem(customView: button, value: "\(count)")
        leftBarButton = newBarButton
        leftBarButton?.badgeValue = "\(count)"
        var menus = navigationItem.rightBarButtonItems
         menus?.insert(leftBarButton!, atIndex: 0)
        navigationItem.rightBarButtonItems = menus
    }
}

extension MainViewController {
    
    func leftButtonPressed(_sender: UIButton) {
        count = count + 1
        leftBarButton?.badgeValue = "\(count)"
    }
    
    func rightButtonPressed(_sender: UIButton) {
        count = 0
        leftBarButton?.badgeValue = "0"
    }
}

