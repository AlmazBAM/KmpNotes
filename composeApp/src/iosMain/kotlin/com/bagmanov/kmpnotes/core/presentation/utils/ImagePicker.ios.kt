package com.bagmanov.kmpnotes.core.presentation.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.Foundation.NSURL
import platform.darwin.NSObject
import platform.UIKit.UIApplication
import platform.UIKit.UIImagePickerControllerReferenceURL

@Composable
actual fun rememberImagePickerLauncher(onResult: (String?) -> Unit): ImagePickerLauncher {
    val delegate = remember {
        object : NSObject(), UIImagePickerControllerDelegateProtocol, UINavigationControllerDelegateProtocol {
            override fun imagePickerController(
                picker: UIImagePickerController,
                didFinishPickingMediaWithInfo: Map<Any?, *>
            ) {
                val url = didFinishPickingMediaWithInfo[UIImagePickerControllerReferenceURL] as? NSURL
                onResult(url?.absoluteString)
                picker.dismissViewControllerAnimated(true, null)
            }

            override fun imagePickerControllerDidCancel(picker: UIImagePickerController) {
                picker.dismissViewControllerAnimated(true, null)
            }
        }
    }

    return remember {
        object : ImagePickerLauncher {
            override fun launch() {
                val picker = UIImagePickerController()
                picker.setSourceType(UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypePhotoLibrary)
                picker.setDelegate(delegate)
                
                val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
                rootViewController?.presentViewController(picker, true, null)
            }
        }
    }
}
