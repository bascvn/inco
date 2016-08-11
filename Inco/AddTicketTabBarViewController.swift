//
//  AddTicketTabBarViewController.swift
//  Inco
//
//  Created by admin on 8/4/16.
//  Copyright © 2016 Ban Sac. All rights reserved.
//

import UIKit
import Alamofire
import AlamofireImage

class AddTicketTabBarViewController: UITabBarController {
    var add:UIBarButtonItem? = nil
    var send:UIBarButtonItem?
    var btnBack:UIBarButtonItem?

    var contentView:ContentTicketViewController?
    var configView:ConfigTicketTableViewController?
    var fileView:FilesTableViewController?
    var refreshDelegate:RefreshProtocol?
    var projectID = ""
    override func viewDidLoad() {
        super.viewDidLoad()
        self.title = CommonMess.NEW_TICKET
        add = UIBarButtonItem(barButtonSystemItem: UIBarButtonSystemItem.Add,target:self,action: #selector(AddTicketTabBarViewController.selectFile))
        send = UIBarButtonItem(image: UIImage(named: "ic_send_white"), style: .Plain, target: self, action:#selector(AddTicketTabBarViewController.sendTicket))
        navigationItem.rightBarButtonItems = [send!]
        btnBack = UIBarButtonItem(image: UIImage(named: "ic_clear_white"), style: .Plain, target: self, action:#selector(AddTicketTabBarViewController.backButtonClick))
        self.navigationItem.hidesBackButton = true
        self.navigationItem.leftBarButtonItem = btnBack
        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    func selectFile()  {
        
        self.fileView!.chooseFileUpload()
    }
    override func tabBar(tabBar: UITabBar, didSelectItem item: UITabBarItem) {
        if item.tag == 3 {
            navigationItem.rightBarButtonItems = [send!,add!]
        }else{
            navigationItem.rightBarButtonItems = [send!]
        }
    }
    func backButtonClick()  {
        if self.fileView?.isEmptyData() == false || self.contentView?.isEmptyData() == false {
            let callActionHandler = { (action:UIAlertAction!) -> Void in
                self.navigationController?.popViewControllerAnimated(true)
                
            }
            let alertController = UIAlertController(title: CommonMess.ALERT, message: CommonMess.DISCARD_CHANGE, preferredStyle: UIAlertControllerStyle.Alert)
            alertController.addAction(UIAlertAction(title: CommonMess.NO, style:UIAlertActionStyle.Cancel, handler: nil))
            alertController.addAction(UIAlertAction(title: CommonMess.OK, style:UIAlertActionStyle.Default, handler:callActionHandler ))
            self.presentViewController(alertController, animated: true, completion: nil)
        }
        self.navigationController?.popViewControllerAnimated(true)
    }
    func showAler(mess: String ) {
        let alertController = UIAlertController(title: CommonMess.ALERT, message: mess, preferredStyle: UIAlertControllerStyle.Alert)
        alertController.addAction(UIAlertAction(title: "OK", style:UIAlertActionStyle.Default, handler: nil))
        self.presentViewController(alertController, animated: true, completion: nil)
    }
    func showAlertFileUploading()  {
        let alertController = UIAlertController(title: CommonMess.ALERT, message: CommonMess.FILE_UPLOADING, preferredStyle: UIAlertControllerStyle.Alert)
        alertController.addAction(UIAlertAction(title: "OK", style:UIAlertActionStyle.Default, handler: nil))
        self.presentViewController(alertController, animated: true, completion: nil)
    }
    func getParameterApi() -> [String:AnyObject] {
        let token = IncoCommon.getToken() as String
        var parameters:[String:AnyObject] =  [:]
        parameters[IncoApi.TOKEN_PARAMETER] = token
        parameters[IncoApi.NEW_TICKET_COM_ID] = self.projectID
        parameters[IncoApi.NEW_TICKET_DEPA_ID] = self.configView?.getDeparmentId()
        parameters[IncoApi.NEW_TICKET_TYPE_ID] = self.configView?.getTypeId()
        parameters[IncoApi.NEW_TICKET_STATUS_ID] = self.configView?.getStatusId()
        parameters[IncoApi.NEW_TIECKT_USER] = IncoCommon.getUserId()
        parameters[IncoApi.NEW_TICKET_NAME] = self.contentView?.mSubject.text
        parameters[IncoApi.NEW_TICKET_DES] = self.contentView?.mDiscription.text
        parameters[IncoApi.ADD_COMMENT_PRO_ID] = self.self.projectID

        for item  in (fileView?.uploadlist)! {
            if item.status == UploadStatus.OK {
                print("attachments_info[\(item.id)]")
                print("item.info[\(item.info)]")
                parameters["attachments_info["+item.id!+"]"] = item.info
            }
        }
        return parameters
        
    }

    func sendTicket() {
        if self.contentView?.isSubjectEmpty() == true {
            self.showAler(CommonMess.SUBJECT_EPMPTY)
            self.selectedIndex = 0
            return
        }
        if self.contentView?.isDiscriptionEmpty() == true {
            self.showAler(CommonMess.DISCRIPTION_EMPTY)
            self.selectedIndex = 0

            return
        }
        if self.fileView?.isFilesUploadFinish() == false {
            self.showAlertFileUploading()
            self.selectedIndex = 2

            return
        }
        if self.configView?.isConfig == false {
            self.showAler(CommonMess.CONFIG_TICKET)
            self.selectedIndex = 1

            return
        }
        let alertController = UIAlertController(title: nil, message: "Please wait\n\n", preferredStyle: UIAlertControllerStyle.Alert)
        
        let spinnerIndicator: UIActivityIndicatorView = UIActivityIndicatorView(activityIndicatorStyle: UIActivityIndicatorViewStyle.WhiteLarge)
        
        spinnerIndicator.center = CGPointMake(135.0, 65.5)
        spinnerIndicator.color = UIColor.blackColor()
        spinnerIndicator.startAnimating()
        
        alertController.view.addSubview(spinnerIndicator)
        self.presentViewController(alertController, animated: false, completion: nil)
        
        let parameters = self.getParameterApi()
        var url = IncoApi.getApi(IncoApi.API_NEW_TICKET)
        var count = 0
        for item in (self.configView?.getNotifyID())! {
            if count  == 0 {
                url = url + "?" + IncoApi.EXTRA_NOTIFY + "=" + item
            }else{
                url = url+"&" + IncoApi.EXTRA_NOTIFY + "=" + item

            }
            count  = count+1
        }
        Alamofire.request(.POST, url ,parameters: parameters) .responseJSON { response in // 1
            print(response.request)  // original URL request
            print(response.response) // URL response
            print(response.data)     // server data
            print(response.result)   // result of response serialization
            
            if let JSON = response.result.value {
                print("JSON: \(JSON)")
                let inco = IncoResponse(data: JSON as! NSDictionary)
                if inco.isOK(){
                    
                }
                
            }
             alertController.dismissViewControllerAnimated(false, completion: nil)
            self.refreshDelegate?.refresh()
            self.navigationController?.popViewControllerAnimated(true)
            
            return
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
