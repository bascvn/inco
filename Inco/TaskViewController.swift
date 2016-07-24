//
//  TaskViewController.swift
//  Inco
//
//  Created by admin on 7/24/16.
//  Copyright © 2016 Ban Sac. All rights reserved.
//

import UIKit

class TaskViewController: UIViewController,UITableViewDelegate,UITableViewDataSource,UISearchResultsUpdating, UISearchBarDelegate,UISearchDisplayDelegate {
    

    @IBOutlet weak var mTable: UITableView!
    override func viewDidLoad() {
        super.viewDidLoad()
        mTable.registerNib(UINib(nibName: "TaskCellTableViewCell", bundle: nil), forCellReuseIdentifier: "TaskCellTableViewCell")

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    internal func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int{
        
        return 10
        
    }
    internal func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell{
        
        
            let cell = tableView.dequeueReusableCellWithIdentifier("TaskCellTableViewCell", forIndexPath: indexPath) as! TaskCellTableViewCell
        
            return cell
    
        
    }
    internal  func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath){
       
        
    }
    internal func searchBarSearchButtonClicked(searchBar: UISearchBar) {
       
    }
    internal func updateSearchResultsForSearchController(searchController: UISearchController){
        
        
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
