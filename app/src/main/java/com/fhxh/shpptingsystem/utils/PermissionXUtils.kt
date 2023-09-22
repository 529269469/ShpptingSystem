package com.fhxh.shpptingsystem.utils

import android.Manifest
import androidx.fragment.app.FragmentActivity
import com.permissionx.guolindev.PermissionX
import com.permissionx.guolindev.request.ExplainScope

object PermissionXUtils {
    private val permissionArray = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO
    )

    fun FragmentActivity.setPermission(vararg permissions: String? = permissionArray, isSuccess: (Boolean)->Unit) {
        PermissionX.init(this)
            .permissions(
                permissions.toString()
            ).onExplainRequestReason { scope: ExplainScope, deniedList: List<String> ->
                scope.showRequestReasonDialog(
                    deniedList,
                    "即将重新申请的权限是程序必须依赖的权限", "我已明白", "取消"
                )
            }
            .onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(deniedList, "您需要去应用程序设置当中手动开启权限", "我已明白", "取消")
            }
            .request { allGranted: Boolean, _: List<String?>?, deniedList: List<String?> ->  //grantedList用于记录所有已被授权的权限，deniedList用于记录所有被拒绝的权限
                if (allGranted) { //是否所有申请的权限都已被授权
                    isSuccess.invoke(true)
                } else {
                }
            }

    }


}