//
//  AppDelegate.swift
//  Inco
//
//  Created by admin on 7/17/16.
//  Copyright © 2016 Ban Sac. All rights reserved.
//

import UIKit
import CoreData
import AudioToolbox

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {
    var notifications  = [Push]()
    var window: UIWindow?
    var centerContainer: MMDrawerController?
    var deviceToken:String?

    func application(application: UIApplication, didFinishLaunchingWithOptions launchOptions: [NSObject: AnyObject]?) -> Bool {
        // Override point for customization after application launch.
        // notifications
        application.registerForRemoteNotifications()
        application.registerUserNotificationSettings(UIUserNotificationSettings(forTypes: [.Badge, .Sound, .Alert], categories: nil))
        
        UINavigationBar.appearance().barTintColor = UIColor(red: 141.0/255.0, green: 184.0/255.0, blue: 61.0/255.0, alpha: 1.0)
        UINavigationBar.appearance().tintColor = UIColor.whiteColor()
        UINavigationBar.appearance().titleTextAttributes = [NSForegroundColorAttributeName:UIColor.whiteColor()]
        UITabBar.appearance().barTintColor = UIColor(red: 141.0/255.0, green: 184.0/255.0, blue: 61.0/255.0, alpha: 1.0)
        UITabBar.appearance().tintColor = UIColor.whiteColor()
        window?.tintColor = UIColor(red: 141.0/255.0, green: 184.0/255.0, blue: 61.0/255.0, alpha: 1.0)

       // UITabBar.appearance().titleTextAttributes = [NSForegroundColorAttributeName:UIColor.whiteColor()]
        if(IncoCommon.isLogin()){
        let mainStoryboard: UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
        
        let centerViewController = mainStoryboard.instantiateViewControllerWithIdentifier("MainViewController") as! MainViewController
        centerViewController.isCheckStatus = true
        let leftViewController = mainStoryboard.instantiateViewControllerWithIdentifier("LeftSideViewController") as! LeftSideViewController
        
        let leftSideNav = UINavigationController(rootViewController: leftViewController)
        let centerNav = UINavigationController(rootViewController: centerViewController)
        
        centerContainer = MMDrawerController (centerViewController: centerNav, leftDrawerViewController: leftSideNav)
        
        centerContainer!.openDrawerGestureModeMask = MMOpenDrawerGestureMode.PanningCenterView;
        centerContainer!.closeDrawerGestureModeMask = MMCloseDrawerGestureMode.PanningCenterView;
        
        window!.rootViewController = centerContainer
            window!.makeKeyAndVisible()
        }
        return true
    }
    // notifications
    func application(application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: NSData) {
        print("deviceToken : \(deviceToken)")
        var dataString = String(format: "%@", deviceToken)
        dataString = dataString.stringByReplacingOccurrencesOfString("<", withString: "")
        dataString = dataString.stringByReplacingOccurrencesOfString(">", withString: "")
        dataString = dataString.stringByReplacingOccurrencesOfString(" ", withString: "")
        self.deviceToken = dataString
        print("deviceToken : \(self.deviceToken)")
        
        
    }
    func application(application: UIApplication, didReceiveRemoteNotification userInfo: [NSObject : AnyObject], fetchCompletionHandler completionHandler: (UIBackgroundFetchResult) -> Void) {
        UIApplication.sharedApplication().applicationIconBadgeNumber = 0
        let state = application.applicationState
               print("didReceiveRemoteNotificationuserInfo1")
        if state != UIApplicationState.Active {
            self.notifications.removeAll()
        }
        let new  = userInfo["inco"] as! String
        let data = new.dataUsingEncoding(NSUTF8StringEncoding, allowLossyConversion: false)!
        
        do {
            let json = try NSJSONSerialization.JSONObjectWithData(data, options: []) as! [String: AnyObject]
            let push = Push()
            push.title = json["title"] as? String
            push.photo = json["photo"] as? String
            push.message = json["message"] as? String
            push.project = json["project"] as? String
            push.parent = json["parent"] as? String
            push.attach = json["attach"] as? String
            push.date = json["date"] as? String
            push.id = json["id"] as? String
            push.id = json["id"] as? String

            let type = json["component"] as? String
            if type == BindTypeUpload.PROJECT_COMMENT {
                push.type = ComponentType.PROJECT
            }else  if type == BindTypeUpload.TICKET_COMMENT{
                push.type = ComponentType.TICKET
            }else  if type == BindTypeUpload.DISCUSSION_COMMENT {
                push.type = ComponentType.DISCUSSTION
            }else{
                push.type = ComponentType.TASKS
            }
            self.notifications.append(push)

            
        } catch let error as NSError {
            print("Failed to load: \(error.localizedDescription)")
        }
        print(new)
       
        if state == UIApplicationState.Active {
             NSNotificationCenter.defaultCenter().postNotificationName("didReceiveRemoteNotification", object: userInfo)
            AudioServicesPlaySystemSound(1007);
        }else{
        
            
            let tabBarController = DetailTabBarViewController()
            let nav = UINavigationController(rootViewController: tabBarController)
            tabBarController.isNotificaiton = true
            let item = self.notifications[0]
            tabBarController.type = item.type!
            tabBarController.iD = item.id!
            let projectStoryboard: UIStoryboard = UIStoryboard(name: "detailcomponent", bundle: nil)
            var detail:UITableViewController
            if item.type == ComponentType.TASKS {
                detail = projectStoryboard.instantiateViewControllerWithIdentifier("DetailTableViewController") as! DetailTableViewController
                let cell = TaskCell()
                cell.id = item.id!
                cell.projects_id = item.project
                (detail as! DetailTableViewController).item = cell
                
                tabBarController.projectID = item.project!
                
            }else if item.type == ComponentType.TICKET {
                detail = projectStoryboard.instantiateViewControllerWithIdentifier("DetailTicketTableViewController") as! DetailTicketTableViewController
                let cell = TicketCell()
                cell.id = item.id!
                cell.projects_id = item.project
                (detail as! DetailTicketTableViewController).item = cell
                
                tabBarController.projectID = item.project!
                
            }else if item.type == ComponentType.DISCUSSTION{
                detail = projectStoryboard.instantiateViewControllerWithIdentifier("DetailDiscussionTableViewController") as! DetailDiscussionTableViewController
                let cell = DiscussionCell()
                cell.id = item.id!
                cell.projects_id = item.project
                (detail as! DetailDiscussionTableViewController).item = cell
                
                tabBarController.projectID = item.project!
                
            }else{
                detail = projectStoryboard.instantiateViewControllerWithIdentifier("DetailProjectTableViewController") as! DetailProjectTableViewController
                tabBarController.projectID = item.project!
            }
            
            let comment =   projectStoryboard.instantiateViewControllerWithIdentifier("CommentsTableViewController") as! CommentsTableViewController
            comment.type = item.type!
            comment.id = item.id!
            let controllers = [detail,comment]
            tabBarController.commentView = comment
            tabBarController.viewControllers = controllers
            let firstImage = UIImage(named: "ic_description")
            let secondImage = UIImage(named: "tabs_discusstion")
            detail.tabBarItem = UITabBarItem(
                title: "",
                image: firstImage,
                tag: 1)
            comment.tabBarItem = UITabBarItem(
                title: "",
                image: secondImage,
                tag:2)
            self.window?.rootViewController?.presentViewController(nav, animated: true, completion: nil)
            
        }

        completionHandler(UIBackgroundFetchResult.NewData)
        
        
    }
    
    func applicationWillResignActive(application: UIApplication) {
        print("applicationWillResignActive")

        // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
        // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
    }

    func applicationDidEnterBackground(application: UIApplication) {
        print("applicationDidEnterBackground")

        // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
        // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
    }

    func applicationWillEnterForeground(application: UIApplication) {
        UIApplication.sharedApplication().applicationIconBadgeNumber = 0
        print("applicationWillEnterForeground")

        // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
    }

    func applicationDidBecomeActive(application: UIApplication) {
        print("applicationDidBecomeActive")

        // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
    }

    func applicationWillTerminate(application: UIApplication) {
        print("applicationWillTerminate")

        // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
        // Saves changes in the application's managed object context before the application terminates.
        self.saveContext()
    }

    // MARK: - Core Data stack

    lazy var applicationDocumentsDirectory: NSURL = {
        // The directory the application uses to store the Core Data store file. This code uses a directory named "vn.bansac.Inco" in the application's documents Application Support directory.
        let urls = NSFileManager.defaultManager().URLsForDirectory(.DocumentDirectory, inDomains: .UserDomainMask)
        return urls[urls.count-1]
    }()

    lazy var managedObjectModel: NSManagedObjectModel = {
        // The managed object model for the application. This property is not optional. It is a fatal error for the application not to be able to find and load its model.
        let modelURL = NSBundle.mainBundle().URLForResource("Inco", withExtension: "momd")!
        return NSManagedObjectModel(contentsOfURL: modelURL)!
    }()

    lazy var persistentStoreCoordinator: NSPersistentStoreCoordinator = {
        // The persistent store coordinator for the application. This implementation creates and returns a coordinator, having added the store for the application to it. This property is optional since there are legitimate error conditions that could cause the creation of the store to fail.
        // Create the coordinator and store
        let coordinator = NSPersistentStoreCoordinator(managedObjectModel: self.managedObjectModel)
        let url = self.applicationDocumentsDirectory.URLByAppendingPathComponent("SingleViewCoreData.sqlite")
        var failureReason = "There was an error creating or loading the application's saved data."
        do {
            try coordinator.addPersistentStoreWithType(NSSQLiteStoreType, configuration: nil, URL: url, options: nil)
        } catch {
            // Report any error we got.
            var dict = [String: AnyObject]()
            dict[NSLocalizedDescriptionKey] = "Failed to initialize the application's saved data"
            dict[NSLocalizedFailureReasonErrorKey] = failureReason

            dict[NSUnderlyingErrorKey] = error as NSError
            let wrappedError = NSError(domain: "YOUR_ERROR_DOMAIN", code: 9999, userInfo: dict)
            // Replace this with code to handle the error appropriately.
            // abort() causes the application to generate a crash log and terminate. You should not use this function in a shipping application, although it may be useful during development.
            NSLog("Unresolved error \(wrappedError), \(wrappedError.userInfo)")
            abort()
        }
        
        return coordinator
    }()

    lazy var managedObjectContext: NSManagedObjectContext = {
        // Returns the managed object context for the application (which is already bound to the persistent store coordinator for the application.) This property is optional since there are legitimate error conditions that could cause the creation of the context to fail.
        let coordinator = self.persistentStoreCoordinator
        var managedObjectContext = NSManagedObjectContext(concurrencyType: .MainQueueConcurrencyType)
        managedObjectContext.persistentStoreCoordinator = coordinator
        return managedObjectContext
    }()

    // MARK: - Core Data Saving support

    func saveContext () {
        if managedObjectContext.hasChanges {
            do {
                try managedObjectContext.save()
            } catch {
                // Replace this implementation with code to handle the error appropriately.
                // abort() causes the application to generate a crash log and terminate. You should not use this function in a shipping application, although it may be useful during development.
                let nserror = error as NSError
                NSLog("Unresolved error \(nserror), \(nserror.userInfo)")
                abort()
            }
        }
    }

}

