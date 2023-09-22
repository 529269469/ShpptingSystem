package com.fhxh.shpptingsystem.sdk;

import java.io.UnsupportedEncodingException;


public class BrightekCommandM {

    //10.1.1 初始化打印机

    /**
     * 初始打印机，指令集复位
     *
     * @return
     */
    public static byte[] t1b40() {


        return new byte[]{0x1B, 0x40};
    }

    //10.1.2唤醒打印
    public static byte[] t00() {


        return new byte[]{0x00};
    }

    //10.1.3回车
    public static byte[] t0D() {


        return new byte[]{0x0D};
    }


    //10.1.4换行
    public static byte[] t0A() {


        return new byte[]{0x0A};
    }

    //10.1.5执行N点走纸
    public static byte[] t1b4a(int n) {


        return new byte[]{0x1B, 0x4A, (byte) n};
    }

    //10.1.6打印并走纸到黑标处
    public static byte[] t0c() {


        return new byte[]{0x0C};
    }


    //10.2.1选择字库
    public static byte[] t1b38(int n) {


        return new byte[]{0x1B, 0x38, (byte) n};
    }


    //10.3.1灰度打印
    public static byte[] t1b6d(int n) {


        return new byte[]{0x1B, 0x6D, (byte) n};
    }

    //10.3.2横向放大
    public static byte[] t1b55(int n) {


        return new byte[]{0x1B, 0x55, (byte) n};
    }

    //10.3.3纵向放大
    public static byte[] t1b56(int n) {


        return new byte[]{0x1B, 0x56, (byte) n};
    }

    //10.3.4等比例放大
    public static byte[] t1b57(int n) {


        return new byte[]{0x1B, 0x57, (byte) n};
    }

    //10.3.5下划线打印
    public static byte[] t1b2d(int n) {


        return new byte[]{0x1B, 0x2D, (byte) n};
    }

    //10.3.6上划线打印
    public static byte[] t1b2b(int n) {


        return new byte[]{0x1B, 0x2B, (byte) n};
    }

    //10.3.7反白打印
    public static byte[] t1b69(int n) {


        return new byte[]{0x1B, 0x69, (byte) n};
    }

    //10.3.8反向打印
    public static byte[] t1b63(int n) {


        return new byte[]{0x1B, 0x63, (byte) n};
    }

    //10.3.9字符旋转打印
    public static byte[] t1c49(int n) {


        return new byte[]{0x1C, 0x49, (byte) n};
    }

    //10.4.1设置行间距
    public static byte[] t1b31(int n) {


        return new byte[]{0x1B, 0x31, (byte) n};
    }

    //10.4.2设置字符间距
    public static byte[] t1b70(int n) {


        return new byte[]{0x1B, 0x70, (byte) n};
    }

    //10.4.3设置对齐方式
    public static byte[] t1b61(int n) {


        return new byte[]{0x1B, 0x61, (byte) n};
    }

    //10.4.4垂直制表

    public static byte[] t1b42_3(int v1, int v2, int v3, String str1, String str2, String str3) {

        byte[] strdata1 = str1.getBytes();
        byte[] strdata2 = str2.getBytes();
        byte[] strdata3 = str3.getBytes();
        int lengths = strdata1.length + strdata2.length + strdata3.length + 9;
        byte[] retData = new byte[lengths];
        int k = 0;

        retData[k++] = 0x1B;
        retData[k++] = 0x42;
        retData[k++] = (byte) v1;
        retData[k++] = (byte) v2;
        retData[k++] = (byte) v3;
        retData[k++] = 0x00;
        for (byte b : strdata1
        ) {
            retData[k++] = b;
        }
        retData[k++] = 0x0B;

        for (byte b : strdata2
        ) {
            retData[k++] = b;
        }
        retData[k++] = 0x0B;
        for (byte b : strdata3
        ) {
            retData[k++] = b;
        }
        retData[k++] = 0x0B;

        return retData;
    }

