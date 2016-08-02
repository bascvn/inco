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
    var item:ProjectCell?
    override func viewDidLoad() {
        super.viewDidLoad()
        refresh = UIBarButtonItem(barButtonSystemItem: UIBarButtonSystemItem.Refresh,target:self,action:nil)
        detail = UIBarButtonItem(image: UIImage(named: "ic_description"), style: .Plain, target: self, action: #selector(ProjectTabBarViewController.showDetailProject))
     //   detail = UIBarButtonItem(title: "Detail", style: .Plain, target: self, action: nil)
     //   detail.
        navigationItem.rightBarButtonItems = [refresh!,detail!]

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    func showDetailProject() {
        let tabBarController = DetailTabBarViewController()
        let projectStoryboard: UIStoryboard = UIStoryboard(name: "detailcomponent", bundle: nil)
        let detail = projectStoryboard.instantiateViewControllerWithIdentifier("DetailProjectTableViewController") as! DetailProjectTableViewController
        detail.item = self.item
        let comment = CommentsTableViewController(nibName: "CommentsTableViewController", bundle: nil)
       comment.type = ComponentType.PROJECT
        comment.id = (self.item?.id)!
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
