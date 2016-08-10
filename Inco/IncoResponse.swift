//
//  IncoResponse.swift
//  Inco
//
//  Created by admin on 7/19/16.
//  Copyright © 2016 Ban Sac. All rights reserved.
//

import Foundation

class IncoResponse  {
    var status:String = ""
    var error_code:String = ""
    var error_mess:String = ""
    var data:[NSDictionary]!
    static let M_STATUS_OK = "sucess"
    static let M_TOKEN = "token"
    
    init(data :NSDictionary){
        self.status = (data.valueForKey("status") as? String)!
        if self.status == "" {
        
        
        }
        if self.isOK() {
            self.data =   data.valueForKey("data") as? [NSDictionary]
            if self.data == nil{
                let tmp =   data.valueForKey("data") as? NSDictionary
                self.data =  [NSDictionary]()
                self.data.append(tmp!)
            }
            
        }
    }
    func isOK() -> Bool{
        if self.status == IncoResponse.M_STATUS_OK  || self.status == "ok" {
            return true
        }
        return false
    
    }

}