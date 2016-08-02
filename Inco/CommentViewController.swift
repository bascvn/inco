//
//  CommentViewController.swift
//  Inco
//
//  Created by admin on 7/31/16.
//  Copyright © 2016 Ban Sac. All rights reserved.
//

import UIKit

class CommentViewController: UIViewController {

    @IBOutlet weak var mComment: UITextView!
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    func isEmptyData() -> Bool{
        if self.mComment.text.characters.count == 0 {
            return true
        }
        return false
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
