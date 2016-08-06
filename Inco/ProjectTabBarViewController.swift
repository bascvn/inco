//
//  ProjectTabBarViewController.swift
//  Inco
//
//  Created by admin on 7/31/16.
//  Copyright © 2016 Ban Sac. All rights reserved.
//

import UIKit

class ProjectTabBarViewController: UITabBarController {
    var refresh:UIBarButtonItem? = nil
    var detail:UIBarButtonItem? = nil
    var addTicketBtn:UIBarButtonItem? = nil
    var item:ProjectCell?
    override func viewDidLoad() {
        super.viewDidLoad()
        refresh = UIBarButtonItem(barButtonSystemItem: UIBarButtonSystemItem.Refresh,target:self,action:nil)
        detail = UIBarButtonItem(image: UIImage(named: "ic_description"), style: .Plain, target: self, action: #selector(ProjectTabBarViewController.showDetailProject))
        addTicketBtn = UIBarButtonItem(barButtonSystemItem: UIBarButtonSystemItem.Add,target:self,action:#selector(ProjectTabBarViewController.addTicket));
     //   detail = UIBarButtonItem(title: "Detail", style: .Plain, target: self, action: nil)
     //   detail.
        navigationItem.rightBarButtonItems = [refresh!,addTicketBtn!,detail!]

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    func addTicket()  {
        let tabBarController = AddTicketTabBarViewController()
        let ticketStoryboard: UIStoryboard = UIStoryboard(name: "ticket", bundle: nil)
        let config = ticketStoryboard.instantiateViewControllerWithIdentifier("ConfigTicketTableViewController") as! ConfigTicketTableViewController
        config.projectId = self.item?.id
        let content = ContentTicketViewController(nibName: "ContentTicketViewController", bundle: nil)
        let file = FilesTableViewController(nibName: "CommentsTableViewController", bundle: nil)
        
        let controllers = [content,config,file]
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
            tag:2)
        
        self.navigationController?.pushViewController(tabBarController, animated: true)
    }
    func showDetailProject() {
        let tabBarController = DetailTabBarViewController()
        let projectStoryboard: UIStoryboard = UIStoryboard(name: "detailcomponent", bundle: nil)
        let detail = projectStoryboard.instantiateViewControllerWithIdentifier("DetailProjectTableViewController") as! DetailProjectTableViewController
        detail.item = self.item
        let comment = CommentsTableViewController(nibName: "CommentsTableViewController", bundle: nil)
       comment.type = ComponentType.PROJECT
        comment.id = (self.item?.id)!
        tabBarController.type  = ComponentType.PROJECT
        tabBarController.iD = (self.item?.id)!
        tabBarController.projectID = (self.item?.id)!
        let controllers = [detail,comment]
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

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
