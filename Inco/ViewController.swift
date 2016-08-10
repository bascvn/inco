//
//  ViewController.swift
//  Inco
//
//  Created by admin on 7/17/16.
//  Copyright © 2016 Ban Sac. All rights reserved.
//

import UIKit
import Alamofire
class ViewController: UIViewController,CompanyTableDelegate,UITextFieldDelegate{

   
    @IBOutlet weak var isLoging: UIActivityIndicatorView!
    @IBOutlet weak var mEmail: UITextField!
    
    @IBOutlet weak var mPassword: UITextField!
    
    @IBOutlet weak var mCompany: UITextField!
    
    @IBOutlet weak var mVersion: UILabel!
    
    @IBOutlet weak var mRemeber: UISwitch!
    var isResize = false
    var height:CGFloat  = 0
    override func viewDidLoad() {
        super.viewDidLoad()
        self.mEmail.delegate = self
        self.mCompany.delegate = self
        self.mPassword.delegate = self
        self.title = "Inco"
        self.hideKeyboardWhenTappedAround()
        // Do any additional setup after loading the view, typically from a nib.
        if IncoCommon.isRemember() == true{
           mRemeber.setOn(true, animated: true)
           let user = IncoCommon.getUsrInfo()! as NSDictionary
           // if user != nil{
                mCompany.text = IncoCommon.getClientID();
                mEmail.text = user.valueForKey("email") as? String
            //}
        }else{
            mRemeber.setOn(false, animated: true)

        }
        mVersion.text = IncoCommon.getVersion()
        self.isLoging.hidesWhenStopped = true
        self.isLoging.stopAnimating()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    @IBAction func searchCompany(sender: UIButton) {
        print("searchCompany")
        self.view.endEditing(true)

        let mainStoryboard: UIStoryboard = UIStoryboard(name: "Main", bundle: nil)

         let companyTableViewController = mainStoryboard.instantiateViewControllerWithIdentifier("CompanyTableViewController") as! CompanyTableViewController
        companyTableViewController.delegate = self
        self.navigationController?.pushViewController(companyTableViewController, animated: true)
    }

    @IBAction func loginAction(sender: AnyObject) {
        if(!self.validateForm()){
            return
        }
        self.isLoging.startAnimating()
        let email = self.mEmail.text!
        let pass = self.mPassword.text!
        let parameters:[String:AnyObject] = [IncoApi.LOGIN_DEVICE_ID:"ios device 1",IncoApi.LOGIN_DEVICE_TYPE:"2",
                                             IncoApi.LOGIN_PASS:pass,IncoApi.LOGIN_EMAIL:email]
        Alamofire.request(.POST, IncoApi.getLogin(self.mCompany.text!),parameters: parameters) .responseJSON { response in // 1
            print(response.request)  // original URL request
            print(response.response) // URL response
            print(response.data)     // server data
            print(response.result)   // result of response serialization
            
            if let JSON = response.result.value {
                print("JSON kien: \(JSON)")
                    let inco = IncoResponse(data: JSON as! NSDictionary)
                   
                    if inco.isOK(){
                        
                        let data = inco.data as [NSDictionary]
                        let token  = data[0].valueForKey(IncoResponse.M_TOKEN) as! String;
                        if token.characters.count > 0{
                            IncoCommon.saveToken(token)
                            IncoCommon.saveClientID(self.mCompany.text!)
                            IncoCommon.saveUserInfo(data[0].valueForKey("user") as! NSDictionary)
                            IncoCommon.setRemember(self.mRemeber.on)
                            self.loginSucess()
                            self.isLoging.stopAnimating()

                            return
                        }
                        
                    }
                    
                
                
            }
            self.isLoging.stopAnimating()

            self.showAlertBox(CommonMess.ALERT, mess: ErrorMess.LOGIN_ERROR)
            return
        }
        
      
        return
        

    }
    override func viewDidAppear(animated: Bool) {
        height = self.view.frame.height
        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(ViewController.keyboardWillShow(_:)), name: UIKeyboardWillShowNotification, object: nil)
        NSNotificationCenter.defaultCenter().addObserver(self, selector: #selector(ViewController.keyboardWillHide(_:)), name: UIKeyboardWillHideNotification, object: nil)
    }
    override func viewDidDisappear(animated: Bool) {
        NSNotificationCenter.defaultCenter().removeObserver(self)
    }
    func keyboardWillShow(notification: NSNotification) {
        
        if let keyboardSize = (notification.userInfo?[UIKeyboardFrameBeginUserInfoKey] as? NSValue)?.CGRectValue() {
          
                self.view.frame.size.height = height - keyboardSize.height
            
        }
        
    }
    
    func keyboardWillHide(notification: NSNotification) {
        if let keyboardSize = (notification.userInfo?[UIKeyboardFrameBeginUserInfoKey] as? NSValue)?.CGRectValue() {
           
                self.view.frame.size.height = height
            
        }
    }
    func loginSucess()  {
        
        let appDelegate: AppDelegate = UIApplication.sharedApplication().delegate as! AppDelegate
        
        let mainStoryboard: UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
        
        let centerViewController = mainStoryboard.instantiateViewControllerWithIdentifier("MainViewController") as! MainViewController
        
        let leftViewController = mainStoryboard.instantiateViewControllerWithIdentifier("LeftSideViewController") as! LeftSideViewController
        
        let leftSideNav = UINavigationController(rootViewController: leftViewController)
        let centerNav = UINavigationController(rootViewController: centerViewController)
        
        appDelegate.centerContainer = MMDrawerController (centerViewController: centerNav, leftDrawerViewController: leftSideNav)
        
        appDelegate.centerContainer!.openDrawerGestureModeMask = MMOpenDrawerGestureMode.PanningCenterView;
        appDelegate.centerContainer!.closeDrawerGestureModeMask = MMCloseDrawerGestureMode.PanningCenterView;
        
        appDelegate.window!.rootViewController = appDelegate.centerContainer
        appDelegate.window!.makeKeyAndVisible()
    }
    func showAlertBox(title: String,mess: String) {
        let alertController = UIAlertController(title: title, message: mess, preferredStyle: UIAlertControllerStyle.Alert)
        alertController.addAction(UIAlertAction(title: "OK", style:UIAlertActionStyle.Default, handler: nil))
        self.presentViewController(alertController, animated: true, completion: nil)
    }
    // check data login form
    func validateForm() -> Bool {
        let email: String    = self.mEmail.text!
        let password: String = self.mPassword.text!
        let company: String  = self.mCompany.text!
        
        if email.characters.count == 0{
            self.mEmail.becomeFirstResponder()
            self.showAlertBox(CommonMess.ALERT,mess: ErrorMess.EMAIL_EMPTY)
            return false;
        }
        if email.rangeOfString("@") == nil {
            self.mEmail.becomeFirstResponder()
            self.showAlertBox(CommonMess.ALERT,mess: ErrorMess.EMAIL_ERROR)
            return false;
        }
        if password.characters.count == 0{
            self.mPassword.becomeFirstResponder()
            self.showAlertBox(CommonMess.ALERT,mess: ErrorMess.PASSWORD_EMPTY)
            return false;
        }
        if company.characters.count == 0{
            self.mCompany.becomeFirstResponder()
            self.showAlertBox(CommonMess.ALERT,mess: ErrorMess.COMPANY_EMPTY)
            return false;
        }
        return true
    }
    func companyslected(clientId:String){
     self.mCompany.text = clientId
    }
    func textFieldShouldReturn(textField: UITextField) -> Bool {
        self.view.endEditing(true)
        return false
    }
    
}

