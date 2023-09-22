package com.fhxh.shpptingsystem.utils;

public class ByteUtil {

    //public static void main(String[] args) {
    //    byte[] bytes = {
    //        (byte) 0xab, 0x01, 0x11
    //    };
    //    String hexStr = bytes2HexStr(bytes);
    //    System.out.println(hexStr);
    //    System.out.println(hexStr2decimal(hexStr));
    //    System.out.println(decimal2fitHex(570));
    //    String adc = "abc";
    //    System.out.println(str2HexString(adc));
    //    System.out.println(bytes2HexStr(adc.getBytes()));
    //}

    /**
     * 字节数组转换成对应的16进制表示的字符串
     *
     * @param src
     * @return
     */
    public static String bytes2HexStr(byte[] src) {
        StringBuilder builder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return "";
        }
        char[] buffer = new char[2];
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
            builder.append(buffer);
        }
        return builder.toString().toUpperCase();
    }

    /**
     * 十六进制字节数组转字符串
     *
     * @param src 目标数组
     * @param dec 起始位置
     * @param length 长度
     * @return
     */
    public static String bytes2HexStr(byte[] src, int dec, int length) {
        byte[] temp = new byte[length];
        System.arraycopy(src, dec, temp, 0, length);
        return bytes2HexStr(temp);
    }

    /**
     * 16进制字符串转10进制数字
     *
     * @param hex
     * @return
     */
    public static int hexStr2decimal(String hex) {
        return Integer.parseInt(hex, 16);
    }

    /**
     * 把十进制数字转换成足位的十六进制字符串,并补全空位
     *
     * @param num
     * @return
     */


    public static String decimal2fitHex(long num) {
        String hex = Long.toHexString(num).toUpperCase();
        if (hex.length() >2) {
            if (hex.length() % 2 != 0) {
                return hex;
            }
        } else {
            if (hex.length() % 2 != 0) {
                return "0" + hex;
            } else {
                return hex;
            }
        }
        return hex.toUpperCase();
    }


    /**
     * 把十进制数字转换成足位的十六进制字符串,并补全空位
     *
     * @param num
     * @param strLength 字符串的长度
     * @return
     */
    public static String decimal2fitHex(int num, int strLength) {
        String hexStr = Integer.toHexString(num).toUpperCase();
        StringBuilder stringBuilder = new StringBuilder(hexStr);
        while (stringBuilder.length() < strLength) {
            stringBuilder.insert(0, '0');
        }
        return stringBuilder.toString();
    }

    public static String fitDecimalStr(int dicimal, int strLength) {
        StringBuilder builder = new StringBuilder(String.valueOf(dicimal));
        while (builder.length() < strLength) {
            builder.insert(0, "0");
        }
        return builder.toString();
    }

    /**
     * 字符串转十六进制字符串
     *
     * @param str
     * @return
     */
    public static String str2HexString(String str) {
        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder();
        byte[] bs = null;
        try {

            bs = str.getBytes("utf8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
        }
        return sb.toString();
    }

    /**
     * 把十六进制表示的字节数组字符串，转换成十六进制字节数组
     *
     * @param
     * @return byte[]
     */
    public static byte[] hexStr2bytes(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toUpperCase().toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (hexChar2byte(achar[pos]) << 4 | hexChar2byte(achar[pos + 1]));
        }
        return result;
    }

    /**
     * 把16进制字符[0123456789abcde]（含大小写）转成字节
     *
     * @param c
     * @return
     */
    public static int hexChar2byte(char c) {
        switch (c) {
            case '0':
                return 0;
            case '1':
                return 1;
            case '2':
                return 2;
            case '3':
                return 3;
            case '4':
                return 4;
            case '5':
                return 5;
            case '6':
                return 6;
            case '7':
                return 7;
            case '8':
                return 8;
            case '9':
                return 9;
            case 'a':
            case 'A':
                return 10;
            case 'b':
            case 'B':
                return 11;
            case 'c':
            case 'C':
                return 12;
            case 'd':
            case 'D':
                return 13;
            case 'e':
            case 'E':
                return 14;
            case 'f':
            case 'F':
                return 15;
            default:
                return -1;
        }
    }

    public static String Byteget8Bit(byte by){
        StringBuffer sb = new StringBuffer();
        sb.append((by>>7)&0x1).append((by>>6)&0x1).append((by>>5)&0x1).append((by>>4)&0x1)
                .append((by>>3)&0x1).append((by>>2)&0x1).append((by>>1)&0x1).append((by>>0)&0x1);
        return sb.toString();
    }

    public static String INTget16Bit(int by){
        int kk = by & 0x0000FFFF;
        StringBuffer sb = new StringBuffer();
        sb.append((kk>>15)&0x1).append((kk>>14)&0x1).append((kk>>13)&0x1).append((kk>>12)&0x1)
                .append((kk>>11)&0x1).append((kk>>10)&0x1).append((kk>>9)&0x1).append((kk>>8)&0x1)
                .append((kk>>7)&0x1).append((kk>>6)&0x1).append((kk>>5)&0x1).append((kk>>4)&0x1)
                .append((kk>>3)&0x1).append((kk>>2)&0x1).append((kk>>1)&0x1).append((kk>>0)&0x1);
        return sb.toString();
    }

    public static String BytegetBit8Reverse(byte by){
        StringBuffer sb = new StringBuffer();
        sb.append((by>>0)&0x1).append((by>>1)&0x1).append((by>>2)&0x1).append((by>>3)&0x1)
                .append((by>>4)&0x1).append((by>>5)&0x1).append((by>>6)&0x1).append((by>>7)&0x1);
        return sb.toString();
    }
    public static String INTBit16Reverse(int by){
        int kk = by & 0x0000FFFF;
        StringBuffer sb = new StringBuffer();
        sb.append((kk>>0)&0x1).append((kk>>1)&0x1).append((kk>>2)&0x1).append((kk>>3)&0x1)
                .append((kk>>4)&0x1).append((kk>>5)&0x1).append((kk>>6)&0x1).append((kk>>7)&0x1)
                .append((kk>>8)&0x1).append((kk>>9)&0x1).append((kk>>10)&0x1).append((kk>>11)&0x1)
                .append((kk>>12)&0x1).append((kk>>13)&0x1).append((kk>>14)&0x1).append((kk>>15)&0x1);

        return sb.toString();
    }

    public static byte bitStringToByte(String str) {
        if(null == str){
            throw new RuntimeException("when bit string convert to byte, Object can not be null!");
        }
        if (8 != str.length()){
            throw new RuntimeException("bit string'length must be 8");
        }
        try{
            //判断最高位，决定正负
            if(str.charAt(0) == '0'){
                return (byte) Integer.parseInt(str,2);
            }else if(str.charAt(0) == '1'){
                return (byte) (Integer.parseInt(str,2) - 256);
            }
        }catch (NumberFormatException e){
            throw new RuntimeException("bit string convert to byte failed, byte String must only include 0 and 1!");
        }

        return 0;
    }

    public static String byteToHex(byte[] bytes){
        String strHex = "";
        StringBuilder sb = new StringBuilder("");
        for (int n = 0; n < bytes.length; n++) {
            strHex = Integer.toHexString(bytes[n] & 0xFF);
            sb.append((strHex.length() == 1) ? "0" + strHex : strHex); // 每个字节由两个字符表示，位数不够，高位补0
        }
        return sb.toString().trim();
    }
}

