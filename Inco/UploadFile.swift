//
//  UploadFile.swift
//  Inco
//
//  Created by admin on 7/31/16.
//  Copyright © 2016 Ban Sac. All rights reserved.
//

import Foundation
enum UploadStatus {
    case OK,UPLOADING,NG
}
class UploadFile {
    var fileName:String?
    var file_size:String?
    var info:String = ""
    var id:String?
    var progress:Float = 0.0
    var status = UploadStatus.UPLOADING
    var index:Int = 0;
}