//
//  IncoCommon.swift
//  Inco
//
//  Created by admin on 7/20/16.
//  Copyright © 2016 Ban Sac. All rights reserved.
//

import Foundation

class IncoCommon {
    static let CLIENT_ID = "company"
    static let NUMBER_ITEMS_LOADING = 5
    
    static func saveToken(token:String) {
        let userDefaults = NSUserDefaults.standardUserDefaults()
        userDefaults.setValue(token, forKey: IncoResponse.M_TOKEN)
        userDefaults.synchronize() // don't forget this!!!!
    }
    static func logOut(){
        let userDefaults = NSUserDefaults.standardUserDefaults()
        userDefaults.removeObjectForKey( IncoResponse.M_TOKEN)
          userDefaults.synchronize() 
    }
    static func getToken()->String{
        let userDefaults = NSUserDefaults.standardUserDefaults()
        let token = userDefaults.valueForKey(IncoResponse.M_TOKEN) as? String
        return token!
    }
    
    static func isLogin() -> Bool {
        let userDefaults = NSUserDefaults.standardUserDefaults()
        let token = userDefaults.valueForKey(IncoResponse.M_TOKEN) as? String
        if token?.characters.count > 0 {
            return true
        }
        return false

    }
    static func saveClientID(clientid: String ){
        let userDefaults = NSUserDefaults.standardUserDefaults()
        userDefaults.setValue(clientid, forKey: IncoCommon.CLIENT_ID)
        userDefaults.synchronize() // don't forget this!!!!
    }
    static func getClientID() -> String{
        let userDefaults = NSUserDefaults.standardUserDefaults()
        let token = userDefaults.valueForKey(IncoCommon.CLIENT_ID) as? String
        return token!
    }
}
