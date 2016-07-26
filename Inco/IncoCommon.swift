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
    static let USER_INFO = "user_info"
    static let IS_REMEMBER = "remember"
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
    static func  setRemember(remember:Bool){
        let userDefaults = NSUserDefaults.standardUserDefaults()
        userDefaults.setBool(remember, forKey: IncoCommon.IS_REMEMBER)
        userDefaults.synchronize() // don't forget this!!!!
    }
    static func  isRemember() -> Bool{
        let userDefaults = NSUserDefaults.standardUserDefaults()
        let remember = userDefaults.valueForKey(IncoCommon.IS_REMEMBER) as? Bool
        if remember == nil{
            return false
        }
        return remember!
    }
    static func saveUserInfo(user:NSDictionary){
        let userDefaults = NSUserDefaults.standardUserDefaults()
        let dataSave:NSData = NSKeyedArchiver.archivedDataWithRootObject(user)
        userDefaults.setObject(dataSave, forKey: IncoCommon.USER_INFO)
        userDefaults.synchronize() // don't forget this!!!!
    }
    static func getUsrInfo() -> NSDictionary?{
        let userDefaults = NSUserDefaults.standardUserDefaults()
        let dataSave = userDefaults.valueForKey(IncoCommon.USER_INFO) as? NSData
        if dataSave != nil{
            print("dataSave != nil")

            let user = NSKeyedUnarchiver.unarchiveObjectWithData(dataSave!) as? NSDictionary
            return user
        }
        print("dataSave nil")
        return nil
    }
    static func getVersion() -> String{
        let version = NSBundle.mainBundle().infoDictionary?["CFBundleShortVersionString"] as? String
        let build = NSBundle.mainBundle().infoDictionary?["CFBundleVersion"] as? String
        return "Inco v "+version!+"("+build!+")"
    }
}
