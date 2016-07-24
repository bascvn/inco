//
//  UIViewControllerExtensions.swift
//  Inco
//
//  Created by admin on 7/24/16.
//  Copyright © 2016 Ban Sac. All rights reserved.
//

import Foundation
extension UIViewController {
    func hideKeyboardWhenTappedAround() {
        let tap: UITapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(UIViewController.dismissKeyboard))
        view.addGestureRecognizer(tap)
    }
    
    func dismissKeyboard() {
        view.endEditing(true)
    }
}