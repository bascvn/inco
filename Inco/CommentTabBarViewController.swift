//
//  CommentTabBarViewController.swift
//  Inco
//
//  Created by admin on 7/31/16.
//  Copyright © 2016 Ban Sac. All rights reserved.
//

import UIKit
import Alamofire
import AlamofireImage
class CommentTabBarViewController: UITabBarController {
    var add:UIBarButtonItem? = nil
    var send:UIBarButtonItem?
    var fileView:FilesTableViewController?
    var commentView:CommentViewController?
    var type = ComponentType.PROJECT
    var projectID = ""
    var id = ""
    var refreshDelegate:RefreshProtocol?
    override func viewDidLoad() {
        super.viewDidLoad()
        
        add = UIBarButtonItem(barButtonSystemItem: UIBarButtonSystemItem.Add,target:self,action: #selector(CommentTabBarViewController.selectFile))
        send = UIBarButtonItem(image: UIImage(named: "ic_send_white"), style: .Plain, target: self, action:#selector(CommentTabBarViewController.sendComment))
        navigationItem.rightBarButtonItems = [send!]
       

        // Do any additional setup after loading the view.
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    func showAlertEmptyData() {
        let alertController = UIAlertController(title: CommonMess.ALERT, message: CommonMess.DATA_EMPTY, preferredStyle: UIAlertControllerStyle.Alert)
        alertController.addAction(UIAlertAction(title: "OK", style:UIAlertActionStyle.Default, handler: nil))
        self.presentViewController(alertController, animated: true, completion: nil)
    }
    func showAlertFileUploading()  {
        let alertController = UIAlertController(title: CommonMess.ALERT, message: CommonMess.FILE_UPLOADING, preferredStyle: UIAlertControllerStyle.Alert)
        alertController.addAction(UIAlertAction(title: "OK", style:UIAlertActionStyle.Default, handler: nil))
        self.presentViewController(alertController, animated: true, completion: nil)
    }
    func getCommentApi(type:ComponentType) -> String {
        var url = ""
        switch type {
            case ComponentType.PROJECT :
                url = IncoApi.getApi(IncoApi.API_ADD_COMMENT_OF_PROJECT)
            case ComponentType.TASKS:
                url = IncoApi.getApi(IncoApi.API_ADD_COMMENT_OF_TASK)
            case ComponentType.TICKET:
                url = IncoApi.getApi(IncoApi.API_ADD_COMMENT_OF_TICKET)
            case ComponentType.DISCUSSTION:
                url = IncoApi.getApi(IncoApi.API_ADD_COMMENT_OF_DISCUSS)
        }
        return url
    }
    func getParameterApi(type:ComponentType,id:String,projectID:String,userId:String) -> [String:AnyObject] {
        let token = IncoCommon.getToken() as String
        var parameters:[String:AnyObject] =  [:]
        parameters[IncoApi.TOKEN_PARAMETER] = token
        switch type {
        case ComponentType.PROJECT :
            parameters[IncoApi.ADD_PROJECT_COMM_DES] = commentView?.mComment.text
            parameters[IncoApi.ADD_PROJECT_COMM_ID] =  projectID
            parameters[IncoApi.ADD_COMMENT_PRO_ID] = projectID
            parameters[IncoApi.ADD_PROJECT_COMM_BY] = userId
            print("projectID =\( parameters[IncoApi.ADD_PROJECT_COMM_ID] )")
            print("userId =\(parameters[IncoApi.ADD_COMMENT_PRO_ID])")
            print("token =\(parameters[IncoApi.TOKEN_PARAMETER])")

        case ComponentType.TASKS:
            parameters[IncoApi.ADD_TASK_COMM_BY] = userId
            parameters[IncoApi.ADD_TASK_ID] = id
            parameters[IncoApi.ADD_TASK_COMM_ID] = id
            parameters[IncoApi.ADD_TASK_COMM_DES] = commentView?.mComment.text
            parameters[IncoApi.ADD_COMMENT_PRO_ID] = projectID
            
        case ComponentType.TICKET:
            parameters[IncoApi.ADD_TICKET_COMM_BY] = userId
            parameters[IncoApi.ADD_TICKET_COMM_ID] = id
            parameters[IncoApi.ADD_TICKET_ID] = id
            parameters[IncoApi.ADD_TICKET_DES] = commentView?.mComment.text
            parameters[IncoApi.ADD_COMMENT_PRO_ID] = projectID

            
        case ComponentType.DISCUSSTION:
            parameters[IncoApi.ADD_DISCUSS_COMM_BY] = userId
            parameters[IncoApi.ADD_DISCUSS_COM_ID] = id
            parameters[IncoApi.ADD_DISCUSS_ID] = id
            parameters[IncoApi.ADD_DISCUSS_DES] = commentView?.mComment.text
            parameters[IncoApi.ADD_COMMENT_PRO_ID] = projectID
        }
        for item  in (fileView?.uploadlist)! {
            if item.status == UploadStatus.OK {
                print("attachments_info[\(item.id)]")
                print("item.info[\(item.info)]")
                parameters["attachments_info["+item.id!+"]"] = item.info
            }
        }
        return parameters

    }
    
    func sendComment(){
        if commentView?.isEmptyData() == true && fileView?.isEmptyData() == true {
            showAlertEmptyData()
            return
        }
        if fileView?.isFilesUploadFinish() == false {
            showAlertFileUploading()
            return
        }
        let alertController = UIAlertController(title: nil, message: "Please wait\n\n", preferredStyle: UIAlertControllerStyle.Alert)
        
        let spinnerIndicator: UIActivityIndicatorView = UIActivityIndicatorView(activityIndicatorStyle: UIActivityIndicatorViewStyle.WhiteLarge)
        
        spinnerIndicator.center = CGPointMake(135.0, 65.5)
        spinnerIndicator.color = UIColor.blackColor()
        spinnerIndicator.startAnimating()
        
        alertController.view.addSubview(spinnerIndicator)
        self.presentViewController(alertController, animated: false, completion: nil)
        
        let userId = IncoCommon.getUserId()
        let parameters = self.getParameterApi(self.type, id: self.id, projectID: self.projectID, userId: userId)
        Alamofire.request(.POST, self.getCommentApi(self.type),parameters: parameters) .responseJSON { response in // 1
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
            
            self.navigationController?.popViewControllerAnimated(true)
            self.refreshDelegate?.refresh()
            return
        }

        
    }
    func selectFile()  {
        
        self.fileView!.chooseFileUpload()
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
