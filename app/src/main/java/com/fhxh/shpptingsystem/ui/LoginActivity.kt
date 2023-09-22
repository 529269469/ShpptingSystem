package com.fhxh.shpptingsystem.ui

import android.app.Activity
import android.content.Intent
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.fhxh.shpptingsystem.R
import com.fhxh.shpptingsystem.databinding.ActivityLoginBinding
import com.fhxh.shpptingsystem.utils.usb.USBDeviceParcelable


class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    val TAG = "Login"
    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setNavigationBarVisible(this, true)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)





        val usbManager = getSystemService(USB_SERVICE) as UsbManager
        val deviceHashMap: HashMap<String, UsbDevice> = usbManager.deviceList
        for (device in deviceHashMap.values) {
            if ("whkj Printer"==device.productName){
                Log.e(TAG, "onCreate: ${device.productName}" )
                var f = USBDeviceParcelable()
                f.usbDevice = device
                Handler().postDelayed({
                    startActivity(Intent(this,MainActivity::class.java).putExtra("usbDevice",f))
                    finish()
                }, 3000)
            }

        }


    }


    /**
     * 隐藏或显示 导航栏
     *
     * @param activity
     */
    fun setNavigationBarVisible(activity: Activity, isHide: Boolean) {
        if (isHide) {
            val decorView = activity.window.decorView
            val uiOptions = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
            decorView.systemUiVisibility = uiOptions
        } else {
            val decorView = activity.window.decorView
            val uiOptions = View.SYSTEM_UI_FLAG_VISIBLE
            decorView.systemUiVisibility = uiOptions
        }
    }
}