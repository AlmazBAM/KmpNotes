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
import platform.UIKit.UIImagePickerControllerOriginalImage
import platform.UIKit.UIImagePickerControllerEditedImage
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSUUID
import platform.Foundation.writeToURL
import platform.UIKit.UIWindow

@Composable
actual fun rememberImagePickerLauncher(onResult: (String?) -> Unit): ImagePickerLauncher {
    val delegate = remember {
        object : NSObject(), UIImagePickerControllerDelegateProtocol, UINavigationControllerDelegateProtocol {
            override fun imagePickerController(
                picker: UIImagePickerController,
                didFinishPickingMediaWithInfo: Map<Any?, *>
            ) {
                val image = (didFinishPickingMediaWithInfo[UIImagePickerControllerEditedImage]
                    ?: didFinishPickingMediaWithInfo[UIImagePickerControllerOriginalImage]) as? UIImage

                if (image != null) {
                    val imageData = UIImageJPEGRepresentation(image, 0.8)
                    if (imageData != null) {
                        val fileName = "temp_${NSUUID().UUIDString()}.jpg"
                        val tempPath = NSTemporaryDirectory()
                        val fileURL = NSURL.fileURLWithPath(tempPath).URLByAppendingPathComponent(fileName)!!

                        if (imageData.writeToURL(fileURL, true)) {
                            // Возвращаем путь. Если Coil не подхватит, попробуйте "file://" + fileURL.path
                            onResult(fileURL.path)
                        } else {
                            onResult(null)
                        }
                    } else {
                        onResult(null)
                    }
                } else {
                    onResult(null)
                }
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

                val keyWindow = UIApplication.sharedApplication.windows.firstOrNull { (it as? UIWindow)?.isKeyWindow() == true } as? UIWindow
                    ?: UIApplication.sharedApplication.keyWindow

                keyWindow?.rootViewController?.presentViewController(picker, true, null)
            }
        }
    }
}
