//
//  ContentTicketViewController.swift
//  Inco
//
//  Created by admin on 8/3/16.
//  Copyright © 2016 Ban Sac. All rights reserved.
//

import UIKit

class ContentTicketViewController: UIViewController {

    @IBOutlet weak var mSubject: UITextField!
    @IBOutlet weak var mDiscription: UITextView!
    override func viewDidLoad() {
        super.viewDidLoad()
        self.addDoneButtonOnKeyboard()
        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    func isSubjectEmpty() -> Bool {
        if self.mSubject.text?.characters.count == 0 {
            return true
        }
        return false
    }
    func isDiscriptionEmpty() -> Bool {
        if self.mDiscription.text?.characters.count == 0 {
            return true
        }
        return false
    }
    func addDoneButtonOnKeyboard()
    {
        let doneToolbar: UIToolbar = UIToolbar(frame: CGRectMake(0, 0, 320, 50))
        doneToolbar.barStyle = UIBarStyle.BlackTranslucent
        
        let flexSpace = UIBarButtonItem(barButtonSystemItem: UIBarButtonSystemItem.FlexibleSpace, target: nil, action: nil)
        let done: UIBarButtonItem = UIBarButtonItem(title: "Done", style: UIBarButtonItemStyle.Done, target: self, action: #selector(ContentTicketViewController.doneButtonAction))
        
 
        doneToolbar.items = [flexSpace,done]
        doneToolbar.sizeToFit()
        
        self.mSubject.inputAccessoryView = doneToolbar
        self.mDiscription.inputAccessoryView = doneToolbar
        
    }
    
    func doneButtonAction()
    {
        self.mSubject.resignFirstResponder()
        self.mDiscription.resignFirstResponder()
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
