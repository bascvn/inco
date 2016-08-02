//
//  CommentTabBarViewController.swift
//  Inco
//
//  Created by admin on 7/31/16.
//  Copyright © 2016 Ban Sac. All rights reserved.
//

import UIKit

class CommentTabBarViewController: UITabBarController {
    var add:UIBarButtonItem? = nil
    var send:UIBarButtonItem?
    override func viewDidLoad() {
        super.viewDidLoad()
        
        add = UIBarButtonItem(barButtonSystemItem: UIBarButtonSystemItem.Add,target:self,action: #selector(CommentTabBarViewController.selectFile))
        send = UIBarButtonItem(image: UIImage(named: "ic_send_white"), style: .Plain, target: self, action:nil)
        navigationItem.rightBarButtonItems = [send!]

        // Do any additional setup after loading the view.
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
  
    func selectFile()  {
        let fileView = self.viewControllers![1] as? FilesTableViewController
        fileView!.chooseFileUpload()
    }
    override func tabBar(tabBar: UITabBar, didSelectItem item: UITabBarItem) {
        if item.tag == 1 {
             navigationItem.rightBarButtonItems = [send!]
        }else{
            navigationItem.rightBarButtonItems = [send!,add!]
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