    /**
     * 垂直制表
     *
     * @param datas 垂直制表的数据
     * @return
     */
    public static byte[] t1b42(String[] datas) {

        //data length
        int length = 4 + datas.length * 2;//默认是4个数据
        for (int i = 0; i < datas.length; i++) {

            length += datas[i].length();


        }

        byte[] retData = new byte[length];
        int k = 0;

        retData[k++] = 0x1B;
        retData[k++] = 0x42;
        for (int i = 0; i < datas.length; i++) {
            retData[k++] = (byte) (i + 1);

        }
        retData[k++] = 0x00;


        for (String str : datas
        ) {
            try {
                byte[] data = str.getBytes("GBK");

                for (byte b : data
                ) {

                    retData[k++] = b;

                }
                retData[k++] = 0x0B;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


        }

        retData[k++] = 0x0A;


        return retData;
    }


    //10.4.6水平制表

    /**
     * 水平制表 3列
     *
     * @param h1   位置1
     * @param h2   位置2
     * @param h3   位置3
     * @param str1 数据1
     * @param str2 数据2
     * @param str3 数据3
     * @return 打印数据
     */
    public static byte[] t1b44_3(int h1, int h2, int h3, String str1, String str2, String str3) {

        byte[] strdata1 = str1.getBytes();
        byte[] strdata2 = str2.getBytes();
        byte[] strdata3 = str3.getBytes();
        int lengths = strdata1.length + strdata2.length + strdata3.length + 10;
        byte[] retData = new byte[lengths];
        int k = 0;

        retData[k++] = 0x1B;
        retData[k++] = 0x44;
        retData[k++] = (byte) h1;
        retData[k++] = (byte) h2;
        retData[k++] = (byte) h3;
        retData[k++] = 0x00;
        retData[k++] = 0x09;
        for (byte b : strdata1
        ) {
            retData[k++] = b;
        }
        retData[k++] = 0x09;

        for (byte b : strdata2
        ) {
            retData[k++] = b;
        }
        retData[k++] = 0x09;
        for (byte b : strdata3
        ) {
            retData[k++] = b;
        }
        retData[k++] = 0x0A;


        return retData;
    }

    /**
     * 水平制表
     *
     * @param position 水平位置合集
     * @param datas    数据合集
     * @return
     */
    public static byte[] t1b44(int[] position, String[] datas) {

        byte[] errorData = ("Error Input!\n").getBytes();
        if (position.length != datas.length) {

            return errorData;
        }

        //data length
        int length = 4 + position.length * 2;//默认是4个数据
        for (int i = 0; i < datas.length; i++) {

            length += datas[i].length();


        }

        byte[] retData = new byte[length];
        int k = 0;

        retData[k++] = 0x1B;
        retData[k++] = 0x44;
        for (int i = 0; i < position.length; i++) {
            retData[k++] = (byte) position[i];

        }
        retData[k++] = 0x00;


        for (String str : datas
        ) {
            try {
                byte[] data = str.getBytes("GBK");
                retData[k++] = 0x09;
                for (byte b : data
                ) {

                    retData[k++] = b;

                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


        }

        retData[k++] = 0x0A;


        return retData;
    }

    //10.4.8设置右边距
    public static byte[] t1b51(int n) {


        return new byte[]{0x1B, 0x51, (byte) n};
    }

    //10.4.9设置左边距
    public static byte[] t1b6c(int n) {
        return new byte[]{0x1B, 0x6C, (byte) n};
    }

    //10.5.1打印点阵图形


    //10.6一维码打印

    /**
     * 打印一维条码
     *
     * @param type 条码类型 65- UPC-A,66-UPC-E,67-EAN13,68-EAN8,69-CODE39,70-INTERLEAVED25,71-CODE BAR
     *             72-CODE93,73-CODE128
     * @param data 条码内容
     * @return 打印数据
     */
    public static byte[] t1d6b(int type, String data) {
        byte[] strdata = data.getBytes();
        byte[] redata;// = new byte[strdata.length + 5];
        byte[] errorData;
        int k = 0;
        switch (type) {
            case 65:
                if (data.length() == 0) {

                    data = "12345678902";
                    strdata = data.getBytes();
                }
                if (strdata.length != 11) {
                    errorData = "length is not equals 11 ".getBytes();
                    return errorData;
                }
                break;
            case 66:


                if (data.length() == 0) {
                    data = "0234567";
                    strdata = data.getBytes();
                }
                if (strdata.length != 7) {

                    if (strdata[0] != 0x30) {//判断收个字符是否是0

                        errorData = "first size is not equals 0 ".getBytes();
                        return errorData;

                    }
                    errorData = "length is not equals 7 ".getBytes();
                    return errorData;
                }
                break;
            case 67:
                if (data.length() == 0) {
                    data = "123456789012";
                    strdata = data.getBytes();
                }
                if (strdata.length != 12) {

                    errorData = "length is not equals 12 ".getBytes();
                    return errorData;
                }
                break;
            case 68:
                if (data.length() == 0) {
                    data = "0234567";
                    strdata = data.getBytes();
                }
                if (strdata.length != 7) {

                    errorData = "length is not equals 7 ".getBytes();
                    return errorData;
                }
                break;

            case 69:
                if (data.length() == 0) {
                    data = "12345";
                    strdata = data.getBytes();
                }
                break;

            case 70:
                if (data.length() == 0) {
                    data = "123456789";
                    strdata = data.getBytes();
                }
//                if (strdata.length % 2 != 0) {
//
//                    errorData = "length is not equals    even number ".getBytes();
//                    return errorData;
//                }
                break;

            case 71:
                if (data.length() == 0) {
                    data = "A1234B";
                    strdata = data.getBytes();
                }
                break;

            case 72:
                if (data.length() == 0) {
                    data = "1234562227";
                    strdata = data.getBytes();
                }
                break;

            case 73:
                if (data.length() == 0) {
                    data = "1234222567";
                    strdata = data.getBytes();
                }
                break;

        }

        redata = new byte[strdata.length + 5];
        redata[k++] = 0x1D;
        redata[k++] = 0x6B;
        redata[k++] = (byte) type;
        redata[k++] = (byte) (strdata.length);

        for (byte b : strdata
        ) {
            redata[k++] = b;
        }

        redata[k++] = 0x0A;

        return redata;

    }

    //10.6.4HRI字符
    public static byte[] t1d48(int n) {
        return new byte[]{0x1D, 0x48, (byte) n};
    }

    //10.6.5设置条码高度
    public static byte[] t1d68(int n) {
        return new byte[]{0x1D, 0x68, (byte) n};
    }

    //10.6.6设置条码宽度
    public static byte[] t1d77(int n) {
        return new byte[]{0x1D, 0x77, (byte) n};
    }

    //10.7.1打印PDF417
    public static byte[] t1d28Pdf417(String data) {
        if (data.length() == 0) {
            data = "1234567890";
        }
        byte[] dataByte = new byte[0];
        try {
            dataByte = data.getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] retData = new byte[dataByte.length + 7];
        int k = 0;
        retData[k++] = 0x1D;
        retData[k++] = 0x28;
        retData[k++] = 0x6b;
        retData[k++] = (byte) ((dataByte.length + 2) % 256);
        retData[k++] = (byte) ((dataByte.length + 2) / 256);
        retData[k++] = 0x04;//条码列数，可以根据纸宽设定
        retData[k++] = 0x00;
        for (byte b : dataByte
        ) {
            retData[k++] = b;

        }

        return retData;


    }
    //10.7.2打印QR码

    public static byte[] t1d28Qr(String data) {
        if (data.length() == 0) {
            data = "1234567890";
        }
        byte[] dataByte = data.getBytes();
        byte[] retData = new byte[dataByte.length + 7];
        int k = 0;
        retData[k++] = 0x1D;
        retData[k++] = 0x28;
        retData[k++] = 0x31;
        retData[k++] = (byte) ((dataByte.length + 2) % 256);
        retData[k++] = (byte) ((dataByte.length + 2) / 256);
        retData[k++] = 0x00;
        retData[k++] = 0x00;
        for (byte b : dataByte
        ) {
            retData[k++] = b;

        }

        return retData;

    }

    //11.22加粗打印

    /**
     * 加粗打印
     *
     * @param n
     * @return
     */
    public static byte[] t1f5535(int n) {

        return new byte[]{0x1F, 0x55, 0x35, (byte) n};

    }

    //11.12蜂鸣测试
    public static byte[] t1b552b(int n) {
        return new byte[]{0x1B, 0x55, 0x2B, (byte) n};
    }


    //20200904 add

    /**
     * 设置行间距】 设置行间距为((n-24)*0.125 毫米)。
     *
     * @param n 0≤n≤255。n 少于 24 时，行间距为 0。
     * @return
     */
    public static byte[] t1b33(int n) {
        return new byte[]{0x1b, 0x33, (byte) n};
    }

    /**
     * QR 码打印一
     *
     * @param a    a 的值是指设置 QR 码图形模块的类型到[a 点 × a 点]。
     * @param b    b 的值是指设置 QR 码的错误校正水平误差。
     * @param data 打印数据
     * @return
     */
    public static byte[] t1f1c(int a, int b, String data) {

        byte[] dataStr = null;
        try {
            dataStr = data.getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (null == dataStr) {


            dataStr = new byte[]{0x31, 0x32, 0x33};

        }

        int reLength = dataStr.length + 6;

        byte[] reData = new byte[reLength];

        int iRe = 0;
        reData[iRe++] = 0x1F;
        reData[iRe++] = 0x1C;
        reData[iRe++] = (byte) a;
        reData[iRe++] = (byte) b;
        reData[iRe++] = (byte) (dataStr.length % 256);
        reData[iRe++] = (byte) (dataStr.length / 256);

        for (byte y : dataStr
        ) {
            reData[iRe++] = y;
        }

        return reData;

    }

    /**
     * 打印二维码 ，新版本M
     *
     * @param v    v 是指定 QR 码的版本号，如不符合规则，会自动适配。v 的值为 0 则表示由系统根 据 QR 码数据自动分配。
     * @param k    k 的值是指设置 QR 码的错误校正水平误差
     * @param y    、y 的值是指设置 QR 码图形模块的类型到[y 点 × y 点]。,二维码大小
     * @param data 打印数据
     * @return
     */
    public static byte[] t1b5a(int v, int k, int y, String data) {


        byte[] dataStr = null;
        try {
            dataStr = data.getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (null == dataStr) {


            dataStr = new byte[]{0x31, 0x32, 0x33};

        }

        int reLength = dataStr.length + 7;

        byte[] reData = new byte[reLength];

        int iRe = 0;
        reData[iRe++] = 0x1B;
        reData[iRe++] = 0x5A;
        reData[iRe++] = (byte) v;
        reData[iRe++] = (byte) k;
        reData[iRe++] = (byte) y;
        reData[iRe++] = (byte) (dataStr.length % 256);
        reData[iRe++] = (byte) (dataStr.length / 256);

        for (byte b : dataStr
        ) {
            reData[iRe++] = b;
        }


        return reData;
    }


    /**
     * 设置打开双二维码支持，或者左边二维码，右边文字的功能支持。
     * 此只有新版 M 系列打印机支持。
     */

    public static byte[] t1f9a9a() {
        return new byte[]{0x1f, (byte) 0x9a, (byte) 0x9a};
    }


    /***
     *设置双二维码或者二维码与文字之间的间隙
     * 此命令只有新版 M 系列打印机支持。
     * @param n  n 的值为（0<=n<=64）。
     * @return
     */
    public static byte[] t1f9b(int n) {
        return new byte[]{0x1f, (byte) 0x9b, (byte) n};
    }

    /**
     * 【设置双二维码等特殊打印之间的位置关系
     * 1、设置双二维码小的这个针对大的这个，或者文字针对二维码的打印位置功能。
     * 2、n 的值有 3 个。0x30：上面对齐方式； 0x31：中间对齐方式； 0x32：底部对齐方式。
     * 3、此命令只有新版 M 系列打印机支持。
     *
     * @param n
     * @return
     */
    public static byte[] t1f9c(byte n) {


        return new byte[]{0x1f, (byte) 0x9c, n};


    }


    /**
     * 特殊功能的打印二维码指令
     *
     * @param a1、a1 表示版本号,等于零可以自动选择
     * @param a2    a2 为放大倍数，1~8 倍
     * @param a3    a3 为纠错级别 1~4 倍。
     * @param data  二维码数据。
     * @return
     */

    public static byte[] t1f9f(int a1, int a2, int a3, String data) {

        byte[] dataStr = null;
        try {
            dataStr = data.getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (null == dataStr) {
            //防止输入空内容
            dataStr = new byte[]{0x31, 0x32, 0x0a};
        }
        int dataLength = dataStr.length + 6;

        byte[] reData = new byte[dataLength];
        int iRe = 0;
        reData[iRe++] = 0x1f;
        reData[iRe++] = (byte) 0x9f;
        reData[iRe++] = (byte) a1;
        reData[iRe++] = (byte) a2;
        reData[iRe++] = (byte) a3;
        reData[iRe++] = (byte) dataStr.length;//数据长度

        for (byte b : dataStr) {
            reData[iRe++] = b;
        }
        return reData;
    }

    /**
     * 特殊功能的打印二维码右边文字指令
     *
     * @param data ,打印的字符（可打印字符，POS 命令字符等）。
     * @return
     */

    public static byte[] t1f9d(String data) {

        byte[] dataStr = null;
        try {
            dataStr = data.getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (null == dataStr) {
            //防止输入空内容
            dataStr = new byte[]{0x31, 0x32, 0x0a};
        }

        int dataLength = dataStr.length + 3;
        byte[] reData = new byte[dataLength];
        int iRe = 0;
        reData[iRe++] = 0x1f;
        reData[iRe++] = (byte) 0x9d;
        reData[iRe++] = (byte) dataStr.length;


        for (byte b : dataStr) {
            reData[iRe++] = b;
        }


        return reData;

    }


    /**
     * 特殊功能的打印二维码右边文字指令
     * (实例文字放大居中等)
     *
     * @return
     */

    public static byte[] t1f9dTest() {

        String data = "ABDC\n";

        byte[] dataStr1 = null; //左边文字
        byte[] dataStr2 = null;//居中文字
        byte[] dataStr3 = null;//右边文字
        //赋值

        try {
            dataStr1 = data.getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        data = "ABDC\n";
        try {
            dataStr2 = data.getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        data = "ABDC\n";
        try {
            dataStr3 = data.getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        byte[] dataStrLeftComm = {0x1b, 0x61, 0x30};//居左指令
        byte[] dataStrCenterComm = {0x1b, 0x61, 0x31};//居中指令
        byte[] dataStrRightComm = {0x1b, 0x61, 0x32};//居右指令

        byte[] dataStrScaleWidthComm = {0x1b, 0x21, 0x10};
        // byte[] dataStrScaleHeightComm = {0x1b,0x21,0x10};
        // byte[] dataStrScaleWidthHeightComm = {0x1d,0x21,0x20};
        byte[] reData = new byte[3 + 3 + dataStr1.length + dataStr2.length + dataStr3.length];


        int iRe = 0;
        for (byte b : dataStrScaleWidthComm) {
            reData[iRe++] = b;
        }
        reData[iRe++] = 0x1f;
        reData[iRe++] = (byte) 0x9d;
        reData[iRe++] = (byte) (reData.length - 6);

        //
//        for (byte b : dataStrLeftComm ) {
//            reData[iRe++] = b;
//        }

        for (byte b : dataStr1) {
            reData[iRe++] = b;
        }

//        for (byte b : dataStrCenterComm ) {
//            reData[iRe++] = b;
//        }
//        for (byte b : dataStrScaleHeightComm) {
//            reData[iRe++] = b;
//        }
        for (byte b : dataStr2) {
            reData[iRe++] = b;
        }


//        for (byte b : dataStrRightComm ) {
//            reData[iRe++] = b;
//        }
//        for (byte b : dataStrScaleWidthHeightComm ) {
//            reData[iRe++] = b;
//        }
        for (byte b : dataStr3) {
            reData[iRe++] = b;
        }

        return reData;

    }


}
