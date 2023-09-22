package com.fhxh.shpptingsystem.base_app;

public class Constant {

    public static String CHANNEL1 = "/dev/ttyS1"; //   /dev/ttyS1  对应枪
    public static String CHANNEL2 = "/dev/ttyS2"; //   /dev/ttyS2  对应靶

    public static int BAUDRATE_115200 = 115200;
    public static int BAUDRATE_9600 = 9600;

    public static int k8 = 230;   //K8  对应靶

    // 上传灯号为0的间隔时间
    public static int mTime = 500;

    public static String ip = "192.168.0.145";  // 后端ip
    public static int port_int =9291 ;
    public static String port = "9291";  // 端口号




    public static String MODE_START = "AT+MODE=0\r\n";
    public static int BAUDRATE = 115200;
    public static String UART = "AT+UART=115200,0,0\r\n";
    public static String RFBR = "AT+RFBR=250000\r\n";
    public static String PWR = "AT+PWR=4\r\n";
    public static String RFCH = "AT+RFCH=0\r\n";
    public static String PID = "AT+PID=1\r\n";
    public static String MODE_END = "AT+MODE=1\r\n";

    public static String At_ALL = "AT+ALL\r\n";

}
