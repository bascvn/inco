//
//  DetailTabBarViewController.swift
//  Inco
//
//  Created by admin on 7/27/16.
//  Copyright © 2016 Ban Sac. All rights reserved.
//

import UIKit

class DetailTabBarViewController: UITabBarController {
    var add:UIBarButtonItem?
    override func viewDidLoad() {
        super.viewDidLoad()
      //  add = UIBarButtonItem(title: "Refresh", style: .Plain, target: self, action: nil)
        
        add = UIBarButtonItem(barButtonSystemItem: UIBarButtonSystemItem.Add,target:self,action:#selector(DetailTabBarViewController.openAddComment))
      // navigationItem.rightBarButtonItem = add
        // Do any additional setup after loading the view.
    }
    func openAddComment() {
        let tabBarController = CommentTabBarViewController()
        
        let comment = CommentViewController(nibName: "CommentViewController", bundle: nil)
        let file = FilesTableViewController(nibName: "FilesTableViewController", bundle: nil)

        let controllers = [comment,file]
        tabBarController.viewControllers = controllers
        let firstImage = UIImage(named: "ic_description")
        let secondImage = UIImage(named: "tabs_discusstion")
        comment.tabBarItem = UITabBarItem(
            title: "",
            image: firstImage,
            tag: 1)
        file.tabBarItem = UITabBarItem(
            title: "",
            image: secondImage,
            tag:2)
        self.navigationController?.pushViewController(tabBarController, animated: false)
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
