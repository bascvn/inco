//
//  ContentTicketViewController.swift
//  Inco
//
//  Created by admin on 8/3/16.
//  Copyright © 2016 Ban Sac. All rights reserved.
//

import UIKit
import KMPlaceholderTextView

class ContentTicketViewController: UIViewController {

    @IBOutlet weak var mSubject: UITextField!
    @IBOutlet weak var mDiscription: KMPlaceholderTextView!
    var height:CGFloat  = 0

    override func viewDidLoad() {
        super.viewDidLoad()
        self.addDoneButtonOnKeyboard()
        self.mSubject.placeholder = CommonMess.SUBJECT
        self.mDiscription.placeholder = CommonMess.TYPE_YOUR_COMMENT
        // Do any additional setup after loading the view.
    }
    override func viewDidAppear(animated: Bool) {
        self.height = self.view.frame.height
        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(ContentTicketViewController.keyboardWillShow(_:)), name: UIKeyboardWillShowNotification, object: nil)
        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(ContentTicketViewController.keyboardWillHide(_:)), name: UIKeyboardWillHideNotification, object: nil)
    }
    override func viewDidDisappear(animated: Bool) {
        NSNotificationCenter.defaultCenter().removeObserver(self)
    }
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    func keyboardWillShow(notification: NSNotification) {
        
        if let keyboardSize = (notification.userInfo?[UIKeyboardFrameBeginUserInfoKey] as? NSValue)?.CGRectValue() {
            
            self.view.frame.size.height =  self.height - keyboardSize.height
        }
        
    }
    
    func keyboardWillHide(notification: NSNotification) {
        if let keyboardSize = (notification.userInfo?[UIKeyboardFrameBeginUserInfoKey] as? NSValue)?.CGRectValue() {
            self.view.frame.size.height  = self.height
        }
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
