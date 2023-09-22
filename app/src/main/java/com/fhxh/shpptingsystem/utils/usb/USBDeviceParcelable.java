package com.fhxh.shpptingsystem.utils.usb;


import android.hardware.usb.UsbDevice;
import android.os.Parcel;
import android.os.Parcelable;

public class USBDeviceParcelable implements Parcelable {


    private UsbDevice usbDevice;



    public UsbDevice getUsbDevice() {
        return usbDevice;
    }

    public void setUsbDevice(UsbDevice usbDevice) {
        this.usbDevice = usbDevice;
    }


    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub

        dest.writeParcelable(usbDevice,flags);

    }


    public static final Creator<USBDeviceParcelable> CREATOR = new Creator<USBDeviceParcelable>() {

        @Override
        public USBDeviceParcelable createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            return new USBDeviceParcelable(source);
        }

        @Override
        public USBDeviceParcelable[] newArray(int size) {
            // TODO Auto-generated method stub
            return new USBDeviceParcelable[size];
        }

    };

   public USBDeviceParcelable() {

    }

    private USBDeviceParcelable(Parcel in) {

        usbDevice = in.readParcelable(UsbDevice.class.getClassLoader());

    }

}
