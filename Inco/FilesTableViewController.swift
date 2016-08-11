//
//  FilesTableViewController.swift
//  Inco
//
//  Created by admin on 7/31/16.
//  Copyright © 2016 Ban Sac. All rights reserved.
//

import UIKit
import Alamofire
import AlamofireImage
class FilesTableViewController: UITableViewController,UIImagePickerControllerDelegate, UINavigationControllerDelegate{
    var type = ComponentType.TASKS
    let imagePicker = UIImagePickerController()
    var uploadlist = [UploadFile]()
       var index = 1
    var doneToolbar: UIToolbar? = nil
    override func viewDidLoad() {
        super.viewDidLoad()
        self.addDoneButtonOnKeyboard()
        imagePicker.delegate = self
        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem()
         self.tableView.registerNib(UINib(nibName: "UploadFileCell", bundle: nil), forCellReuseIdentifier: "UploadFileCell")
        self.tableView.estimatedRowHeight = 44
        self.tableView.rowHeight = UITableViewAutomaticDimension
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    // MARK: - Table view data source

    override func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        // #warning Incomplete implementation, return the number of sections
        return 1
    }
    // this function creates the required URLRequestConvertible and NSData we need to use Alamofire.upload
    func urlRequestWithComponents(urlString:String, parameters:Dictionary<String, String>, imageData:NSData,imageName:String) -> (URLRequestConvertible, NSData) {
        
        // create url request to send
        let mutableURLRequest = NSMutableURLRequest(URL: NSURL(string: urlString)!)
        mutableURLRequest.HTTPMethod = Alamofire.Method.POST.rawValue
        let boundaryConstant = "myRandomBoundary12345";
        let contentType = "multipart/form-data;boundary="+boundaryConstant
        mutableURLRequest.setValue(contentType, forHTTPHeaderField: "Content-Type")
        
        
        
        // create upload data to send
        let uploadData = NSMutableData()
        // add parameters
        for (key, value) in parameters {
            uploadData.appendData("\r\n--\(boundaryConstant)\r\n".dataUsingEncoding(NSUTF8StringEncoding)!)
            uploadData.appendData("Content-Disposition: form-data; name=\"\(key)\"\r\n\r\n\(value)".dataUsingEncoding(NSUTF8StringEncoding)!)
        }
        // add image
        uploadData.appendData("\r\n--\(boundaryConstant)\r\n".dataUsingEncoding(NSUTF8StringEncoding)!)
        uploadData.appendData("Content-Disposition: form-data; name=\"Filedata\"; filename=\"\(imageName)\"\r\n".dataUsingEncoding(NSUTF8StringEncoding)!)
        uploadData.appendData("Content-Type: image/jpg\r\n\r\n".dataUsingEncoding(NSUTF8StringEncoding)!)
        uploadData.appendData(imageData)
        
        
        uploadData.appendData("\r\n--\(boundaryConstant)--\r\n".dataUsingEncoding(NSUTF8StringEncoding)!)
        
        
        
        // return URLRequestConvertible and NSData
        return (Alamofire.ParameterEncoding.URL.encode(mutableURLRequest, parameters: nil).0, uploadData)
    }
    func uploadFile(image:NSData,index:Int,name:String) {
        let token = IncoCommon.getToken() as String
        var parameters = [IncoApi.TOKEN_PARAMETER:token]
        if self.type == ComponentType.TASKS{
            parameters[IncoApi.TYPE_BIND_PARAMATER] = BindTypeUpload.TASK_COMMENT
        
        }else if self.type == ComponentType.PROJECT{
            parameters[IncoApi.TYPE_BIND_PARAMATER] = BindTypeUpload.PROJECT_COMMENT

        }else if self.type == ComponentType.TICKET{
            parameters[IncoApi.TYPE_BIND_PARAMATER] = BindTypeUpload.TICKET_COMMENT

        }else if self.type == ComponentType.DISCUSSTION{
            parameters[IncoApi.TYPE_BIND_PARAMATER] = BindTypeUpload.DISCUSSION_COMMENT

        }else{
            parameters[IncoApi.TYPE_BIND_PARAMATER] = BindTypeUpload.NEW_TICKET_FILE

        }

        let urlRequest = urlRequestWithComponents(IncoApi.getApi(IncoApi.API_UPLOAD_FILE), parameters: parameters, imageData: image,imageName: name)
        var preProgress:Float = 0.0
        Alamofire.upload(urlRequest.0, data: urlRequest.1)
            .progress { (bytesWritten, totalBytesWritten, totalBytesExpectedToWrite) in
                
                print("RESPONSE \(totalBytesWritten)")
                let pos = self.getFileByIndex(index)
               if pos == -1 {
                    return
               }
               self.uploadlist[pos].progress =  Float(totalBytesWritten) / Float(totalBytesExpectedToWrite)
                print("RESPONSE \(self.uploadlist[pos].progress)")
                if (self.uploadlist[pos].progress - preProgress ) > 0.2 {
                    preProgress = self.uploadlist[pos].progress
                    dispatch_async(dispatch_get_main_queue(), {
                        self.tableView.reloadData()
                    })

             
               }
                
            }
            .responseJSON { response in
                let pos = self.getFileByIndex(index)
                if let JSON = response.result.value {
                    print("JSON: \(JSON)")
                    let inco = IncoResponse(data: JSON as! NSDictionary)
                    if inco.isOK(){
                        self.uploadlist[pos].fileName = inco.data[0].valueForKey("fileName") as?String
                        self.uploadlist[pos].id = inco.data[0].valueForKey("id") as? String
                        self.uploadlist[pos].status = UploadStatus.OK
                    }else{
                        self.uploadlist[pos].status = UploadStatus.NG
                    }
                    
                }else{
                    self.uploadlist[pos].status = UploadStatus.NG
                }
                 self.tableView.reloadData()
                
        }
    }
    func isEmptyData() -> Bool {
        for file in self.uploadlist {
            if file.status == UploadStatus.OK || file.status == UploadStatus.UPLOADING {
                return false
            }
        }
        return true
    }
    func isFilesUploadFinish() -> Bool  {
        for file in self.uploadlist {
            if file.status == UploadStatus.UPLOADING{
                return false
            }
        }
        return true
    }
    func getFileByIndex(index:Int) -> Int {
        var count = 0
        for file in self.uploadlist {
            if file.index == index{
                return count
            }
            count += 1
        }
        return -1
    }
    override func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        return uploadlist.count
    }
 
    
    func imagePickerController(picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [String : AnyObject]) {
        let image = info[UIImagePickerControllerOriginalImage] as? UIImage
        
        if image != nil {
            let imageUrl = info[UIImagePickerControllerReferenceURL] as! NSURL
            let imagePickedData =  UIImagePNGRepresentation(image!)!
           
            let file = UploadFile()
            file.index = self.index
            file.status = UploadStatus.UPLOADING
            file.fileName = "file_\(self.index).jpg"

            self.index += 1
            print("path"+imageUrl.absoluteString)
            uploadlist.append( file)
            let index = NSIndexPath(forRow: self.uploadlist.count - 1 , inSection: 0)
            self.tableView.insertRowsAtIndexPaths([index], withRowAnimation: UITableViewRowAnimation.Fade)
            self.tableView.reloadData()
            self.uploadFile(imagePickedData, index: file.index,name: file.fileName!)
            
        }
        
        picker.dismissViewControllerAnimated(true, completion: nil)
    }
    
    func imagePickerControllerDidCancel(picker: UIImagePickerController) {
        picker.dismissViewControllerAnimated(true, completion: nil)
    }
    func chooseFileUpload()  {
        imagePicker.allowsEditing = false
        imagePicker.sourceType = .PhotoLibrary
        presentViewController(imagePicker, animated: true, completion: nil)
        
     }
    func updateInfo(input: UITextField)  {
        print("updateInfo")
        
        let pos = self.getFileByIndex(input.tag)
        self.uploadlist[pos].info = input.text!
    }
    func deleteFile(bnt: UIButton){
            let callActionHandler = { (action:UIAlertAction!) -> Void in
                let pos = self.getFileByIndex(bnt.tag)
                self.uploadlist.removeAtIndex(pos)
                let index = NSIndexPath(forRow: pos , inSection: 0)
                self.tableView.deleteRowsAtIndexPaths([index], withRowAnimation: UITableViewRowAnimation.Fade)
                self.tableView.reloadData()
            }
            let alertController = UIAlertController(title: CommonMess.ALERT, message: CommonMess.DELETE_FILE_MESS, preferredStyle: UIAlertControllerStyle.Alert)
            alertController.addAction(UIAlertAction(title: "NO", style:UIAlertActionStyle.Cancel, handler: nil))
            alertController.addAction(UIAlertAction(title: "OK", style:UIAlertActionStyle.Default, handler:callActionHandler ))
            self.presentViewController(alertController, animated: true, completion: nil)
    }
    override func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCellWithIdentifier("UploadFileCell", forIndexPath: indexPath) as! UploadFileCell
        cell.mFileName.text = self.uploadlist[indexPath.row].fileName
        cell.mProgess.progress = self.uploadlist[indexPath.row].progress
        if self.uploadlist[indexPath.row].status == UploadStatus.OK {
            cell.mFileName.textColor = UIColor.greenColor()
           // cell.mInfo.hidden = false
           
        }else if self.uploadlist[indexPath.row].status == UploadStatus.NG {
            cell.mFileName.textColor = UIColor.redColor()
            cell.mProgess.progressTintColor = UIColor.redColor()
            //cell.mInfo.hidden = true
        }else{
          //  scell.mInfo.hidden = true
        }
        cell.mInfo.tag = self.uploadlist[indexPath.row].index
        cell.mInfo.addTarget(self, action: #selector(FilesTableViewController.updateInfo(_:)), forControlEvents: .EditingChanged)
        cell.mInfo.inputAccessoryView = self.doneToolbar
        cell.mBtnDelete.tag = uploadlist[indexPath.row].index
        cell.mBtnDelete.addTarget(self, action: #selector(FilesTableViewController.deleteFile(_:)), forControlEvents: .TouchUpInside)

        // Configure the cell...

        return cell
    }
    func addDoneButtonOnKeyboard()
    {
         self.doneToolbar = UIToolbar(frame: CGRectMake(0, 0, 320, 50))
        doneToolbar!.barStyle = UIBarStyle.BlackTranslucent
        
        let flexSpace = UIBarButtonItem(barButtonSystemItem: UIBarButtonSystemItem.FlexibleSpace, target: nil, action: nil)
        let done: UIBarButtonItem = UIBarButtonItem(title: CommonMess.DONE, style: UIBarButtonItemStyle.Done, target: self, action: #selector(FilesTableViewController.doneButtonAction))
        
        
        doneToolbar!.items = [flexSpace,done]
        doneToolbar!.sizeToFit()
        
    }
    
    func doneButtonAction()
    {
        self.view.endEditing(true)
    }

    /*
    // Override to support conditional editing of the table view.
    override func tableView(tableView: UITableView, canEditRowAtIndexPath indexPath: NSIndexPath) -> Bool {
        // Return false if you do not want the specified item to be editable.
        return true
    }
    */

    /*
    // Override to support editing the table view.
    override func tableView(tableView: UITableView, commitEditingStyle editingStyle: UITableViewCellEditingStyle, forRowAtIndexPath indexPath: NSIndexPath) {
        if editingStyle == .Delete {
            // Delete the row from the data source
            tableView.deleteRowsAtIndexPaths([indexPath], withRowAnimation: .Fade)
        } else if editingStyle == .Insert {
            // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
        }    
    }
    */

    /*
    // Override to support rearranging the table view.
    override func tableView(tableView: UITableView, moveRowAtIndexPath fromIndexPath: NSIndexPath, toIndexPath: NSIndexPath) {

    }
    */

    /*
    // Override to support conditional rearranging of the table view.
    override func tableView(tableView: UITableView, canMoveRowAtIndexPath indexPath: NSIndexPath) -> Bool {
        // Return false if you do not want the item to be re-orderable.
        return true
    }
    */

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepareForSegue(segue: UIStoryboardSegue, sender: AnyObject?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */
    
}
