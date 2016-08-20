//
//  DetailTabBarViewController.swift
//  Inco
//
//  Created by admin on 7/27/16.
//  Copyright © 2016 Ban Sac. All rights reserved.
//

import UIKit

class DetailTabBarViewController: UITabBarController,RefreshProtocol{
    var add:UIBarButtonItem?
    var type:ComponentType = ComponentType.PROJECT
    var projectID = ""
    var iD = ""
    var commentView:CommentsTableViewController?
    var isNotificaiton = false

    override func viewDidLoad() {
        super.viewDidLoad()
     
      //  add = UIBarButtonItem(title: "Refresh", style: .Plain, target: self, action: nil)
        
        add = UIBarButtonItem(image: UIImage(named: "ic_create_white_36pt"), style: .Plain, target: self, action:#selector(DetailTabBarViewController.openAddComment))
        
      // navigationItem.rightBarButtonItem = add
        // Do any additional setup after loading the view.
    }
    override func viewDidAppear(animated: Bool) {
        if isNotificaiton == true{
            self.navigationItem.leftBarButtonItem =  UIBarButtonItem(image: UIImage(named: "ic_clear_white"), style: .Plain, target: self, action:#selector(DetailTabBarViewController.closeNotifications(_:)))
            
        }
    }
    func closeNotifications(sender: UIBarButtonItem) {
        self.dismissViewControllerAnimated(true, completion: nil)
    }
    func openAddComment() {
        let tabBarController = CommentTabBarViewController()
        tabBarController.refreshDelegate = self
        let projectStoryboard: UIStoryboard = UIStoryboard(name: "addcomment", bundle: nil)
       
        //let comment = CommentViewController(nibName: "CommentViewController", bundle: nil)
        //let file = FilesTableViewController(nibName: "FilesTableViewController", bundle: nil)
        let comment = projectStoryboard.instantiateViewControllerWithIdentifier("CommentViewController") as! CommentViewController
        let file = projectStoryboard.instantiateViewControllerWithIdentifier("FilesTableViewController") as! FilesTableViewController
        file.type = self.type

        let controllers = [comment,file]
        tabBarController.fileView = file
        tabBarController.commentView = comment
        tabBarController.id = self.iD
        tabBarController.projectID = self.projectID
        tabBarController.type = self.type
        tabBarController.viewControllers = controllers
        let firstImage = UIImage(named: "tabs_discusstion")
        let secondImage = UIImage(named: "ic_attachment_white")
        comment.tabBarItem = UITabBarItem(
            title: "",
            image: firstImage,
            tag: 1)
        file.tabBarItem = UITabBarItem(
            title: "",
            image: secondImage,
            tag:2)
        self.navigationController?.pushViewController(tabBarController, animated: true)
    }
    func refresh() {
        if self.tabBar.selectedItem?.tag == 2 {
            self.commentView!.refresh()
        }

    }
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    override func tabBar(tabBar: UITabBar, didSelectItem item: UITabBarItem) {
        if item.tag == 2{
            navigationItem.rightBarButtonItems = [add!]
        }else{
            navigationItem.rightBarButtonItems = []

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
