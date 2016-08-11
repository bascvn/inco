//
//  ProjectTabBarViewController.swift
//  Inco
//
//  Created by admin on 7/31/16.
//  Copyright © 2016 Ban Sac. All rights reserved.
//

import UIKit

class ProjectTabBarViewController: UITabBarController,RefreshProtocol{
    var btnrefresh:UIBarButtonItem? = nil
    var detail:UIBarButtonItem? = nil
    var addTicketBtn:UIBarButtonItem? = nil
    var item:ProjectCell?
    var taskview:MainViewController?
    var ticketView:MainViewController?
    var discussionView:MainViewController?
    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationItem.backBarButtonItem = UIBarButtonItem(title: "", style: .Plain, target: nil, action: nil)
        btnrefresh = UIBarButtonItem(barButtonSystemItem: UIBarButtonSystemItem.Refresh,target:self,action:#selector(ProjectTabBarViewController.refreshView))
        detail = UIBarButtonItem(image: UIImage(named: "ic_description"), style: .Plain, target: self, action: #selector(ProjectTabBarViewController.showDetailProject))
       // addTicketBtn = UIBarButtonItem(barButtonSystemItem: UIBarButtonSystemItem.Edit,target:self,action:#selector(ProjectTabBarViewController.addTicket));
          addTicketBtn = UIBarButtonItem(image: UIImage(named: "ic_create_white_36pt"), style: .Plain, target: self, action:#selector(ProjectTabBarViewController.addTicket))
     //   detail = UIBarButtonItem(title: "Detail", style: .Plain, target: self, action: nil)
     //   detail.
        navigationItem.rightBarButtonItems = [btnrefresh!,detail!]

        // Do any additional setup after loading the view.
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    func refreshView() {
        if self.tabBar.selectedItem?.tag == 1 {
            self.taskview?.refresh()
        } else if self.tabBar.selectedItem?.tag == 2{
            self.ticketView?.refresh()
        }else{
            self.discussionView?.refresh()
        }
    }
    func addTicket()  {
        let tabBarController = AddTicketTabBarViewController()
        let ticketStoryboard: UIStoryboard = UIStoryboard(name: "ticket", bundle: nil)
        let config = ticketStoryboard.instantiateViewControllerWithIdentifier("ConfigTicketTableViewController") as! ConfigTicketTableViewController
        config.projectId = self.item?.id
        let content = ContentTicketViewController(nibName: "ContentTicketViewController", bundle: nil)
        let commentStoryboard: UIStoryboard = UIStoryboard(name: "addcomment", bundle: nil)

        let file = commentStoryboard.instantiateViewControllerWithIdentifier("FilesTableViewController") as! FilesTableViewController
        let controllers = [content,config,file]
        tabBarController.configView = config
        tabBarController.contentView = content
        tabBarController.refreshDelegate = self
        tabBarController.fileView = file
        file.type = ComponentType.NEW_TICKET
        tabBarController.projectID = (self.item?.id)!
        tabBarController.viewControllers = controllers
        let firstImage = UIImage(named: "tabs_ticket")
        let secondImage = UIImage(named: "ic_settings_white")
        let threeImage = UIImage(named: "ic_attachment_white")
        
        content.tabBarItem = UITabBarItem(
            title: "",
            image: firstImage,
            tag: 1)
        config.tabBarItem = UITabBarItem(
            title: "",
            image: secondImage,
            tag:2)
        file.tabBarItem = UITabBarItem(
            title: "",
            image: threeImage,
            tag:3)
        
        self.navigationController?.pushViewController(tabBarController, animated: true)
    }
    func showDetailProject() {
        let tabBarController = DetailTabBarViewController()
        let projectStoryboard: UIStoryboard = UIStoryboard(name: "detailcomponent", bundle: nil)
        let detail = projectStoryboard.instantiateViewControllerWithIdentifier("DetailProjectTableViewController") as! DetailProjectTableViewController
        detail.item = self.item
        let comment = projectStoryboard.instantiateViewControllerWithIdentifier("CommentsTableViewController") as! CommentsTableViewController
        comment.type = ComponentType.PROJECT
        comment.id = (self.item?.id)!
        tabBarController.type  = ComponentType.PROJECT
        tabBarController.iD = (self.item?.id)!
        tabBarController.projectID = (self.item?.id)!
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
        //tabBarController.title = (self.projects[indexPath.row] ).name

    }
    func refresh() {
        if self.tabBar.selectedItem?.tag == 2 {
            self.ticketView!.refresh()
        }
        
    }
    override func tabBar(tabBar: UITabBar, didSelectItem item: UITabBarItem) {
        if item.tag == 2 {
            navigationItem.rightBarButtonItems = [btnrefresh!,addTicketBtn!,detail!]
        }else{
            navigationItem.rightBarButtonItems = [btnrefresh!,detail!]
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
